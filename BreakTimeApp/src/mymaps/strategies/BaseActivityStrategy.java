package mymaps.strategies;

import java.util.List;

import mymaps.utils.CoreUtils;
import mymaps.utils.CoreUtilsImplSingleton;

import org.gmarz.googleplaces.GooglePlaces;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public abstract class BaseActivityStrategy implements CoreUtils {

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

    @Override
    public <T> List<String> enumToStringArrayList(Class<T> enumType) {
	return coreUtils.enumToStringArrayList(enumType);
    }

}
