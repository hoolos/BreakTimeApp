package mymaps.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import twitter4j.examples.oauth.GetAccessToken;
import mymaps.builders.ListItem;
import mymaps.builders.ListItemBuilder;
import mymaps.builders.TweetListItem;
import mymaps.builders.TweetListItemBuilder;
import mymaps.managers.TwitterDownloadManager;
import android.R.integer;
import android.util.LruCache;

public class CachedStructure<V extends ListItem> extends LruCache<Integer, V> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5404504176353327263L;
	
	protected List<Long> items;
	private TwitterDownloadManager<ListItemBuilder> dManager;
	private Integer count=-1;
	
	public int getItemCount(){
		return count;
	}
	public CachedStructure(int maxSize) {
		super(maxSize);
		dManager=null;
		items=new ArrayList<Long>();
	}
	
	public void setDownloadManager(TwitterDownloadManager<ListItemBuilder> manager){
		dManager=manager;
	}
	public Long getItemId(int position){
		return items.get(position);
		
	}
	public  void addItemId(Long item){
		items.add(item);
		count++;
	}
	
	public V put(V value){
		return super.put(count, value);
	}
	public int getItemsCount(){
		return count;
	}
	
	public V persistentGet(Integer key){
		V itemV= super.get(key);
		if(dManager==null)
			return itemV;
		if( itemV==null){
			ListItem listItem =new TweetListItem();
			dManager.downloadTweet(items.get(key), listItem);
			super.put(key, (V) listItem);
			return (V) listItem;
		}
		return itemV;
	}
}
