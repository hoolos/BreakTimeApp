package mymaps.utils;

import org.gmarz.googleplaces.GooglePlaces;

import android.util.Log;
import android.widget.Toast;

import com.example.breaktimeapp.R;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterSingleton {
	
	private Twitter twitter=null;
	private GooglePlaces places;
	private static TwitterSingleton INSTANCE=null; 
	
	private TwitterSingleton(){
		
		twitter= new TwitterFactory().getInstance();
		places=new GooglePlaces("AIzaSyBKYN-kTWX9un6aoeGMu8WXMeZprSeqdac");
	}

	public static TwitterSingleton getInstance( ){
		if(INSTANCE==null){
			INSTANCE=new TwitterSingleton();
		}
		return INSTANCE;
	}
	public Twitter getTwitter() {
	
		return twitter;
	}
	
	public GooglePlaces getPlaces(){
		return places;
	}
	
	public void setAccessTokenAndSecret(String token,String tokenSecret){
		
		twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
		
	}
	
	public void setConsumerTokenAndSecret(String token,String tokenSecret){
		try{
			twitter.setOAuthConsumer(token, tokenSecret);
		}catch(IllegalStateException e){
			Log.d("123", e.getMessage());
		}
		
	}
	
	public void authenticate(String token,String tokenSecret) throws TwitterException{
			try{
				setAccessTokenAndSecret(token,tokenSecret);
				twitter.verifyCredentials();
			}catch(TwitterException e){
				e.printStackTrace();
				throw new TwitterException(e);
			}
		
	}


}
