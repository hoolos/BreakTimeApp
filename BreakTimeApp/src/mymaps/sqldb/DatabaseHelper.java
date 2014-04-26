package mymaps.sqldb;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns{

	public static final String DATABASE_NAME = "twitter.db";
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_TABLE="tweets";
	public static final String USER_ID = "user_id";
	public static final String STATUS_BIN = "status_bin";
	public static final String IMAGE_BIN="image_bin";
	public static final String STATUS_ID="status_id";
	
	private static final String DATABASE_CREATE_SCRIPT="create table "
			+ DATABASE_TABLE + " (" + BaseColumns._ID
			+ " integer primary key autoincrement, " + USER_ID
			+ " integer not null, "+STATUS_ID+ " integer not null,"+ STATUS_BIN + " blob, " + IMAGE_BIN
			+ " blob);";
	
	
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_SCRIPT);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF IT EXIST " + DATABASE_TABLE);
		onCreate(db);
		
	}

}
