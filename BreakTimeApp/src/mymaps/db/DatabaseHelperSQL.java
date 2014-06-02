package mymaps.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public abstract class DatabaseHelperSQL extends SQLiteOpenHelper implements
	BaseColumns {
    protected static final String TAG = "DATABASE_HELPER";

    public static final String DATABASE_NAME = "tweets.db";
    public static final String TEXT_RES_ID = "text_res_id";
    private final String textResTable;

    public DatabaseHelperSQL(Context context, String name,
	    CursorFactory factory, int version,
	    DatabaseErrorHandler errorHandler, String table) {
	super(context, name, factory, version, errorHandler);
	textResTable = table;
    }

    public DatabaseHelperSQL(Context context, String name,
	    CursorFactory factory, int version, String table) {
	super(context, name, factory, version);
	textResTable = table;
    }

    public String getTable() {
	return textResTable;
    }

}
