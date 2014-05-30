package mymaps.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DatabaseHelperFriends extends DatabaseHelperSQL {

    public static final String DATABASE_NAME = "friends.db";
    public static final int DATABASE_VERSION = 1;
    private final List<String> text;
    private final List<String> bitmap;
    private static String TEXT_RES_CREATE_SCRIPT = "create table "
	    + TEXT_RES_TABLE + " (" + BaseColumns._ID
	    + " integer primary key autoincrement, " + TEXT_RES_ID
	    + " integer not null,";

    public DatabaseHelperFriends(Context context, List<String> textColumns,
	    List<String> bitmapColumns) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	text = textColumns;
	bitmap = bitmapColumns;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	String createScript = TEXT_RES_CREATE_SCRIPT;
	for (String column : text) {
	    if (!column.isEmpty()) {
		createScript = createScript + column + " TEXT, ";
	    }
	}
	for (String column : bitmap) {
	    if (!bitmap.isEmpty()) {
		createScript = createScript + column + " BLOB, ";
	    }
	}
	createScript = createScript + ");";
	db.execSQL(createScript);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	db.execSQL("DROP TABLE IF IT EXIST " + TEXT_RES_TABLE);
	onCreate(db);

    }

    public List<String> getTextColumns() {
	return text;
    }

    public List<String> getBitmapColumns() {
	return bitmap;
    }

}
