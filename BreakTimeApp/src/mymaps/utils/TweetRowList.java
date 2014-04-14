package mymaps.utils;

import android.graphics.Bitmap;
import twitter4j.User;

public class TweetRowList extends FriendRowList{

	private String tweet;
	
	public TweetRowList(User user,String tweet) {
		super(user);
		this.tweet=tweet;
	}
	
	public TweetRowList(User user,Bitmap bitmap,String tweet) {
		super(user,bitmap);
		this.tweet=tweet;
	}
	
	public String getTweet(){
		return tweet;
	}

}
