package mymaps.cache;

import java.util.HashMap;
import java.util.Map;

public class CachedListInfo {

    private final Map<String, String> bitmapInfo;
    private final Long textResId;

    public CachedListInfo(Long id) {
	bitmapInfo = new HashMap<String, String>();
	textResId = id;
    }

    public void addBitmapInfo(String column, String res) {
	bitmapInfo.put(column, res);
    }

    public String getBitmapInfo(String column) {
	return bitmapInfo.get(column);
    }

    public Long getTextResId() {
	return textResId;
    }

}
