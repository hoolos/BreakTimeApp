package mymaps.builders;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.concurrent.Future;

import android.graphics.Bitmap;
import twitter4j.Status;

public  abstract class ListItem {

	protected Status status=null;
	protected List<Bitmap> bitmaps=null;
	protected List<Future<?>> refThreads=null;
	
	public abstract void addBitmap(Bitmap bitReference);
	public abstract void addThreadRef(Future<?> thrReference);
	public abstract void addStatus(Status status);
	
	public abstract Bitmap getBitmap();
	public abstract Future<?> getThreadRef();
	public abstract Status getStatus();
}
