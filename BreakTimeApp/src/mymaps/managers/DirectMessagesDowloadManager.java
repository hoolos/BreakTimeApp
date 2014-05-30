package mymaps.managers;

import java.util.Map;

import mymaps.TweetsActivity;
import mymaps.cache.CachedListInfo;
import mymaps.cache.CachedStracture;
import mymaps.db.columns.DirectMessagesResColumns;
import mymaps.db.dao.TweetsDao;
import mymaps.list.items.BaseListItem;
import mymaps.utils.TwitterSingleton;
import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DirectMessagesDowloadManager extends BaseManager {

    private final Twitter twitter;
    private final CachedStracture<TweetsDao> lruCache;
    private final int count = 1;
    private final Handler handler;

    public DirectMessagesDowloadManager(Handler handl) {
	twitter = TwitterSingleton.getInstance().getTwitter();
	lruCache = new CachedStracture<TweetsDao>(15);
	handler = handl;
    }

    public void start() {
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    ResponseList<DirectMessage> receivedMsg = twitter
			    .getDirectMessages();
		    ResponseList<DirectMessage> sentMsg = twitter
			    .getSentDirectMessages();
		    BaseListItem item;
		    Map<String, String> textRes;
		    for (DirectMessage msg : receivedMsg) {
			item = new BaseListItem();
			textRes = item.getText();
			textRes.put(DirectMessagesResColumns.DMID.toString(),
				String.valueOf(msg.getId()));
			if (msg.getSenderId() == TweetsActivity.userInfo
				.getUser().getId()) {
			    textRes.put(
				    DirectMessagesResColumns.Owner.toString(),
				    "123");
			    textRes.put(DirectMessagesResColumns.DM.toString(),
				    msg.getText());
			}

			lruCache.put(item, new CachedListInfo(msg.getId()));

		    }

		    Message msg = new Message();
		    Bundle bundle = new Bundle();
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
