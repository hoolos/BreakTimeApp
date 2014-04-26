package mymaps.utils;

import com.example.breaktimeapp.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DirMessagesListAdapter extends BaseAdapter {

	private MessageCache<String> msgCache;
	private LayoutInflater lInflater;
	public DirMessagesListAdapter(LayoutInflater inflater){
		msgCache=new MessageCache<String>(1);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return msgCache.getItemCount();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return msgCache.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		String msg=(String) getItem(arg0);
		if(arg1==null){
			arg1=lInflater.inflate(R.layout.mp_dir_message_entry,null);
			Handler handler=new Handler();
			handler.message=(TextView) arg1.findViewById(R.id.textView1);
			arg1.setTag(handler);
		}
		Handler handler=(Handler) arg1.getTag();
		handler.message.setText(msg);
		return arg1;
		
		
	}
	
	private class Handler{
		public TextView message;
	}
	
	public void setData(MessageCache<String> sCache){
		msgCache=sCache;
		notifyDataSetChanged();
	}

}
