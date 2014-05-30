package mymaps;

import mymaps.cache.CachedStracture;
import mymaps.db.dao.TweetsDao;
import mymaps.list.items.FriendListItem;
import mymaps.strategies.TweetsActivityStrategy;
import mymaps.utils.TweetsListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.breaktimeapp.R;

public class TweetsActivity extends BaseAppActivity {

    public static FriendListItem userInfo;
    private TextView textView;
    private ImageView imageView;
    private RelativeLayout rLayout;
    private TweetsListAdapter adapter;
    private ListView lView;
    private TweetsActivityStrategy strategy;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mp_tweets_layout);
	lView = (ListView) findViewById(R.id.listView1);
	userInfo = (FriendListItem) getIntent().getExtras().getParcelable(
		"User");
	textView = (TextView) findViewById(R.id.user_name);
	imageView = (ImageView) findViewById(R.id.imageView4);
	rLayout = (RelativeLayout) findViewById(R.id.rel_layout1);
	textView.setText(userInfo.getUser().getName());
	imageView.setImageBitmap(userInfo.getImage());

	Handler handler = new Handler(new Handler.Callback() {

	    @Override
	    public boolean handleMessage(Message arg0) {
		if (arg0.getData() != null) {
		    try {
			@SuppressWarnings("unchecked")
			CachedStracture<TweetsDao> cachedStructure = (CachedStracture<TweetsDao>) arg0
				.getData().getSerializable("Parcel");
			if (cachedStructure != null)
			    adapter.setRows(cachedStructure);
			adapter.notifyDataSetChanged();
			return true;

		    } catch (Exception e) {
			Log.d("error", e.getMessage());
			e.printStackTrace();
		    }
		}
		return false;
	    }
	});
	strategy = new TweetsActivityStrategy(getApplicationContext(), handler);
	lView = (ListView) findViewById(R.id.listView1);
	adapter = new TweetsListAdapter(null, getLayoutInflater());
	lView.setAdapter(adapter);
	rLayout.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		startActivity(new Intent(getApplicationContext(),
			SendDMActivity.class));

	    }
	});
	lView.setOnScrollListener(new OnScrollListener() {

	    @Override
	    public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.d("124", "op");
		if (view.getCount() == lView.getLastVisiblePosition() + 1)
		    if (this.SCROLL_STATE_TOUCH_SCROLL == scrollState
			    && !strategy.isDowloading())
			strategy.downloadTweets();
		if (this.SCROLL_STATE_IDLE == scrollState) {
		    Log.d("123", "SCROLL_STATE_IDLE");
		    Log.d("123",
			    view.getCount() + "+"
				    + lView.getLastVisiblePosition());
		    // dManager.start();
		    // if(thread.getState()==State.TERMINATED)
		    // thread.start();
		    // thread.s
		}

	    }

	    @Override
	    public void onScroll(AbsListView view, int firstVisibleItem,
		    int visibleItemCount, int totalItemCount) {

	    }
	});

    }

    public void ifEndOfList() {
	if (adapter.getCount() == (lView.getLastVisiblePosition() + 1)) {
	    Log.d("123", thread.getState().name());
	    // if(thread.getState()==State.TERMINATED)
	    // thread.start();
	    // thread.s
	}

    }

    @Override
    protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	strategy.deleteDataBase();
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
