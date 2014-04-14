package mymaps.asynctasks;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

public class TwitterAsynkTask extends AsyncTask<String, String, String>{

	@Override
	protected String doInBackground(String... arg0) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey("aYA6Px3QI6oDvsR5EMAwkjNzj");
        builder.setOAuthConsumerSecret("0xiHsPnbNbryLGeftiip7oiqZntzH0JDXxM3s5CY2f7hyRlHUk");
        twitter4j.conf.Configuration configuration= builder.build();
         
        TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();
        try {
            RequestToken requestToken = twitter
                    .getOAuthRequestToken();
        } catch (TwitterException e) {
            e.printStackTrace(System.out);
        }
		return null;
	}

}
