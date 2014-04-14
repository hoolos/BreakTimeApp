package mymaps.twitter.download;

import java.util.List;

import mymaps.utils.FriendRowList;

public interface AdapterCallbackInt {
	
	public void onDataChanged();

	public void onDataSet(List<FriendRowList> users);

}
