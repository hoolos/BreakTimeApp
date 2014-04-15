package mymaps;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.User;

import com.example.breaktimeapp.R;
import com.google.android.gms.drive.internal.r;

import mymaps.twitter.download.TweetsDownloadTask;
import mymaps.utils.AbstractAppActivity;
import mymaps.utils.FriendRowList;
import mymaps.utils.TweetRowList;
import mymaps.utils.TweetsListAdapter;
import mymaps.utils.TwitterSingleton;
import android.R.attr;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TweetsActivity extends AbstractAppActivity{

	private User user;
	private FriendRowList rowList;
	private TextView textView;
	private ImageView imageView;
	private RelativeLayout rLayout;
	private TweetsListAdapter adapter;
	private ListView lView;
	private TweetsDownloadTask task;
	private Task12 task12;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_tweets_layout);
        lView=(ListView) findViewById(R.id.listView1);
        adapter=new TweetsListAdapter(new ArrayList<TweetRowList>() , 
        		getLayoutInflater());
        lView.setAdapter(adapter);
        rowList= (FriendRowList) getIntent().getExtras().getParcelable("User");
        user=rowList.getUser();
        textView=(TextView) findViewById(R.id.tweet_text);
        imageView=(ImageView) findViewById(R.id.imageView4);
        rLayout=(RelativeLayout) findViewById(R.id.rel_layout1);
        textView.setText(rowList.getUser().getName());
        imageView.setImageBitmap(rowList.getImage());
        task=new TweetsDownloadTask(TwitterSingleton.getInstance().getTwitter()
        		, user, adapter);
        task12=new Task12();
        Thread thread=new Thread(task12);
        thread.start();
        rLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),SendDMActivity.class));
				
			}
		});
        lView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.d("124", "op");
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.d("123", task.getStatus().name());
				ifEndOfList();
				
			}
		});
        
	}

	public void ifEndOfList(){
		if(adapter.getCount()==(lView.getLastVisiblePosition()+1)){
			Thread thread =new Thread(task12);
			thread.start();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private class Task12 implements Runnable{
		private int count=4;
		
		public Task12(){
		}
		@Override
		public void run() {
			try{
			count+=count;
			Paging page =new Paging(1, count);
			ResponseList<twitter4j.Status> tweetsList=TwitterSingleton.
					getInstance().getTwitter()
					.getUserTimeline(user.getId(), page);
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
			TweetsActivity.this.runOnUiThread(new Runnable() {
				
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
		

class Task2 implements Runnable{

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
            TweetsActivity.this.runOnUiThread(new Runnable() {
				
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
}
