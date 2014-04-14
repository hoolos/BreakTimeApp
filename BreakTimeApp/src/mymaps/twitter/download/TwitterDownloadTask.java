package mymaps.twitter.download;

import mymaps.utils.FriendListAdapter;
import twitter4j.Twitter;
import android.os.AsyncTask;


public abstract class TwitterDownloadTask<Params, Progress, Result> 
	extends AsyncTask<Params, Progress, Result>{

	protected Twitter twitter;
	
	public TwitterDownloadTask (Twitter t){
		
		this.twitter=t;
	}
}
