package mymaps;

import mymaps.utils.GPSinit;
import android.content.Context;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;

/**
 * Class for making Activity's with map and gps location
 * @author Max
 *
 */

public abstract class ActivityGPS extends FragmentActivity implements LocationListener{
	public abstract GPSinit getGPSInit();

	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
