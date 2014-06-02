package mymaps.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DatabaseHelperFriends extends DatabaseHelperSQL {

    public static final String DATABASE_NAME = "friends.db";
    public static final int DATABASE_VERSION = 1;
    private final List<String> text;
    private final List<String> bitmap;

    private final String createScript;

    public DatabaseHelperFriends(Context context, String tableName,
	    List<String> textColumns, List<String> bitmapColumns) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION, tableName);
	text = new ArrayList<String>();
	bitmap = new ArrayList<String>();
	text.add(TEXT_RES_ID);
	text.addAll(textColumns);
	bitmap.addAll(bitmapColumns);
	createScript = "create table " + getTable() + " (" + BaseColumns._ID
		+ " integer primary key autoincrement, " + TEXT_RES_ID
		+ " integer not null,";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	String script = createScript;
	for (int i = 1; i < text.size(); i++) {
	    String column = text.get(i);
	    if (!column.isEmpty()) {
		script = script + column + " TEXT, ";
	    }
	}
	for (String column : bitmap) {
	    if (!bitmap.isEmpty()) {
		script = script + column + " BLOB, ";
	    }
	}
	script = script.substring(0, script.lastIndexOf(","));
	script = script + ");";
	db.execSQL(script);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	db.execSQL("DROP TABLE IF EXISTS " + getTable());
	onCreate(db);

    }

    public List<String> getTextColumns() {
	return text;
    }

    public List<String> getBitmapColumns() {
	return bitmap;
    }

}
