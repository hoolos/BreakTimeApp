package mymaps;

import mymaps.cache.CachedStracture;
import mymaps.db.dao.TweetsDao;
import mymaps.managers.DirectMessagesDowloadManager;
import mymaps.utils.DirMessagesListAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.breaktimeapp.R;

public class SendDMActivity extends BaseAppActivity {

    private TextView textView;
    private ImageView imageView;
    private EditText editText;
    private DirMessagesListAdapter adapter;
    private ListView lView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mp_send_dm);
	lView = (ListView) findViewById(R.id.listView1);
	textView = (TextView) findViewById(R.id.textView1);
	imageView = (ImageView) findViewById(R.id.imageView1);
	editText = (EditText) findViewById(R.id.editText1);
	textView.setText(TweetsActivity.userInfo.getUser().getName());
	imageView.setImageBitmap(TweetsActivity.userInfo.getImage());
	// adapter=new DirMessagesListAdapter(getLayoutInflater());
	lView.setAdapter(adapter);
	Handler handler = new Handler(new Handler.Callback() {

	    @Override
	    public boolean handleMessage(Message arg0) {
		if (arg0.getData() != null) {
		    try {
			@SuppressWarnings("unchecked")
			CachedStracture<TweetsDao> cachedStructure = (CachedStracture<TweetsDao>) arg0
				.getData().getSerializable("Parcel");
			// if(cachedStructure!=null)
			// adapter.setData(sCache);(cachedStructure);
			// adapter.notifyDataSetChanged();
			return true;

		    } catch (Exception e) {
			Log.d("error", e.getMessage());
			e.printStackTrace();
		    }
		}
		return false;
	    }
	});
	DirectMessagesDowloadManager task = new DirectMessagesDowloadManager(
		handler);
	task.start();

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

}
