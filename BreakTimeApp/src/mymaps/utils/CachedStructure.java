package mymaps.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.LruCache;

public class CachedStructure<S,V> extends LruCache<Integer, V>{

	protected List<S> items;
	private Integer count;
	
	public CachedStructure(int maxSize) {
		super(maxSize);
		items=new ArrayList<S>();
	}
	
	public S getItemId(int position){
		return items.get(position);
		
	}
	public  void addItemId(S item){
		items.add(item);
	}
	public V put(V value){
		return super.put(count++, value);
	}
	public int getItemsCount(){
		return count;
	}
}
