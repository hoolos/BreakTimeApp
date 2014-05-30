package mymaps.twitter.download;

import java.util.List;

import mymaps.list.items.FriendListItem;

public interface AdapterCallbackInt {
	
	public void onDataChanged();

	public void onDataSet(List<FriendListItem> users);

}
