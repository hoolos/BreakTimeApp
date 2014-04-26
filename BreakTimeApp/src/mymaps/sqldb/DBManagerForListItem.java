package mymaps.sqldb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import com.google.android.gms.internal.in;

import twitter4j.Status;
import mymaps.builders.ListItem;
import mymaps.builders.TweetListItem;
import mymaps.managers.NotifyDownloadManager;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DBManagerForListItem {

	private DatabaseHelper dbHelper;
	private WeakReference<Context> contextReference;
	private NotifyDownloadManager callback;
	
	public DBManagerForListItem(Context context){
		contextReference=new WeakReference<Context>(context);
		dbHelper=new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, 
				null, DatabaseHelper.DATABASE_VERSION);
	}
	
	public void setNottifyDownloadManager(NotifyDownloadManager callManager){
		callback=callManager;
	}
	
	public void deleteDataBase(){
		contextReference.get().deleteDatabase(DatabaseHelper.DATABASE_NAME);
	}
	public ListItem getFromDB(final Long user_id, final Long status_id){
		final ListItem listItem=new TweetListItem();
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				SQLiteDatabase sqlDB;
 				ByteArrayInputStream in = null;
				ObjectInputStream is = null;
				try{
					sqlDB=dbHelper.getWritableDatabase();
					Cursor cursor = sqlDB.query(DatabaseHelper.DATABASE_TABLE, new String[]{DatabaseHelper.STATUS_BIN,DatabaseHelper.IMAGE_BIN},
							DatabaseHelper.STATUS_ID+"=?", 
							new String[]{String.valueOf(status_id)}, null, null,null);
					cursor.moveToFirst();
					Status status;
						in = new ByteArrayInputStream(cursor.getBlob(0));
						is = new ObjectInputStream(in);
						status=(Status) is.readObject();
					listItem.addStatus(status);
					byte[] image=cursor.getBlob(1);
					if(image!=null){
						Bitmap bitmap=BitmapFactory.decodeByteArray(image, 0, image.length);
						listItem.addBitmap(bitmap);
					}else {
						listItem.addBitmap(null);
					}
					listItem.addThreadRef(null);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try {
						if(in!=null)in.close();
						if(is!=null)is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(callback!=null)
					callback.callback();
			}

		});
		thread.start();
		return listItem;
	
	}
	
	public void putToDB(final ListItem litem){
		Thread thread =new Thread(new Runnable() {
			
			@Override
			public void run() {
				SQLiteDatabase sqlDB;
				ByteArrayOutputStream in = null;
				ObjectOutput out = null;
				try{
					sqlDB=dbHelper.getWritableDatabase();
					Status status=litem.getStatus();
					in = new ByteArrayOutputStream();
					out = new ObjectOutputStream(in);
					out.writeObject(litem.getStatus());
					byte[] dbstatus=in.toByteArray();
					litem.getBitmap().compress(CompressFormat.PNG,0, in);
					byte[] image=in.toByteArray();
					ContentValues cv = new ContentValues();
					cv.put(DatabaseHelper.USER_ID, status.getUser().getId());
					cv.put(DatabaseHelper.STATUS_ID, status.getId());
					cv.put(DatabaseHelper.STATUS_BIN, dbstatus);
					in.close();
					out.close();
					if(litem.getBitmap()!=null)
						cv.put(DatabaseHelper.IMAGE_BIN, image);
					if(sqlDB.insert(DatabaseHelper.DATABASE_TABLE,
							null, cv)==-1)
						throw new Exception();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try {
						if(in!=null)in.close();
						if(out!=null)out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});
		thread.start();
		
	}
	
	public void bulkPutToDB(List<Status> bulk){
		SQLiteDatabase sqlDB;
		ByteArrayOutputStream in = null;
		ObjectOutput out = null;
		sqlDB=dbHelper.getWritableDatabase();
		sqlDB.beginTransaction();
		try{
			for(Status status:bulk){
				in = new ByteArrayOutputStream();
				out = new ObjectOutputStream(in);
				out.writeObject(status);
				byte[] dbstatus=in.toByteArray();
				in.flush();
				ContentValues cv = new ContentValues();
				cv.put(DatabaseHelper.USER_ID, status.getUser().getId());
				cv.put(DatabaseHelper.STATUS_ID, status.getId());
				cv.put(DatabaseHelper.STATUS_BIN, dbstatus);
				in.close();
				out.close();
				if(sqlDB.insert(DatabaseHelper.DATABASE_TABLE,
						null, cv)==-1)
					throw new Exception();
			}
			sqlDB.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(in!=null)in.close();
				if(out!=null)out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sqlDB.endTransaction();
	}
	
	public void updateBitmap(Long statusId,Bitmap bitmap){
		
		SQLiteDatabase sqlDB;
		ByteArrayOutputStream in = null;
		sqlDB=dbHelper.getWritableDatabase();
		try {
			in = new ByteArrayOutputStream();
			if(bitmap.compress(CompressFormat.PNG,0, in));
				Log.d("good", "yes!");
			byte[] image=in.toByteArray();
			ContentValues cv = new ContentValues();
			cv.put(DatabaseHelper.IMAGE_BIN, image);
			sqlDB.update(DatabaseHelper.DATABASE_TABLE, cv, 
					DatabaseHelper.STATUS_ID+"=?", new String[]{String.valueOf(statusId)});
		} catch (Exception e) {
			Log.d("good", "No!");
			e.printStackTrace();
		}
	}
}
