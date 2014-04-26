package mymaps.builders;

import java.lang.ref.SoftReference;
import java.util.concurrent.Future;

import android.graphics.Bitmap;
import twitter4j.Status;

public abstract class ListItemBuilder {
	
	protected ListItem lItem;
	
	public abstract void setStatus(Status status);
	public abstract void  setThreadRef(Future<?> thread);
	public abstract void setBitmap(Bitmap bitmap);
	public abstract ListItem buildListItem();
}