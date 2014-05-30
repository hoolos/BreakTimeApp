package mymaps.utils;

import java.util.ArrayList;
import java.util.List;

import org.gmarz.googleplaces.GooglePlaces;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.util.Log;

public class CoreUtilsImplSingleton implements CoreUtils {

    private static String CONSUMER_TOKEN = "aYA6Px3QI6oDvsR5EMAwkjNzj";
    private static String CONSUMER_SECRET = "0xiHsPnbNbryLGeftiip7oiqZntzH0JDXxM3s5CY2f7hyRlHUk";
    private static String PLACES_API_KEY = "AIzaSyBKYN-kTWX9un6aoeGMu8WXMeZprSeqdac";

    private static CoreUtilsImplSingleton INSTANCE = null;
    private Twitter twitter = null;
    private final GooglePlaces places;
    private AccessToken accessToken;
    private boolean isAuthenticated = false;

    private CoreUtilsImplSingleton() {

	twitter = new TwitterFactory().getInstance();
	setConsumerTokenAndSecret(CONSUMER_TOKEN, CONSUMER_SECRET);
	places = new GooglePlaces(PLACES_API_KEY);
    }

    public static CoreUtilsImplSingleton getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new CoreUtilsImplSingleton();
	}
	return INSTANCE;
    }

    private void setConsumerTokenAndSecret(String token, String tokenSecret) {
	try {
	    twitter.setOAuthConsumer(token, tokenSecret);
	} catch (IllegalStateException e) {
	    Log.d("Can't get token and secret", e.getMessage());
	}

    }

    @Override
    public void authenticate(String token, String secret)
	    throws TwitterException {
	try {
	    accessToken = new AccessToken(token, secret);
	    twitter.setOAuthAccessToken(accessToken);
	    twitter.verifyCredentials();
	    isAuthenticated = true;
	} catch (TwitterException e) {
	    e.printStackTrace();
	    throw new TwitterException(e);
	}

    }

    @Override
    public Twitter getTwitter() {
	return twitter;
    }

    @Override
    public void setTwitter(Twitter twitter) {
	this.twitter = twitter;
    }

    @Override
    public GooglePlaces getPlaces() {
	return places;
    }

    @Override
    public boolean isAuthenticated() {
	return isAuthenticated;
    }

    @Override
    public <T> List<String> enumToStringArrayList(Class<T> enumType) {
	T[] columns = enumType.getEnumConstants();
	List<String> array = null;
	if (columns != null) {
	    array = new ArrayList<String>();
	    for (int i = 0; i < columns.length; i++) {
		array.add(columns[i].toString());
	    }

	}
	return array;

    }
}
