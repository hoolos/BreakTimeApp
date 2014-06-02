package mymaps.managers;

import java.lang.ref.WeakReference;

import mymaps.utils.TwitterStrConst;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class AuthManager extends BaseManager {

    private static final String TAG = "AuthManager";

    private final WeakReference<Context> appContext;
    private static RequestToken requestToken;
    private final Handler appHandler;
    public static int AUTH_SUCCESSFUL = 200;
    public static int START_AUTH = 300;

    public AuthManager(Context context, Handler handler) {

	appHandler = handler;
	appContext = new WeakReference<Context>(context);
    }

    public void logout() {
	// TODO Auto-generated method stub

    }

    public static RequestToken getRequestToken() {
	return requestToken;
    }

    public void startAuthentication() {
	Thread authTask = new Thread(new Runnable() {

	    @Override
	    public void run() {
		Context context = appContext.get();
		if (context != null) {
		    String token = context.getSharedPreferences(
			    TwitterStrConst.PREFSNAME, 0).getString(
			    TwitterStrConst.PREF_KEY_OAUTH_TOKEN, null);
		    String secret = context.getSharedPreferences(
			    TwitterStrConst.PREFSNAME, 0).getString(
			    TwitterStrConst.PREF_KEY_OAUTH_SECRET, null);
		    try {
			authenticate(token, secret);
			appHandler.sendEmptyMessage(AUTH_SUCCESSFUL);
		    } catch (TwitterException e) {
			Toast.makeText(context, "Failed to Authenticate",
				Toast.LENGTH_LONG).show();
		    }
		}

	    };
	});
	authTask.start();
    }

    public void login() {

	Thread login = new Thread(new Runnable() {

	    @Override
	    public void run() {

		try {
		    Context context = appContext.get();
		    if (context != null) {
			requestToken = getTwitter().getOAuthRequestToken(
				TwitterStrConst.TWITTER_CALLBACK_URL);
			Log.w(TAG, requestToken.getAuthenticationURL());
			appHandler.sendEmptyMessage(START_AUTH);
		    }
		} catch (TwitterException e) {
		    Log.e(TAG, e.getMessage(), e);
		}

	    }
	});
	login.start();
    }

    public void getAccesTokenAndSecret(Uri uri) {
	if (uri != null
		&& uri.toString().startsWith(
			TwitterStrConst.TWITTER_CALLBACK_URL)) {
	    // oAuth verifier
	    final String verifier = uri
		    .getQueryParameter(TwitterStrConst.URL_TWITTER_OAUTH_VERIFIER);
	    Thread getTokenAndSecret = new Thread(new Runnable() {

		@Override
		public void run() {
		    try {
			Context context = appContext.get();
			if (context != null) {
			    AccessToken accessToken = getTwitter()
				    .getOAuthAccessToken(requestToken, verifier);
			    // Shared Preferences
			    Editor e = context.getSharedPreferences(
				    TwitterStrConst.PREFSNAME, 0).edit();

			    // After getting access token, access token secret
			    // store them in application preferences
			    e.putString(TwitterStrConst.PREF_KEY_OAUTH_TOKEN,
				    accessToken.getToken());
			    e.putString(TwitterStrConst.PREF_KEY_OAUTH_SECRET,
				    accessToken.getTokenSecret());
			    // Store login status - true
			    e.putBoolean(
				    TwitterStrConst.PREF_KEY_TWITTER_LOGIN,
				    true);
			    e.commit(); // save changes

			    Log.w(TAG, "" + accessToken.getToken());
			    getTwitter().verifyCredentials();
			    appHandler.sendEmptyMessage(AUTH_SUCCESSFUL);
			}
		    } catch (TwitterException e) {
			// Check log for login errors
			Log.d(TAG, e.getMessage(), e);
		    }

		}
	    });
	    getTokenAndSecret.start();
	}
    }
}
