package mymaps.asynctasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public abstract class AbstractDownloadJsonTask extends AsyncTask<String, Integer, String>{
	
	private String data = null;
	
	public AbstractDownloadJsonTask(){
		
	}
	private String downloadUrl(String strUrl) throws IOException{
	        String data = "";
	        InputStream iStream = null;
	        HttpURLConnection urlConnection = null;
	        try{
	        	URL url = new URL(strUrl);


	            // Creating an http connection to communicate with url 
	            urlConnection = (HttpURLConnection) url.openConnection();

	            // Connecting to url 
	            urlConnection.connect();

	            // Reading data from url 
	            iStream = urlConnection.getInputStream();

	            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

	            StringBuffer sb  = new StringBuffer();

	            String line = "";
	            while( ( line = br.readLine())  != null){
	            	sb.append(line);
	            }

	            data = sb.toString();

	            br.close();

	        	}
	        catch(Exception e){
	        	Log.d("Exception while downloading url", e.toString());
	        }
	        finally{
	            iStream.close();
	            urlConnection.disconnect();
	        }

	        return data;
	        
		}
	    @Override
	    protected String doInBackground(String... url) {
	            try{                    		
	                    data = downloadUrl(url[0]);
	            }catch(Exception e){
	                     Log.d("Background Task",e.toString());
	            }
	            return data;
	    }
	    
	    protected String getData(){
	    	
	    	return data;
	    }
	    
	    // Executed after the complete execution of doInBackground() method
	        @Override
	        protected abstract void onPostExecute(String result);
	        	

}
