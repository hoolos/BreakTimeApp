package mymaps.asynctasks;

import java.util.HashMap;
import java.util.List;

import mymaps.ActivityGPS;
import mymaps.utils.GPSinit;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

class ParserTaskForPlaces extends AbstractParser<String, Integer, List<HashMap<String,String>>>{
	JSONObject jObject;
	private GPSinit gpsInit;

	public ParserTaskForPlaces(JSONParser<List<HashMap<String, String>>> parser,GPSinit gpsInit){
		super(parser);
		this.gpsInit=gpsInit;
	}
	
	// Executed after the complete execution of doInBackground() method
	@Override
	protected void onPostExecute(List<HashMap<String,String>> list){			
		
		try{
			// Clears all the existing markers			
			gpsInit.fMap.clear();
			
			for(int i=0;i<list.size();i++){
			
				// Creating a marker
	            MarkerOptions markerOptions = new MarkerOptions();
	            HashMap<String, String> hmPlace = list.get(i);
	
	            double lat = Double.parseDouble(hmPlace.get("lat"));	            
	            double lng = Double.parseDouble(hmPlace.get("lng"));
	           
	            String name = hmPlace.get("formatted_address");
	            LatLng latLng = new LatLng(lat, lng);
	            markerOptions.position(latLng);
	            markerOptions.title(name);
	
	            gpsInit.fMap.addMarker(markerOptions);    
	            
	            // Locate the first location
	            if(i==0)
	            	gpsInit.fMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	            }
		}catch(Exception e){
			Log.d("Exception", e.toString());
		}
		}
	
}
