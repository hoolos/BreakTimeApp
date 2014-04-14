package mymaps.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.breaktimeapp.R;

import twitter4j.User;
import twitter4j.examples.suggestedusers.GetUserSuggestions;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter{

	private List<FriendRowList> users;
	private LayoutInflater mInflater;
	private FriendListHolder holder;
	
	public FriendListAdapter(List<FriendRowList> users, LayoutInflater mInflater){
		if(users==null){
			this.users=new ArrayList<FriendRowList>();
		}
		else{
			this.users=users;
		}
		this.mInflater=mInflater;
		
	}
	@Override
	public int getCount() {
	
			return users.size();
	}

	@Override
	public Object getItem(int position) {
		
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FriendRowList user=(FriendRowList)getItem(position);
		Bitmap mBitmap=user.getImage();
		if(convertView==null){
			 convertView = mInflater.inflate(R.layout.mp_f_list_enty, null);

		     holder = new FriendListHolder();
		     holder.textView = (TextView) convertView.findViewById(R.id.user_name);
		     holder.imageView=(ImageView) convertView.findViewById(R.id.imageView3);
		     convertView.setTag(holder);
		}
			holder= (FriendListHolder)convertView.getTag();
			holder.textView.setText(user.getUser().getName());
			if(mBitmap!=null)
				holder.imageView.setImageBitmap(mBitmap);
		return convertView;
	}

	public void setUsers(List<FriendRowList> users) {
		this.users=users;
		notifyDataSetChanged();
	}
	
	public List<FriendRowList> getUsers(){
		return users;
	}
	private class FriendListHolder{
		
		public TextView textView;
		public ImageView imageView;
	}
}
