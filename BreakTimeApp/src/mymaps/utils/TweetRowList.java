package mymaps.utils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.concurrent.Future;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.graphics.Bitmap;
import twitter4j.Status;
import twitter4j.User;

public class TweetRowList {

	private WeakReference<Future<Void>> refThread;
	private WeakReference<Bitmap> bitmap;
	private WeakReference<Status> status;
	
 	public WeakReference<Bitmap> getBitmap() {
		if(bitmap==null){
			return new WeakReference<Bitmap>(null);
		}
 		return bitmap;
	}

	public void setBitmap(WeakReference<Bitmap> bitmap) {
		this.bitmap = bitmap;
	}



	
	public WeakReference<Status> getStatus() {
		return status;
	}

	public void setStatus(WeakReference<Status> status) {
		this.status = status;
	}



	
	public TweetRowList(WeakReference<Status> statusReference) {
		this.status=statusReference;
	}
	public TweetRowList() {
	}
	public WeakReference<Future<Void>> getRefThread() {
		return refThread;
	}



	public void setRefThread(WeakReference<Future<Void>> refThread) {
		this.refThread = refThread;
	}







}
