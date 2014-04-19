package mymaps.utils;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;
import mymaps.TweetsActivity;
import mymaps.builders.ListItem;

import com.example.breaktimeapp.R;
import com.google.android.gms.drive.internal.e;

import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsListAdapter extends BaseAdapter{

	private CachedStructure<ListItem> tweetRowList;
	private LayoutInflater inflater;
	private TweetViewHolder holder;
	private User user;
	public TweetsListAdapter(CachedStructure<ListItem> list, LayoutInflater inflater){
		if(list==null){
			this.tweetRowList=new CachedStructure<ListItem>(1);
		}else{
			this.tweetRowList=list;
		}
		this.inflater=inflater;
		user=TweetsActivity.user;
		
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
		
		ListItem row=(ListItem) getItem(arg0);
		if(arg1==null){
			arg1=inflater.inflate(R.layout.mp_tweets_entry, null);
			holder=new TweetViewHolder();
			holder.name=(TextView) arg1.findViewById(R.id.user_name);
			holder.text=(TextView) arg1.findViewById(R.id.tweet_text);
			holder.profile=(ImageView) arg1.findViewById(R.id.imageView4);
			holder.imageView=(ImageView) arg1.findViewById(R.id.imageView3);
			holder.tList=row;
			arg1.setTag(holder);
		}
			Status status=row.getStatus();
			holder=(TweetViewHolder) arg1.getTag();
			if(holder.tList.getThreadRef()!=null){
				try{
					if(holder.tList.getThreadRef().get()!=null)
						holder.tList.getThreadRef().get().cancel(true);
				}catch(Exception e){
					Log.d("error", e.getMessage());
					e.printStackTrace();
				}
			}
			holder.name.setText(user.getName());
			holder.text.setText(status==null?"":status.getText());
			holder.tList=row;
			holder.imageView.setImageBitmap(row.getBitmap());
		return arg1;
	}
	
	@Override
	public synchronized void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	};
	
	private class TweetViewHolder{
		public TextView name;
		public TextView text;
		public ImageView imageView;
		public ImageView profile;
		public ListItem tList;
	}
	public void setUser(User user){
		this.user=user;
	}
	public void setRows(CachedStructure< ListItem> list){
		this.tweetRowList=list;
	}
	

}
