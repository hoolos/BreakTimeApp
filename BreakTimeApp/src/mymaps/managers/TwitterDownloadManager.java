package mymaps.managers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mymaps.BaseAppActivity;
import mymaps.TweetsActivity;
import mymaps.cache.CachedListInfo;
import mymaps.cache.CachedStracture;
import mymaps.db.DatabaseHelperFriends;
import mymaps.db.DatabaseHelperSQL;
import mymaps.db.columns.TweetsImageResColumns;
import mymaps.db.columns.TweetsTextResColumns;
import mymaps.db.dao.TweetsDao;
import mymaps.list.items.BaseListItem;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TwitterDownloadManager extends BaseManager implements
	NotifyDownloadManager {

    private static final String TAG = "TwitterDM";
    private int count = 1;
    private final Handler handler;
    private final ExecutorService exec;
    private final ExecutorService exec1;
    private Thread mainThread;
    private final CachedStracture<TweetsDao> lruCache;
    private boolean isRunning = false;
    private final TweetsDao dao;

    public TwitterDownloadManager(Handler handl, TweetsDao tweetsDao) {
	super();
	handler = handl;
	lruCache = new CachedStracture<TweetsDao>(35);
	lruCache.setDAO(tweetsDao);
	exec = Executors.newFixedThreadPool(6);
	exec1 = Executors.newFixedThreadPool(6);
	dao = tweetsDao;
    }

    public boolean isRinning() {
	return isRunning;
    }

    // Два раза качается твит
    public void start() {
	// lruCache=new CachedStructure<Long,TweetRowList>(20);
	isRunning = true;
	exec.execute(new Runnable() {

	    @Override
	    public void run() {
		try {
		    Paging page = new Paging(count++);
		    ResponseList<twitter4j.Status> tweetsList = getTwitter()
			    .getUserTimeline(
				    TweetsActivity.userInfo.getUser().getId(),
				    page);
		    Future<Void> future;
		    CachedListInfo cListInfo;
		    BaseListItem item;
		    List<Task2> tasks2 = new ArrayList<Task2>();
		    Map<String, String> textRes;
		    List<BaseListItem> items = new ArrayList<BaseListItem>();
		    for (Status status : tweetsList) {
			textRes = new HashMap<String, String>();
			item = new BaseListItem();
			items.add(item);
			cListInfo = new CachedListInfo(status.getId());
			textRes.put(DatabaseHelperSQL.TEXT_RES_ID,
				String.valueOf(status.getId()));
			textRes.put(TweetsTextResColumns.TweetText.toString(),
				status.getText());
			item.setText(textRes);
			MediaEntity[] mEntities = status.getMediaEntities();

			if (mEntities.length != 0) {

			    tasks2.add(new Task2(mEntities[0].getMediaURL(),
				    item, cListInfo));
			    // item.addRefThreads(future);
			}
			lruCache.put(item, cListInfo);
		    }

		    dao.saveAll(items);
		    exec1.invokeAll(tasks2);
		    Message msg = new Message();
		    Bundle bundle = new Bundle();
		    bundle.putSerializable("Parcel", lruCache);
		    msg.setData(bundle);
		    handler.sendMessage(msg);
		    isRunning = false;
		} catch (Exception e) {
		    e.printStackTrace();
		}

	    }
	});
    }

    public void stopIfDownloading(BaseListItem tList) {

	List<Future<?>> future = tList.getRefThreads();
	if (future != null) {
	    for (Future<?> f : future) {
		if (f.isDone() || f == null) {
		    return;
		} else {
		    f.cancel(true);
		}
	    }
	}

    }

    public void downloadTweet(Long sLong, BaseListItem tList) {
	Thread thread = new Thread(new Task1(sLong, tList));
	thread.start();

    }

    private class Task1 implements Runnable {

	private final Long statusId;
	private final BaseListItem tList;

	public Task1(Long status, BaseListItem tList) {
	    this.statusId = status;
	    this.tList = tList;
	}

	@Override
	public void run() {
	    try {

		Future<Void> future;
		Status status = getTwitter().showStatus(statusId);
		Map<String, String> textRes = tList.getText();
		textRes.put(DatabaseHelperFriends.TEXT_RES_ID.toString(),
			String.valueOf(status.getId()));
		textRes.put(TweetsTextResColumns.TweetText.toString(),
			status.getText());
		MediaEntity[] mEntities = status.getMediaEntities();

		if (mEntities.length != 0) {
		    future = exec1.submit(new Task2(mEntities[0].getMediaURL(),
			    tList, new CachedListInfo(statusId)));
		    tList.addRefThreads(future);
		}
	    } catch (TwitterException e) {
		Log.e(TAG, e.getMessage(), e);
	    }

	}

    }

    private class Task2 implements Callable<Void> {

	private final BaseListItem tList;
	private final String url;
	private final CachedListInfo listInfo;

	public Task2(String url, BaseListItem tList, CachedListInfo listInfo) {
	    this.tList = tList;
	    this.url = url;
	    this.listInfo = listInfo;

	}

	@Override
	public Void call() {
	    HttpURLConnection connection;
	    Map<String, Bitmap> bitmaps = tList.getBitmap();

	    try {

		URL urlObj = new URL(url);
		connection = (HttpURLConnection) urlObj.openConnection();
		connection.setDoInput(true);
		connection.connect();
		InputStream input = connection.getInputStream();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = calculateInSampleSize(options,
			BaseAppActivity.DISPLAY_HEIGHTS / 8,
			BaseAppActivity.DISPLAY_WIDTH / 8);
		options.inJustDecodeBounds = false;

		Bitmap myBitmap = BitmapFactory.decodeStream(input, null,
			options);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		myBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
		byte[] image = stream.toByteArray();

		bitmaps.put(TweetsImageResColumns.TweetImage.toString(),
			myBitmap);

		dao.updateBitmap(listInfo.getTextResId(),
			TweetsImageResColumns.TweetImage.toString(), image);
		handler.sendMessage(new Message());
		connection.disconnect();

	    } catch (IOException e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	    return null;
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
		int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

		final int halfHeight = height / 2;
		final int halfWidth = width / 2;

		// Calculate the largest inSampleSize value that is a power of 2
		// and keeps both
		// height and width larger than the requested height and width.
		while ((halfHeight / inSampleSize) > reqHeight
			&& (halfWidth / inSampleSize) > reqWidth) {
		    inSampleSize *= 2;
		}
	    }

	    return inSampleSize;
	}

    }

    @Override
    public void callback() {
	handler.sendMessage(new Message());
    }

}
