package mymaps.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mymaps.TweetsActivity;
import mymaps.builders.ListItem;
import mymaps.builders.ListItemBuilder;
import mymaps.managers.TwitterDownloadManager;
import mymaps.sqldb.DBManagerForListItem;
import android.util.LruCache;

public class MessageCache<V> extends LruCache<Integer, V>implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7296177956096036574L;
	private List<Long> messagesId;
	private Integer count=-1;

	
	public MessageCache(int maxSize) {
		super(maxSize);
		messagesId=new ArrayList<Long>();
		// TODO Auto-generated constructor stub
	}
	
	public int getItemCount(){
		return count;
	}
	
	
	public void setDBManager(DBManagerForListItem dbManagerF){
		//dbManager=dbManagerF;
		
	}
	public Long getItemId(int position){
		return messagesId.get(position);
		
	}
	public  void addItemId(Long item){
		messagesId.add(item);
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
		//if(dbManager==null)
			//return itemV;
		//if( itemV==null){
			//String listItem =dbManager.getFromDB(TweetsActivity.userInfo.
					//getUser().getId(), 
					//messagesId.get(key));
			//super.put(key, (V) listItem);
			//return (V) listItem;
		//}
		return itemV;
	}

}
