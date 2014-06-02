package mymaps.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mymaps.TweetsActivity;
import mymaps.cache.CachedListInfo;
import mymaps.cache.CachedStracture;
import mymaps.db.DatabaseHelperSQL;
import mymaps.db.columns.DirectMessagesResColumns;
import mymaps.db.dao.TweetsDao;
import mymaps.list.items.BaseListItem;
import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DirectMessagesDowloadManager extends BaseManager {

    private static final String TAG = "DM_DOWNLOAD_MANAGER";
    private final Twitter twitter;
    private final CachedStracture<TweetsDao> lruCache;
    private final int count = 1;
    private final Handler handler;
    private final TweetsDao dao;

    public DirectMessagesDowloadManager(Handler handl, TweetsDao tDao) {
	twitter = getTwitter();
	lruCache = new CachedStracture<TweetsDao>(15);
	handler = handl;
	dao = tDao;
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
		    int receivedSize = receivedMsg.size();
		    int sentSize = sentMsg.size();
		    List<BaseListItem> items;
		    if (receivedSize > sentSize) {
			items = writeMessagesToList(receivedMsg, sentMsg);
		    } else {
			items = writeMessagesToList(sentMsg, receivedMsg);
		    }

		    dao.saveAll(items);

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

    private List<BaseListItem> writeMessagesToList(List<DirectMessage> list1,
	    List<DirectMessage> list2) {
	BaseListItem item;
	Map<String, String> textRes;
	List<BaseListItem> items = new ArrayList<BaseListItem>();
	for (int i = 0; i < list1.size(); i++) {
	    item = new BaseListItem();
	    items.add(item);
	    textRes = item.getText();
	    DirectMessage rMsg = list1.get(i);
	    DirectMessage sMsg = null;
	    if (rMsg.getSenderId() == TweetsActivity.userInfo.getUser().getId()) {
		textRes.put(DatabaseHelperSQL.TEXT_RES_ID,
			String.valueOf(rMsg.getId()));
		textRes.put(DirectMessagesResColumns.Owner.toString(), "false");
		textRes.put(DirectMessagesResColumns.DM.toString(),
			rMsg.getText());
		item.setText(textRes);
		lruCache.put(item, new CachedListInfo(rMsg.getId()));
	    }
	    try {
		sMsg = list2.get(i);
		textRes.put(DatabaseHelperSQL.TEXT_RES_ID,
			String.valueOf(sMsg.getId()));
		textRes.put(DirectMessagesResColumns.Owner.toString(), "true");
		textRes.put(DirectMessagesResColumns.DM.toString(),
			sMsg.getText());
		item.setText(textRes);
		lruCache.put(item, new CachedListInfo(sMsg.getId()));
	    } catch (IndexOutOfBoundsException e) {
		Log.w(TAG, e.getMessage());
	    }

	}
	return items;
    }
}
