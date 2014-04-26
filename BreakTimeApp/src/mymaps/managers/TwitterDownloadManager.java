package mymaps.managers;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mymaps.TweetsActivity;
import mymaps.builders.ListItem;
import mymaps.builders.ListItemBuilder;
import mymaps.builders.TweetListItem;
import mymaps.sqldb.DBManagerForListItem;
import mymaps.utils.AbstractAppActivity;
import mymaps.utils.CachedStructure;
import mymaps.utils.TweetRowList;
import mymaps.utils.TwitterSingleton;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class TwitterDownloadManager<T extends ListItemBuilder> implements NotifyDownloadManager{
	
	private static Twitter twitter=TwitterSingleton.getInstance().getTwitter();
	private int count=1;
	private Handler handler;
	private  ExecutorService exec;
	private  ExecutorService exec1;
	private Thread mainThread;
	private CachedStructure<ListItem> lruCache;
	private boolean isRunning=false;
	private DBManagerForListItem dbManager;
	
	public TwitterDownloadManager(Handler handl, DBManagerForListItem dbManageRef){
		handler=handl;
		lruCache=new CachedStructure<ListItem>(35);
		lruCache.setDownloadManager((TwitterDownloadManager<ListItemBuilder>) this);
		exec=Executors.newFixedThreadPool(6);
		exec1=Executors.newFixedThreadPool(6);
		dbManager=dbManageRef;
		dbManager.setNottifyDownloadManager((NotifyDownloadManager)this);
	}
	public boolean isRinning(){
		return isRunning;
	}
	//Два раза качается твит
	public void start(){
			//lruCache=new CachedStructure<Long,TweetRowList>(20);
			isRunning=true;
			exec.execute(new Runnable() {
				
				@Override
				public void run() {
					try{
						Paging page =new Paging(count++);
						ResponseList<twitter4j.Status> tweetsList=twitter.getUserTimeline(TweetsActivity.userInfo.
								getUser().getId(), page);
						Future<Void> future;
						dbManager.bulkPutToDB(tweetsList.subList(0, tweetsList.size()));
						for(Status status: tweetsList){
							MediaEntity[] mEntities=status.getMediaEntities();
							lruCache.addItemId(status.getId());
							ListItem item =new TweetListItem();
							item.addStatus(status);
							if(mEntities.length!=0){
								future=exec1.submit(new Task2(mEntities[0].getMediaURL(),
										item));
								item.addThreadRef(future);
							}else{
								item.addThreadRef(null);
							}
							lruCache.setDBManager(dbManager);
							lruCache.put(lruCache.getItemsCount(), item);
						}
						//adapter.setRows(rows);
						Message msg=new Message();
						Bundle bundle=new Bundle();
						bundle.putSerializable("Parcel", lruCache);
						msg.setData(bundle);
						handler.sendMessage(msg);
						isRunning=false;
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
			});
	}
	
	public void stopIfDownloading(TweetRowList tList,int pos){
		
		Future<Void> future=tList.getRefThread().get();
		
		if(future.isDone()||future==null){
			return;
		}else {
			future.cancel(true);
		}
			/*Status status=tList.getStatus().get();
			if(status==null){
				exec.execute(new Task1(lruCache.getItemId(pos), tList));
				
			}else{
				tList.setRefThread(exec.submit(new Task2(st, tList)));
			}
		}*/
			
	}
	public  void downloadTweet(Long sLong,ListItem tList){
		Thread thread=new Thread(new Task1(sLong, tList));
		thread.start();
		
	}
	private  class Task1 implements Runnable{

		private Long statusId;
		private ListItem tList;
		
		public Task1(Long status, ListItem tList){
			this.statusId=status;
			this.tList=tList;
		}
		@Override
		public void run() {
			try{
				
				Future<Void> future;
				Status status=twitter.showStatus(statusId);
				MediaEntity[] mEntities=status.getMediaEntities();
				tList.addStatus(status);
				if(mEntities.length!=0){
					future=exec.submit(new Task2(mEntities[0].getMediaURL(),
							tList));
					tList.addThreadRef(future);
				}else{
					tList.addThreadRef(null);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class Task2 implements Callable<Void>{

		private ListItem tList;
		private String url1;
		
		public Task2(String url,ListItem tList){
			this.tList=tList;
			this.url1=url;
		}
		@Override
		public Void call() {
			try{
				URL url=new URL(url1);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            BitmapFactory.Options options = new BitmapFactory.Options();
	            options.inJustDecodeBounds = true;
	            options.inSampleSize=calculateInSampleSize(options, 
	            		AbstractAppActivity.DISPLAY_HEIGHTS/8,
	            		AbstractAppActivity.DISPLAY_WIDTH/8);
	            options.inJustDecodeBounds = false;
	            Bitmap myBitmap = BitmapFactory.decodeStream(input,null,options);
	            
	            tList.addBitmap((myBitmap));
	            dbManager.updateBitmap(tList.getStatus().getId(), myBitmap);
				handler.sendMessage(new Message());
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
			public int calculateInSampleSize(
		            BitmapFactory.Options options, int reqWidth, int reqHeight) {
		    // Raw height and width of image
		    final int height = options.outHeight;
		    final int width = options.outWidth;
		    int inSampleSize = 1;

		    if (height > reqHeight || width > reqWidth) {

		        final int halfHeight = height / 2;
		        final int halfWidth = width / 2;

		        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
		        // height and width larger than the requested height and width.
		        while ((halfHeight / inSampleSize) > reqHeight
		                && (halfWidth / inSampleSize) > reqWidth) {
		            inSampleSize *= 2;
		        }
		    }
		    
		    return inSampleSize;
		}
			
	}

	@Override
	public void callback() {
		handler.sendMessage(new Message());
	}
	
}
