package mymaps.managers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import mymaps.TweetsActivity;
import mymaps.utils.MessageCache;
import mymaps.utils.TwitterSingleton;
import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class DirectMessagesDowloadTask {

	private Twitter twitter;
	private MessageCache<String> lruCache;
	private int count=1;
	private Handler handler;
	public DirectMessagesDowloadTask(Handler handl){
		twitter=TwitterSingleton.getInstance().getTwitter();
		lruCache=new MessageCache<String>(15);
		handler=handl;
	}
	
	public void start(){
		Thread thread =new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					ResponseList<DirectMessage> message=twitter.getDirectMessages();
					for(DirectMessage msg:message){
						if(msg.getSenderId()==
								TweetsActivity.userInfo.getUser().getId()){
							lruCache.addItemId(msg.getId());
							lruCache.put(msg.getText());
						}
						TweetsActivity.userInfo.getUser().getName();
					}
					Message msg=new Message();
					Bundle bundle=new Bundle();
					bundle.putSerializable("Parcel", lruCache);
					msg.setData(bundle);
					handler.sendMessage(msg);
					
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		thread.start();
	}
}
