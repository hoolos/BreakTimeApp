package mymaps.utils;

import java.util.Map;
import java.util.concurrent.Future;

import mymaps.TweetsActivity;
import mymaps.cache.CachedStracture;
import mymaps.db.columns.TweetsImageResColumns;
import mymaps.db.columns.TweetsTextResColumns;
import mymaps.db.dao.TweetsDao;
import mymaps.list.items.BaseListItem;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.breaktimeapp.R;

public class TweetsListAdapter extends BaseAdapter {

    private static final String TAG = "TweetsAdapter";
    private CachedStracture<TweetsDao> tweetRowList;
    private final LayoutInflater inflater;
    private TweetViewHolder holder;

    public TweetsListAdapter(CachedStracture<TweetsDao> list,
	    LayoutInflater inflater) {
	if (list == null) {
	    this.tweetRowList = new CachedStracture<TweetsDao>(1);
	} else {
	    this.tweetRowList = list;
	}
	this.inflater = inflater;

    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return tweetRowList.getItemCount();
    }

    @Override
    public Object getItem(int arg0) {
	return tweetRowList.persistentGet(arg0);
    }

    @Override
    public long getItemId(int arg0) {

	return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

	BaseListItem row = (BaseListItem) getItem(arg0);
	if (arg1 == null) {
	    arg1 = inflater.inflate(R.layout.mp_tweets_entry, null);
	    holder = new TweetViewHolder();
	    holder.name = (TextView) arg1.findViewById(R.id.user_name);
	    holder.text = (TextView) arg1.findViewById(R.id.tweet_text);
	    holder.profile = (ImageView) arg1.findViewById(R.id.imageView4);
	    holder.imageView = (ImageView) arg1.findViewById(R.id.imageView3);
	    holder.tList = row;
	    arg1.setTag(holder);
	}
	Map<String, String> textResMap = row.getText();
	Map<String, Bitmap> bitmapResMap = row.getBitmap();
	holder = (TweetViewHolder) arg1.getTag();
	if (holder.tList.getRefThreads() != null) {
	    try {
		for (Future<?> f : holder.tList.getRefThreads()) {
		    f.cancel(false);
		}
	    } catch (Exception e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	}
	holder.name.setText(TweetsActivity.userInfo.getUser().getName());
	holder.text.setText(textResMap.get(TweetsTextResColumns.TweetText
		.toString()) == null ? "" : textResMap
		.get(TweetsTextResColumns.TweetText.toString()));
	holder.tList = row;
	holder.imageView.setImageBitmap(bitmapResMap
		.get(TweetsImageResColumns.TweetImage.toString()));
	holder.profile.setImageBitmap(TweetsActivity.userInfo.getImage());
	return arg1;
    }

    @Override
    public synchronized void notifyDataSetChanged() {
	super.notifyDataSetChanged();
    };

    private class TweetViewHolder {
	public TextView name;
	public TextView text;
	public ImageView imageView;
	public ImageView profile;
	public BaseListItem tList;
    }

    public void setRows(CachedStracture<TweetsDao> list) {
	this.tweetRowList = list;
    }

}
