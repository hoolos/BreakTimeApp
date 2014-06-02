package mymaps.strategies;

import mymaps.db.DatabaseHelperFriends;
import mymaps.db.columns.DirectMessagesResColumns;
import mymaps.db.dao.TweetsDao;
import mymaps.managers.DatabaseManagerTweets;
import mymaps.managers.DirectMessagesDowloadManager;
import android.content.Context;
import android.os.Handler;

public class DMActivityStrategy extends BaseActivityStrategy {

    private final DirectMessagesDowloadManager dManager;
    private final DatabaseManagerTweets dbManager;
    private final TweetsDao dao;
    private final String tableName = "DM_table";

    public DMActivityStrategy(Context context, Handler handler) {
	dbManager = new DatabaseManagerTweets(new DatabaseHelperFriends(
		context, tableName,
		enumToStringArrayList(DirectMessagesResColumns.class),
		enumToStringArrayList(null)));
	dao = new TweetsDao(dbManager);
	dbManager.deleteDataBase();
	dManager = new DirectMessagesDowloadManager(handler, dao);

    }

    public void deleteDataBase() {
	dbManager.deleteDataBase();

    }

    public void downloadTweets() {
	dManager.start();

    }

}
