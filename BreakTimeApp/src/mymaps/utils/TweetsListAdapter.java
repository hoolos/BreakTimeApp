package mymaps.utils;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;
import mymaps.twitter.download.TweetsDownloadTask;

import com.example.breaktimeapp.R;

import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsListAdapter extends BaseAdapter{

	private CachedStructure<Long,TweetRowList> tweetRowList;
	private LayoutInflater inflater;
	private TweetViewHolder holder;
	private TweetsDownloadTask task;
	private User user;
	public TweetsListAdapter(CachedStructure<Long,TweetRowList> list, LayoutInflater inflater, 
			TweetsDownloadTask task){
		if(list==null){
			this.tweetRowList=new CachedStructure<Long,TweetRowList>(0);
		}else{
			this.tweetRowList=list;
		}
		this.task=task;
		this.inflater=inflater;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tweetRowList.putCount();
	}

	@Override
	public Object getItem(int arg0) {
		if(tweetRowList.get(arg0)==null){
			TweetRowList tList=new TweetRowList();
			tweetRowList.put(arg0,tList);
			task.reloadTweet(tweetRowList.getItemId(arg0), tList);
		}
		return tweetRowList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		TweetRowList row=(TweetRowList) getItem(arg0);
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
		if(checkIfExpired(row)){
			task.reloadTweet(tweetRowList.getItemId(arg0), row);
			holder=(TweetViewHolder) arg1.getTag();
			holder.name.setText(user.getName());
			holder.text.setText("123");
			holder.tList=row;
			holder.imageView.setImageBitmap(null);
		}else{
			
			Status status=row.getStatus().get();
		
			holder=(TweetViewHolder) arg1.getTag();
			task.stopIfDownloading(holder.tList, arg0);
			holder.name.setText(user.getName());
			holder.text.setText(status.getText());
			holder.tList=row;
			holder.imageView.setImageBitmap(row.getBitmap().get());
			//if(row.getImage()!=null)
				//arg1.
				//holder.imageView.setVisibility(View.VISIBLE);
		}
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
		public TweetRowList tList;
	}
	public void setUser(User user){
		this.user=user;
	}
	public void setRows(CachedStructure<Long, TweetRowList> list){
		this.tweetRowList=list;
	}
	
	private boolean checkIfExpired(TweetRowList tList){
		if(tList.getBitmap().get()==null||
				tList.getStatus().get()==null||
				tList.getRefThread().get()==null){
			return true;
		}else {
			return false;
		}
	}

}
