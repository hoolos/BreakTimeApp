package mymaps.twitter.download;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.android.gms.drive.internal.AddEventListenerRequest;

import mymaps.TweetsActivity;
import mymaps.utils.AbstractAppActivity;
import mymaps.utils.CachedStructure;
import mymaps.utils.TweetRowList;
import mymaps.utils.TweetsListAdapter;
import mymaps.utils.TwitterSingleton;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.LruCache;
import android.widget.Adapter;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;

public class TweetsDownloadTask {

	

	private Twitter twitter;
	private TweetsListAdapter adapter;
	private int count=1;
	private Handler handler;
	private ExecutorService exec;
	private Thread mainThread;
	private CachedStructure<Long, TweetRowList> lruCache;
	
	public TweetsDownloadTask(Handler handler, TweetsListAdapter adapter){
		this.adapter=adapter;
		this.handler=handler;
		twitter=TwitterSingleton.getInstance().getTwitter();
		exec=Executors.newFixedThreadPool(10);
		lruCache=new CachedStructure<Long,TweetRowList>(20);
	
		
	}
	
	//Два раза качается твит
	public void start(){
		try{
			mainThread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					try{
						Paging page =new Paging(count++);
						ResponseList<twitter4j.Status> tweetsList=twitter.getUserTimeline(TweetsActivity.user.getId(), page);
						TweetRowList tweet;
						Future<Void> future;
						for(Status status: tweetsList){
							MediaEntity[] mEntities=status.getMediaEntities();
							lruCache.addItemId(status.getId());
							tweet=new TweetRowList(new WeakReference<Status>(status));
							if(mEntities.length!=0){
								future=exec.submit(new Task2(mEntities[0].getMediaURL(),
										tweet));
								tweet.setRefThread(new WeakReference<Future<Void>>(future));
							}else{
								tweet.setRefThread(null);
							}
							lruCache.put(lruCache.getItemsCount(),tweet);
						}
						//adapter.setRows(rows);
						 handler.post(new Runnable() {
								
								@Override
								public void run() {
									adapter.notifyDataSetChanged();
									
								}
							});
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void stopIfDownloading(TweetRowList tList,int pos){
		
		Future<Void> future=(Future<Void>) tList.getRefThread();
		
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
	public void reloadTweet(Long sLong,TweetRowList tList){
		exec.execute(new Task1(sLong, tList));
		
	}
	private class Task1 implements Runnable{

		private Long statusId;
		private TweetRowList tList;
		
		public Task1(Long status, TweetRowList tList){
			this.statusId=status;
			this.tList=tList;
		}
		@Override
		public void run() {
			try{
				
				Future<Void> future;
				Status status=twitter.showStatus(statusId);
				MediaEntity[] mEntities=status.getMediaEntities();
				tList.setStatus(new WeakReference<Status>(status));
				if(mEntities.length!=0){
					future=exec.submit(new Task2(mEntities[0].getMediaURL(),
							tList));
					tList.setRefThread(new WeakReference<Future<Void>>(future));
				}else{
					tList.setRefThread(null);
				}
				tList.setStatus(new WeakReference<Status>(status));
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class Task2 implements Callable<Void>{

		private TweetRowList tList;
		private String url1;
		
		public Task2(String url,TweetRowList tList){
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
	            
	            tList.setBitmap(new WeakReference<Bitmap>(myBitmap));
	            handler.post(new Runnable() {
					
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						
					}
				});
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
	
}
