package mymaps.utils;

import java.util.List;

import com.example.breaktimeapp.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetsListAdapter extends BaseAdapter{

	private List<TweetRowList> tweetRowList;
	private LayoutInflater inflater;
	private TweetViewHolder holder;
	public TweetsListAdapter(List<TweetRowList> list, LayoutInflater inflater){
		
		this.tweetRowList=list;
		this.inflater=inflater;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tweetRowList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
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
			arg1.setTag(holder);
		}
		holder=(TweetViewHolder) arg1.getTag();
		holder.name.setText(row.getUser().getName());
		holder.text.setText(row.getTweet());
		holder.imageView.setImageBitmap(row.getImage());
		//if(row.getImage()!=null)
			//arg1.
			//holder.imageView.setVisibility(View.VISIBLE);
		return arg1;
	}
	
	private class TweetViewHolder{
		public TextView name;
		public TextView text;
		public ImageView imageView;
		public ImageView profile;
		
	}
	
	public void setRows(List<TweetRowList> list){
		this.tweetRowList=list;
	}

}
