package mymaps;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.examples.oauth.GetAccessToken;

import com.example.breaktimeapp.R;

import mymaps.asynctasks.TwitterAsynkTask;
import mymaps.utils.AbstractAppActivity;
import mymaps.utils.TwitterSingleton;
import mymaps.utils.TwitterStrConst;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AuthActivity extends AbstractAppActivity{
	
	private static RequestToken  requestToken;
	
	private Button mButton;
	private TwitterSingleton twitterSingleton;
	private SharedPreferences sPreferences;
	private ProgressBar progressBar;
	private TextView connLostMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        twitterSingleton = TwitterSingleton.getInstance();
	        setContentView(R.layout.mp_auth);
	        mButton=(Button) findViewById(R.id.button1);
	        progressBar=(ProgressBar) findViewById(R.id.progressBar1);
	        connLostMsg=(TextView) findViewById(R.id.conn_lost);
	        sPreferences=getSharedPreferences(TwitterStrConst.PREFSNAME,0);
	        Uri uri = getIntent().getData();
	        if (uri != null && uri.toString().startsWith(TwitterStrConst.TWITTER_CALLBACK_URL)) {
	            // oAuth verifier
	            final String verifier = uri
	                    .getQueryParameter(TwitterStrConst.URL_TWITTER_OAUTH_VERIFIER);
	            Thread getTokenAndSecret =new Thread(new Runnable() {
					
					@Override
					public void run() {
						getAccessTokenAndSecret(verifier);
						
					}
				});
	            getTokenAndSecret.start();
	        }
	    
	        final Thread login=new Thread(new Runnable() {
				
				@Override
				public void run() {
					loginToTwitter();
					
				}
			});
	        
	        Thread authTask= new Thread(new Runnable() {
				
				@Override
				public void run() {
					String token=getApplicationContext().getSharedPreferences(TwitterStrConst.PREFSNAME
							, 0).getString(TwitterStrConst.PREF_KEY_OAUTH_TOKEN, null);
					String secret=getApplicationContext().getSharedPreferences(TwitterStrConst.PREFSNAME
							, 0).getString(TwitterStrConst.PREF_KEY_OAUTH_SECRET, null);
					try {
						twitterSingleton.authenticate(token,secret);
						startActivity(new Intent(getApplicationContext(), MainActivity.class));
						finish();
					} catch (TwitterException e) {
						Toast.makeText(getApplicationContext(), "Failed to Authenticate", Toast.LENGTH_LONG).show();
					}

					
				};
	        });
	        mButton.setOnClickListener(new OnClickListener() {
				
	        	@Override
				public void onClick(View arg0) {
					login.start();
					
				}
			});
	        
	        if(isOnline()){
	        	
	        	connLostMsg.setVisibility(View.VISIBLE);
		        if(isTwitterLoggedInAlready()){
		        	progressBar.setVisibility(View.VISIBLE);
		        	authTask.start();
		        }
		        else {
		        	mButton.setVisibility(View.VISIBLE);
				}
	        }
	}
	
	@Override
    protected void onResume() {
        super.onResume();
    }
	
	public void loginToTwitter(){
		
        Twitter twitter = twitterSingleton.getTwitter();
        try {
	            requestToken = twitter
	                    .getOAuthRequestToken(TwitterStrConst.TWITTER_CALLBACK_URL);
	            Log.d("123",requestToken.getAuthenticationURL());
	            getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse(requestToken.getAuthenticationURL())).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (TwitterException e) {
            e.printStackTrace();
        }
	}
	
	public void getAccessTokenAndSecret(String verifier){
		try{
			AccessToken accessToken = twitterSingleton.getTwitter().getOAuthAccessToken(
	                requestToken, verifier);
	
	        // Shared Preferences
	        Editor e = sPreferences.edit();
	
	        // After getting access token, access token secret
	        // store them in application preferences
	        e.putString(TwitterStrConst.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
	        e.putString(TwitterStrConst.PREF_KEY_OAUTH_SECRET,
	                accessToken.getTokenSecret());
	        // Store login status - true
	        e.putBoolean(TwitterStrConst.PREF_KEY_TWITTER_LOGIN, true);
	        e.commit(); // save changes
	
	        Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
	        twitterSingleton.getTwitter().verifyCredentials();
	    } catch (TwitterException e) {
	        // Check log for login errors
	    	e.printStackTrace();
	        Log.d("123", "> " + e.getMessage());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	Log.d("123", "> " + e.getMessage());
	    }
	}
	
	private boolean isTwitterLoggedInAlready() {
		 twitterSingleton.setConsumerTokenAndSecret(getResources().getString(R.string.consumerToken), 
					getResources().getString(R.string.consumerSecret));
        // return twitter login status from Shared Preferences
        return sPreferences.getBoolean(TwitterStrConst.PREF_KEY_TWITTER_LOGIN, false);
    }  
}
