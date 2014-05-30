package mymaps.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mymaps.db.DatabaseHelperSQL;
import mymaps.db.dao.BaseDao;
import mymaps.list.items.BaseListItem;
import android.util.LruCache;

public class CachedStracture<T extends BaseDao<? extends BaseListItem, ? extends DatabaseHelperSQL>>
	extends LruCache<Integer, BaseListItem> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8447209737410190351L;

    private Integer count = -1;
    private T dao;
    private final List<CachedListInfo> info;

    public CachedStracture(int maxSize) {
	super(maxSize);
	info = new ArrayList<CachedListInfo>();
	dao = null;
    }

    public int getItemCount() {
	return count;
    }

    public void setDAO(T dao) {
	this.dao = dao;

    }

    public CachedListInfo getItemId(int position) {
	return info.get(position);

    }

    public BaseListItem put(BaseListItem value, CachedListInfo listInfo) {
	count++;
	info.add(listInfo);
	return super.put(count, value);
    }

    public int getItemsCount() {
	return count;
    }

    public BaseListItem persistentGet(Integer key) {
	BaseListItem item = super.get(key);
	if (dao == null)
	    return item;
	if (item == null) {
	    BaseListItem listItem = dao.getByTextResId(info.get(key)
		    .getTextResId());

	    super.put(key, listItem);
	    return listItem;
	}
	return item;
    }

}
