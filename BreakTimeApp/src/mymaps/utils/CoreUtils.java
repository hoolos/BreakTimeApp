package mymaps.utils;

import java.util.List;

import org.gmarz.googleplaces.GooglePlaces;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public interface CoreUtils {

    public void authenticate(String token, String secret)
	    throws TwitterException;

    public Twitter getTwitter();

    public void setTwitter(Twitter twitter);

    public GooglePlaces getPlaces();

    public boolean isAuthenticated();

    public <T> List<String> enumToStringArrayList(Class<T> enumType);

    public User getAuthenticatedUser();
}
