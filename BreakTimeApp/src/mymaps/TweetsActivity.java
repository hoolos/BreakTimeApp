package mymaps;

import java.io.InputStream;
import java.lang.Thread.State;
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

import mymaps.builders.ListItem;
import mymaps.builders.ListItemBuilder;
import mymaps.builders.TweetListItemBuilder;
import mymaps.managers.TwitterDownloadManager;
import mymaps.sqldb.DBManagerForListItem;
import mymaps.utils.AbstractAppActivity;
import mymaps.utils.CachedStructure;
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
import android.os.Handler;
import android.os.Message;
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

public class TweetsActivity  extends Activity{

	public static FriendRowList userInfo;
	private TextView textView;
	private ImageView imageView;
	private RelativeLayout rLayout;
	private TweetsListAdapter adapter;
	private ListView lView;
	private TwitterDownloadManager<ListItemBuilder> dManager;
	private Thread thread;
	private DBManagerForListItem dbManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_tweets_layout);
        lView=(ListView) findViewById(R.id.listView1);
        userInfo= (FriendRowList) getIntent().getExtras().getParcelable("User");
        textView=(TextView) findViewById(R.id.user_name);
        imageView=(ImageView) findViewById(R.id.imageView4);
        rLayout=(RelativeLayout) findViewById(R.id.rel_layout1);
        textView.setText(userInfo.getUser().getName());
        imageView.setImageBitmap(userInfo.getImage());
        Handler handler=new Handler(new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message arg0) {
				if(arg0.getData()!=null){
					try{
						@SuppressWarnings("unchecked")
						CachedStructure< ListItem> cachedStructure=
								(CachedStructure<ListItem>) arg0.getData().
								getSerializable("Parcel");
						if(cachedStructure!=null)
							adapter.setRows(cachedStructure);
						adapter.notifyDataSetChanged();
						return true;
						
					}catch(Exception e){
						Log.d("error", e.getMessage());
						e.printStackTrace();
					}
				}
				return false;
			}
		});
        lView=(ListView) findViewById(R.id.listView1);
        adapter=new TweetsListAdapter(null, getLayoutInflater());
        lView.setAdapter(adapter);
        dbManager=new DBManagerForListItem(getApplicationContext());
        dManager=new TwitterDownloadManager<ListItemBuilder>(handler, dbManager);
        dManager.start();
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
				if(view.getCount()==lView.getLastVisiblePosition()+1)
					if(this.SCROLL_STATE_TOUCH_SCROLL==scrollState&&!dManager.isRinning())
						dManager.start();
				if(this.SCROLL_STATE_IDLE==scrollState){
					Log.d("123", "SCROLL_STATE_IDLE");
					Log.d("123", view.getCount()+"+"+lView.getLastVisiblePosition());
					//dManager.start();
					//if(thread.getState()==State.TERMINATED)
						//thread.start();
					//thread.s
				}
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
        
	}

	public void ifEndOfList(){
		if(adapter.getCount()==(lView.getLastVisiblePosition()+1)){
			Log.d("123", thread.getState().name());
			//if(thread.getState()==State.TERMINATED)
				//thread.start();
			//thread.s
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dbManager.deleteDataBase();
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

	

	
}
