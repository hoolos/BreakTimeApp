package mymaps.builders;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.concurrent.Future;

import twitter4j.Status;
import android.graphics.Bitmap;

public class TweetListItem extends ListItem {

	public TweetListItem() {
		bitmaps=new ArrayList<Bitmap>();
		bitmaps.add(null);
		refThreads=new ArrayList<SoftReference<Future<?>>>();
		refThreads.add(null);
	}
	@Override
	public synchronized void addBitmap(Bitmap bitReference) {
		bitmaps.set(0,bitReference);
		
	}

	@Override
	public synchronized void addThreadRef(SoftReference<Future<?>> thrReference) {
		refThreads.set(0,thrReference);
		
	}

	@Override
	public synchronized void addStatus(Status status) {
		this.status=status;
		
	}
	@Override
	public Bitmap getBitmap() {
		return bitmaps.get(0);
	}
	@Override
	public SoftReference<Future<?>> getThreadRef() {
		return refThreads.get(0);
	}
	@Override
	public Status getStatus() {
		
		return status;
	}
	
	

	


}
