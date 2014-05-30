package mymaps.managers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mymaps.db.DatabaseHelperFriends;
import mymaps.list.items.BaseListItem;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DatabaseManagerTweets extends
	DatabaseManager<BaseListItem, DatabaseHelperFriends> {

    private final int batch = 3;
    private int counter = 0;

    public DatabaseManagerTweets(DatabaseHelperFriends helper) {
	dbHelper = helper;
    }

    @Override
    public void deleteDataBase() {
	dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 0, 1);
    }

    @Override
    public BaseListItem getFromDB(final Object... params) {
	final BaseListItem listItem = new BaseListItem();
	counter++;
	if (counter == batch) {
	    counter = 0;
	    dManager.callback();
	}
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		SQLiteDatabase sqlDB;
		List<String> textColumn = dbHelper.getTextColumns();
		List<String> bitmapColumns = dbHelper.getBitmapColumns();
		List<String> dbColumns = new ArrayList<String>();
		int textColumnSize = 0;
		int bitmapColumnSize = 0;
		byte[] image;
		BaseListItem item = new BaseListItem();
		try {
		    sqlDB = dbHelper.getWritableDatabase();
		    if (textColumn != null) {
			textColumnSize = textColumn.size();
			for (int i = 0; i < textColumnSize; i++) {
			    dbColumns.add(textColumn.get(i));
			}

		    }
		    if (bitmapColumns != null) {
			bitmapColumnSize = bitmapColumns.size();
			for (int i = 0; i < textColumnSize; i++) {
			    dbColumns.add(bitmapColumns.get(i));
			}
		    }
		    Cursor cursor;
		    synchronized (this) {
			cursor = sqlDB.query(
				DatabaseHelperFriends.TEXT_RES_TABLE,
				(String[]) dbColumns.toArray(),
				DatabaseHelperFriends.TEXT_RES_ID + "=?",
				new String[] { String.valueOf(params[0]) },
				null, null, null);
		    }
		    cursor.moveToFirst();
		    Map<String, Bitmap> bitmapItems = new HashMap<String, Bitmap>();
		    Map<String, String> textItems = new HashMap<String, String>();
		    for (int i = 0; i < textColumnSize; i++) {
			textItems.put(dbColumns.get(i), cursor.getString(i));
		    }
		    for (int i = textColumnSize; i < textColumnSize
			    + bitmapColumnSize; i++) {
			image = cursor.getBlob(i);
			if (image != null) {
			    Bitmap bitmap = BitmapFactory.decodeByteArray(
				    image, 0, image.length);
			    bitmapItems.put(dbColumns.get(i), bitmap);
			} else {
			    bitmapItems.put(dbColumns.get(i), null);
			}
		    }
		    item.setBitmap(bitmapItems);
		    item.setText(textItems);

		} catch (Exception e) {
		    Log.e(TAG, e.getMessage(), e);
		}
	    }

	});
	thread.start();
	return listItem;

    }

    @Override
    public void putToDB(final BaseListItem item) {
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		writeItemToDb(item);
	    }
	});
	thread.start();

    }

    @Override
    public void batchPutToDB(List<BaseListItem> batch) {
	SQLiteDatabase sqlDB;
	ByteArrayOutputStream in = null;
	ObjectOutput out = null;
	sqlDB = dbHelper.getWritableDatabase();
	String column;
	List<String> textColumns;
	List<String> bitmapColumns;
	byte[] image;
	ContentValues cv;
	sqlDB.beginTransaction();
	try {
	    in = new ByteArrayOutputStream();
	    out = new ObjectOutputStream(in);
	    for (BaseListItem item : batch) {
		cv = new ContentValues();

		textColumns = dbHelper.getTextColumns();
		bitmapColumns = dbHelper.getBitmapColumns();
		for (Entry<String, String> entry : item.getText().entrySet()) {
		    column = "";
		    try {
			String[] splitted = entry.getKey().split(":");
			if (splitted != null) {
			    column = splitted[0];
			}
			int columnIndex = textColumns.indexOf(column);
			if (columnIndex != -1) {
			    cv.put(textColumns.get(columnIndex),
				    entry.getValue());
			}

		    } catch (NullPointerException e) {
			Log.e(TAG, "Cant get column from string", e);
		    }

		}
		for (Entry<String, Bitmap> entry : item.getBitmap().entrySet()) {
		    column = "";
		    try {
			String[] splitted = entry.getKey().split(":");
			if (splitted != null) {
			    column = splitted[0];
			}
			int columnIndex = bitmapColumns.indexOf(column);
			if (columnIndex != -1) {
			    if (entry.getValue().compress(CompressFormat.PNG,
				    0, in)) {
				image = in.toByteArray();

				cv.put(bitmapColumns.get(columnIndex), image);
			    } else {
				Log.w(TAG,
					"Cant compress image "
						+ entry.getValue());
			    }
			    in.reset();
			}

		    } catch (NullPointerException e) {
			Log.e(TAG, "Cant get column from string", e);
		    }

		}
		synchronized (this) {
		    if (sqlDB.insert(DatabaseHelperFriends.TEXT_RES_TABLE,
			    null, cv) == -1) {
			Log.e(TAG, "Cant write to db");
		    }
		}
	    }
	    in.close();
	    out.close();
	    sqlDB.setTransactionSuccessful();
	    sqlDB.endTransaction();
	} catch (IOException e) {
	    Log.e(TAG, "Streams error", e);
	} catch (IllegalStateException e) {
	    Log.e(TAG, e.getMessage(), e);
	} finally {

	    try {
		if (in != null)
		    in.close();
		if (out != null)
		    out.close();
	    } catch (IOException e) {
		Log.w(TAG, "Cant close streams", e);
	    }
	}

    }

    @Override
    public void updateItem(final Object... params) {

	Thread update = new Thread(new Runnable() {

	    @Override
	    public void run() {
		SQLiteDatabase sqlDB;
		sqlDB = dbHelper.getWritableDatabase();
		byte[] image = null;
		String text = null;
		try {
		    try {
			image = (byte[]) params[2];
		    } catch (ClassCastException e) {
			Log.w(TAG, "not a byte type", e);
		    }
		    try {
			text = (String) params[2];
		    } catch (ClassCastException e) {
			Log.w(TAG, "not a string type", e);
		    }
		    ContentValues cv = new ContentValues();
		    if (image != null) {
			cv.put((String) params[1], image);
		    }
		    if (text != null) {
			cv.put((String) params[1], text);

		    }
		    synchronized (this) {

			sqlDB.update(DatabaseHelperFriends.TEXT_RES_TABLE, cv,
				DatabaseHelperFriends.TEXT_RES_ID + "=?",
				new String[] { String.valueOf(params[0]) });
		    }
		} catch (Exception e) {
		    Log.e(TAG, "cant update", e);

		}

	    }
	});
	update.start();
    }

    private void writeItemToDb(BaseListItem item) {
	SQLiteDatabase sqlDB;
	ByteArrayOutputStream in = null;
	ObjectOutput out = null;
	try {
	    sqlDB = dbHelper.getWritableDatabase();
	    String column;
	    List<String> textColumns;
	    List<String> bitmapColumns;
	    in = new ByteArrayOutputStream();
	    out = new ObjectOutputStream(in);
	    byte[] image;
	    ContentValues cv = new ContentValues();

	    textColumns = dbHelper.getTextColumns();
	    bitmapColumns = dbHelper.getBitmapColumns();
	    for (Entry<String, String> entry : item.getText().entrySet()) {
		column = "";
		try {
		    String[] splitted = entry.getKey().split(":");
		    if (splitted != null) {
			column = splitted[0];
		    }
		    int columnIndex = textColumns.indexOf(column);
		    if (columnIndex != -1) {
			cv.put(textColumns.get(columnIndex), entry.getValue());
		    }

		} catch (NullPointerException e) {
		    Log.e(TAG, "Cant get column from string", e);
		}

	    }
	    for (Entry<String, Bitmap> entry : item.getBitmap().entrySet()) {
		column = "";
		try {
		    String[] splitted = entry.getKey().split(":");
		    if (splitted != null) {
			column = splitted[0];
		    }
		    int columnIndex = bitmapColumns.indexOf(column);
		    if (columnIndex != -1) {
			if (entry.getValue()
				.compress(CompressFormat.PNG, 0, in)) {
			    image = in.toByteArray();

			    cv.put(bitmapColumns.get(columnIndex), image);
			} else {
			    Log.w(TAG,
				    "Cant compress image " + entry.getValue());
			}
			in.reset();
		    }

		} catch (NullPointerException e) {
		    Log.e(TAG, "Cant get column from string", e);
		}

	    }
	    in.close();
	    out.close();
	    synchronized (this) {
		if (sqlDB
			.insert(DatabaseHelperFriends.TEXT_RES_TABLE, null, cv) == -1) {
		    Log.e(TAG, "Cant write to db");
		}
	    }
	} catch (IOException e) {
	    Log.e(TAG, "Streams error", e);
	} finally {
	    try {
		if (in != null)
		    in.close();
		if (out != null)
		    out.close();
	    } catch (IOException e) {
		Log.w(TAG, "Cant close streams", e);
	    }
	}

    }

    public List<String> getBitmapColumns() {
	return dbHelper.getBitmapColumns();
    }

    public List<String> getTextColumns() {
	return dbHelper.getTextColumns();
    }

    @Override
    public DatabaseHelperFriends getDatabaseHelper() {
	return dbHelper;
    }
}
