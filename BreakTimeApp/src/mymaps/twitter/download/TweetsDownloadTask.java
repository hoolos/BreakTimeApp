package mymaps.twitter.download;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mymaps.utils.AbstractAppActivity;
import mymaps.utils.TweetRowList;
import mymaps.utils.TweetsListAdapter;
import mymaps.utils.TwitterSingleton;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Adapter;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.User;

public class TweetsDownloadTask implements Runnable{

	

	private Twitter twitter;
	private User user;
	private TweetsListAdapter adapter;
	private final int count=4;
	private Handler handler;
	private ExecutorService exec;
	
	public TweetsDownloadTask(Handler handler, User user,TweetsListAdapter adapter){
		this.adapter=adapter;
		this.user=user;
		this.handler=handler;
		twitter=TwitterSingleton.getInstance().getTwitter();
		exec=Executors.newFixedThreadPool(4);
	}
	
	
	private class Task2 implements Runnable{

		private TweetRowList tList;
		private String url1;
		
		public Task2(String url,TweetRowList tList){
			this.tList=tList;
			this.url1=url;
		}
		@Override
		public void run() {
			try{
				URL url=new URL(url1);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            BitmapFactory.Options options = new BitmapFactory.Options();
	            options.inJustDecodeBounds = true;
	            options.inSampleSize=calculateInSampleSize(options, 
	            		AbstractAppActivity.DISPLAY_HEIGHTS/4,
	            		AbstractAppActivity.DISPLAY_WIDTH/4);
	            options.inJustDecodeBounds = false;
	            Bitmap myBitmap = BitmapFactory.decodeStream(input,null,options);
	            
	            tList.setImage(myBitmap);
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
	public void run() {
		try{
			Paging page =new Paging(1, count);
			ResponseList<twitter4j.Status> tweetsList=twitter.getUserTimeline(user.getId(), page);
			List<TweetRowList> rows=new ArrayList<TweetRowList>();
			TweetRowList tweet;
			for(twitter4j.Status status: tweetsList){
				MediaEntity[] mEntities=status.getMediaEntities();
				tweet=new TweetRowList(status.getUser(),null, status.getText());
				if(mEntities.length!=0){
					String image=mEntities[0].getMediaURL();
					exec.execute(new Task2(image, tweet));
				}
				rows.add(tweet);
			}
			adapter.setRows(rows);
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
}
