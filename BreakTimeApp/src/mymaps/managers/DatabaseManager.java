package mymaps.managers;

import java.util.List;

import mymaps.db.DatabaseHelperSQL;

public abstract class DatabaseManager<V, DB extends DatabaseHelperSQL> {

    protected DB dbHelper;
    protected static final String TAG = "DB_MANAGER";
    protected NotifyDownloadManager dManager;

    public abstract void deleteDataBase();

    public abstract DB getDatabaseHelper();

    public abstract V getFromDB(Object... params);

    public abstract void putToDB(final V item);

    public abstract void batchPutToDB(List<V> batch);

    public abstract void updateItem(Object... params);

    public void setNotifyDownloadManager(NotifyDownloadManager dManager) {

	this.dManager = dManager;
    }

}
