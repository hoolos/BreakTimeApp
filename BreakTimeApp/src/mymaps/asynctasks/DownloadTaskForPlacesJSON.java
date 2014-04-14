package mymaps.asynctasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;







import mymaps.ActivityGPS;
import mymaps.utils.GPSinit;
import android.os.AsyncTask;
import android.util.Log;



class DownloadTaskForPlacesJSON extends AbstractDownloadJsonTask{

	GPSinit gpsInit;
    public DownloadTaskForPlacesJSON(GPSinit gpsInit){
    	super();
    	this.gpsInit=gpsInit;
    }
    // Executed after the complete execution of doInBackground() method
	@Override
	protected void onPostExecute(String result) {
		
		//ParserTaskForPlaces parser=new ParserTaskForPlaces(gpsInit);
		
	}
    
    
}
