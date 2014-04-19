package mymaps.builders;

import java.lang.ref.SoftReference;
import java.util.concurrent.Future;

import twitter4j.Status;
import android.graphics.Bitmap;

public class TweetListItemBuilder extends ListItemBuilder{

	public TweetListItemBuilder() {
		
		lItem=new TweetListItem();
	}
	@Override
	public void setStatus(Status status) {
		lItem.addStatus(status);
		
	}

	@Override
	public void setThreadRef(SoftReference<Future<?>> thread) {
		lItem.addThreadRef(thread);
		
	}

	@Override
	public void setBitmap(Bitmap bitmap) {
		lItem.addBitmap(bitmap);
		
	}
	@Override
	public ListItem buildListItem() {
		ListItem item=lItem;
		lItem=new TweetListItem();
		return item;
	}
	

}
