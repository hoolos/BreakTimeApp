package mymaps.list.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import android.graphics.Bitmap;

public class BaseListItem {
    private Map<String, Bitmap> bitmap;
    private Map<String, String> text;
    private final List<Future<?>> refThreads;
    private int threadId = 0;

    public BaseListItem() {
	bitmap = new HashMap<String, Bitmap>();
	text = new HashMap<String, String>();
	refThreads = new ArrayList<Future<?>>();
    }

    public Map<String, Bitmap> getBitmap() {
	return bitmap;
    }

    public void setBitmap(Map<String, Bitmap> bitmap) {
	this.bitmap = bitmap;
    }

    public Map<String, String> getText() {
	return text;
    }

    public void setText(Map<String, String> text) {
	this.text = text;
    }

    public List<Future<?>> getRefThreads() {
	return refThreads;
    }

    public void addRefThreads(Future<?> refThreads) {
	this.refThreads.add(refThreads);
	threadId++;
    }

}
