package mymaps;

import java.util.List;

import mymaps.utils.CoreUtils;
import mymaps.utils.CoreUtilsImplSingleton;
import mymaps.utils.GPSinit;

import org.gmarz.googleplaces.GooglePlaces;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.content.Context;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;

/**
 * Class for making Activity's with map and gps location
 * 
 * @author Max
 * 
 */

public abstract class ActivityGPS extends FragmentActivity implements
	LocationListener, CoreUtils {

    private static CoreUtils coreUtils = CoreUtilsImplSingleton.getInstance();

    @Override
    public void authenticate(String token, String secret)
	    throws TwitterException {
	coreUtils.authenticate(token, secret);

    }

    @Override
    public Twitter getTwitter() {
	return coreUtils.getTwitter();
    }

    @Override
    public void setTwitter(Twitter twitter) {
	coreUtils.setTwitter(twitter);

    }

    @Override
    public GooglePlaces getPlaces() {

	return coreUtils.getPlaces();
    }

    @Override
    public boolean isAuthenticated() {
	return coreUtils.isAuthenticated();
    }

    public abstract GPSinit getGPSInit();

    public boolean isOnline() {
	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo = cm.getActiveNetworkInfo();
	if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	    return true;
	}
	return false;
    }

    @Override
    public <T> List<String> enumToStringArrayList(Class<T> enumType) {
	return coreUtils.enumToStringArrayList(enumType);
    }
}
