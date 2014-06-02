package mymaps;

import java.util.List;

import mymaps.utils.CoreUtils;
import mymaps.utils.CoreUtilsImplSingleton;

import org.gmarz.googleplaces.GooglePlaces;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class BaseAppActivity extends Activity implements CoreUtils {

    private static CoreUtils coreUtils = CoreUtilsImplSingleton.getInstance();

    public static int DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHTS;

    public boolean isOnline() {
	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo = cm.getActiveNetworkInfo();
	if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	    return true;
	}
	return false;
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
	    int reqWidth, int reqHeight) {
	// Raw height and width of image
	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;

	if (height > reqHeight || width > reqWidth) {

	    final int halfHeight = height / 2;
	    final int halfWidth = width / 2;

	    // Calculate the largest inSampleSize value that is a power of 2 and
	    // keeps both
	    // height and width larger than the requested height and width.
	    while ((halfHeight / inSampleSize) > reqHeight
		    && (halfWidth / inSampleSize) > reqWidth) {
		inSampleSize *= 2;
	    }
	}

	return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	    int reqWidth, int reqHeight) {

	// First decode with inJustDecodeBounds=true to check dimensions
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeResource(res, resId, options);

	// Calculate inSampleSize
	options.inSampleSize = calculateInSampleSize(options, reqWidth,
		reqHeight);

	// Decode bitmap with inSampleSize set
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeResource(res, resId, options);
    }

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

    @Override
    public <T> List<String> enumToStringArrayList(Class<T> enumType) {
	return coreUtils.enumToStringArrayList(enumType);
    }

    @Override
    public User getAuthenticatedUser() {

	return coreUtils.getAuthenticatedUser();
    }

}
