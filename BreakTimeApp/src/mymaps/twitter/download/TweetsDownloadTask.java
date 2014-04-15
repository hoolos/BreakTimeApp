package mymaps.twitter.download;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mymaps.utils.TweetRowList;
import mymaps.utils.TweetsListAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Adapter;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.User;

public class TweetsDownloadTask extends AsyncTask<Integer, Void, Void>{

	

	private Twitter twitter;
	private User user;
	private TweetsListAdapter adapter;
	private int count=0;
	public TweetsDownloadTask(Twitter twitter, User user,TweetsListAdapter adapter){
		this.adapter=adapter;
		this.twitter=twitter;
		this.user=user;
	}
	
	

	@Override
	public Void doInBackground(Integer... params) {
		try{
			count=4;
			Paging page =new Paging(1, count);
			ResponseList<twitter4j.Status> tweetsList=twitter.getUserTimeline(user.getId(), page);
			List<TweetRowList> rows=new ArrayList<TweetRowList>();
			ExecutorService exec=Executors.newCachedThreadPool();
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
			publishProgress();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	@Override
	public synchronized void onProgressUpdate(Void... values) {
		adapter.notifyDataSetChanged();
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
	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
	            
	            tList.setImage(myBitmap);
	            publishProgress();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
}
