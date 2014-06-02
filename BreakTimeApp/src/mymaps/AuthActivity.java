package mymaps;

import mymaps.managers.AuthManager;
import mymaps.utils.TwitterStrConst;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.breaktimeapp.R;

public class AuthActivity extends BaseAppActivity {

    private Button mButton;
    private SharedPreferences sPreferences;
    private ProgressBar progressBar;
    private TextView connLostMsg;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	DisplayMetrics metrics = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(metrics);
	DISPLAY_WIDTH = metrics.widthPixels;
	DISPLAY_HEIGHTS = metrics.heightPixels;
	setContentView(R.layout.mp_auth);
	mButton = (Button) findViewById(R.id.button1);
	progressBar = (ProgressBar) findViewById(R.id.progressBar1);
	connLostMsg = (TextView) findViewById(R.id.conn_lost);
	sPreferences = getSharedPreferences(TwitterStrConst.PREFSNAME, 0);
	Uri uri = getIntent().getData();
	Handler handler = new Handler(new Handler.Callback() {

	    @Override
	    public boolean handleMessage(Message arg0) {
		if (arg0.what == AuthManager.AUTH_SUCCESSFUL) {
		    startActivity(new Intent(getApplicationContext(),
			    MainActivity.class));
		    finish();
		}
		if (arg0.what == AuthManager.START_AUTH) {
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri
			    .parse(AuthManager.getRequestToken()
				    .getAuthenticationURL()))
			    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
		return false;
	    }
	});
	authManager = new AuthManager(getApplicationContext(), handler);
	authManager.getAccesTokenAndSecret(uri);

	mButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		authManager.login();

	    }
	});

	if (isOnline()) {

	    connLostMsg.setVisibility(View.VISIBLE);
	    if (isTwitterLoggedInAlready()) {
		progressBar.setVisibility(View.VISIBLE);
		authManager.startAuthentication();
	    } else {
		mButton.setVisibility(View.VISIBLE);
	    }
	}
    }

    @Override
    protected void onResume() {
	super.onResume();
    }

    private boolean isTwitterLoggedInAlready() {

	return sPreferences.getBoolean(TwitterStrConst.PREF_KEY_TWITTER_LOGIN,
		false);
    }

}
