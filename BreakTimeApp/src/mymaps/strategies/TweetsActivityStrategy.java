package mymaps.strategies;

import mymaps.db.DatabaseHelperFriends;
import mymaps.db.columns.TweetsImageResColumns;
import mymaps.db.columns.TweetsTextResColumns;
import mymaps.db.dao.TweetsDao;
import mymaps.managers.DatabaseManagerTweets;
import mymaps.managers.TwitterDownloadManager;
import android.content.Context;
import android.os.Handler;

public class TweetsActivityStrategy extends BaseActivityStrategy {

    private final TwitterDownloadManager dManager;
    private final DatabaseManagerTweets dbManager;
    private final TweetsDao dao;

    public TweetsActivityStrategy(Context context, Handler handler) {
	dbManager = new DatabaseManagerTweets(new DatabaseHelperFriends(
		context, enumToStringArrayList(TweetsTextResColumns.class),
		enumToStringArrayList(TweetsImageResColumns.class)));
	dao = new TweetsDao(dbManager);

	dManager = new TwitterDownloadManager(handler, dao);

    }

    public void deleteDataBase() {
	dbManager.deleteDataBase();

    }

    public boolean isDowloading() {
	return dManager.isRinning();
    }

    public void downloadTweets() {
	dManager.start();

    }
}
