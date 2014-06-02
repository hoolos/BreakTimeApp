package mymaps.db.dao;

import java.util.List;

import mymaps.db.DatabaseHelperSQL;
import mymaps.list.items.BaseListItem;
import mymaps.managers.DatabaseManager;

public abstract class BaseDao<T extends BaseListItem, S extends DatabaseHelperSQL> {

    protected final DatabaseManager<T, S> dbManager;

    public BaseDao(DatabaseManager<T, S> dbManager) {
	this.dbManager = dbManager;
    }

    public void save(T item) {
	dbManager.putToDB(item);
    }

    public void saveAll(List<T> items) {
	dbManager.batchPutToDB(items);
    }

    public T getByTextResId(Long textResId) {

	return dbManager.getFromDB(textResId);
    }

    public void updateTextColumn(Long textResId, String column, String text) {

	dbManager.updateItem(textResId, column, text);

    }

    public void updateBitmap(Long textResId, String column, byte[] image) {

	dbManager.updateItem(String.valueOf(textResId), column, image);
    }

    public abstract List<String> getBitmapColumns();

    public abstract List<String> getTextColumns();
}
