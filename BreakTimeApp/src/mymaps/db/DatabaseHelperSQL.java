package mymaps.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public abstract class DatabaseHelperSQL extends SQLiteOpenHelper implements
	BaseColumns {
    protected static final String TAG = "DATABASE_HELPER";

    public static final String TEXT_RES_TABLE = "text_res";
    public static final String TEXT_RES_ID = "text_res_id";

    public DatabaseHelperSQL(Context context, String name,
	    CursorFactory factory, int version,
	    DatabaseErrorHandler errorHandler) {
	super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelperSQL(Context context, String name,
	    CursorFactory factory, int version) {
	super(context, name, factory, version);
    }

}
