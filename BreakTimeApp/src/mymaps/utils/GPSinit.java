package mymaps.utils;

import twitter4j.Location;
import mymaps.ActivityGPS;
import android.content.Context;
import android.location.LocationManager;

import com.example.breaktimeapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GPSinit{
	public GoogleMap fMap;
	public LocationManager locationManager;
	private ActivityGPS gpsActivity;
	private static final long MIN_TIME = 400; 
	private static final float MIN_DISTANCE = 10;
	public GPSinit(){}
	public GPSinit(ActivityGPS gpsActivity){
		this.gpsActivity=gpsActivity;
	}
	public void setUpMap(){
		  if (fMap == null) {
	            // Try to obtain the map from the MapFragment.
	            fMap = ((MapFragment) gpsActivity.getFragmentManager().findFragmentById(R.id.fragment1))
	                    .getMap();
	            // Check if we were successful in obtaining the map.
	            if (fMap != null) {
	            	fMap.addMarker(new MarkerOptions().position(
	            			new LatLng(56.834325,60.600532)).title("Marker"));
	            }
	        }
	}
	public boolean setUpLocation() {
		if(locationManager==null)
    		locationManager=(LocationManager)gpsActivity.getSystemService(Context.LOCATION_SERVICE);
    	if(locationManager!=null)
			if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
	    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
	    				MIN_TIME, MIN_DISTANCE, gpsActivity);
	    	else
	    		if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
	    			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
	    					MIN_TIME, MIN_DISTANCE, gpsActivity);
	    		else
	    			return false;
    	return true;
		
		
		
	}
	
	public void addMarker(Location location){
		try{
			fMap.addMarker(new MarkerOptions().position(
        			new LatLng(1,1)).title("Marker"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
