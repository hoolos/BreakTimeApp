package mymaps.db.dao;

import java.util.List;

import mymaps.db.DatabaseHelperFriends;
import mymaps.list.items.BaseListItem;
import mymaps.managers.DatabaseManager;

public class TweetsDao extends BaseDao<BaseListItem, DatabaseHelperFriends> {

    public TweetsDao(
	    DatabaseManager<BaseListItem, DatabaseHelperFriends> dbManager) {
	super(dbManager);
    }

    @Override
    public List<String> getBitmapColumns() {
	DatabaseHelperFriends dbHelper = dbManager.getDatabaseHelper();
	List<String> columns = null;
	if (dbHelper != null) {
	    columns = dbHelper.getBitmapColumns();
	}
	return columns;
    }

    @Override
    public List<String> getTextColumns() {
	DatabaseHelperFriends dbHelper = dbManager.getDatabaseHelper();
	List<String> columns = null;
	if (dbHelper != null) {
	    columns = dbHelper.getTextColumns();
	}
	return columns;
    }

}
