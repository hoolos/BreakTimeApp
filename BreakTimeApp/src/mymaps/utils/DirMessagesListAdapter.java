package mymaps.utils;

import java.util.Map;

import mymaps.cache.CachedStracture;
import mymaps.db.columns.DirectMessagesResColumns;
import mymaps.db.dao.TweetsDao;
import mymaps.list.items.BaseListItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.breaktimeapp.R;

public class DirMessagesListAdapter extends BaseAdapter {

    private CachedStracture<TweetsDao> msgCache;
    private final LayoutInflater lInflater;

    public DirMessagesListAdapter(LayoutInflater inflater) {
	msgCache = new CachedStracture<TweetsDao>(15);
	lInflater = inflater;
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
	BaseListItem msg = (BaseListItem) getItem(arg0);
	if (arg1 == null) {
	    arg1 = lInflater.inflate(R.layout.mp_dir_message_entry, null);
	    Handler handler = new Handler();
	    handler.friendMessage = (TextView) arg1
		    .findViewById(R.id.textView1);
	    handler.userMessage = (TextView) arg1.findViewById(R.id.textView2);
	    arg1.setTag(handler);
	}
	Handler handler = (Handler) arg1.getTag();
	Map<String, String> text = msg.getText();
	if (text.get(DirectMessagesResColumns.Owner.toString()) == "true") {
	    handler.userMessage.setText(text.get(DirectMessagesResColumns.DM
		    .toString()));
	} else {
	    handler.friendMessage.setText(text.get(DirectMessagesResColumns.DM
		    .toString()));
	}

	return arg1;

    }

    private class Handler {
	public TextView friendMessage;
	public TextView userMessage;
    }

    public void setData(CachedStracture<TweetsDao> sCache) {
	msgCache = sCache;
	notifyDataSetChanged();
    }

}
