package mymaps;




import java.util.List;
import java.util.concurrent.Executor;
import twitter4j.Twitter;
import twitter4j.User;
import mymaps.twitter.download.AdapterCallbackInt;
import mymaps.twitter.download.FriendListDownloadTask;
import mymaps.utils.FriendListAdapter;
import mymaps.utils.FriendRowList;
import mymaps.utils.GPSinit;
import mymaps.utils.TwitterSingleton;

import com.example.breaktimeapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;


import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView; 
import android.widget.Toast;


@SuppressLint("ShowToast")
public class MainActivity extends  ActivityGPS {
	 private GPSinit gpsInit=new GPSinit(this);
	 private Button mBtnFind;
	 private EditText mFplace;
	 private Twitter twitter;
	 private FriendListAdapter adapter;
	
	 
	/**
	 * MIN_TIME and MIN_DISTANCE are constants for location updates
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_bs2);
        twitter=TwitterSingleton.getInstance().getTwitter();
        adapter=new FriendListAdapter(null, getLayoutInflater());
        ListView parent = (ListView)findViewById(R.id.left_sms);
        parent.setAdapter(adapter);
        FriendListDownloadTask f=new FriendListDownloadTask(twitter,adapter);
        f.execute();
        gpsInit.setUpMap();
        
        //mBtnFind=(Button)findViewById(R.id.button1);
       // mFplace=(EditText)findViewById(R.id.editText1);
        /*mBtnFind.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				String location =mFplace.getText().toString();
				if(location==null||location==""){
					Toast.makeText(getBaseContext(), 
							getString(R.string.warning_msg2), Toast.LENGTH_LONG);
					return;
				}
				String map_url = "https://maps.googleapis.com/maps/api/geocode/json?"; 
				 try {
				     // encoding special characters like space in the user input place
				     location = URLEncoder.encode(location, "utf-8");
				 } 
				 catch (UnsupportedEncodingException e){
					 e.printStackTrace();
				 }
				    String address = "address=" + location;
				    String sensor = "sensor=false";
				     
				     
				    // url , from where the geocoding data is fetched
				    map_url = map_url + address + "&" + sensor;
				   // String modifiedURL= url.toString().replace(" ", "%20");
				 
				    // Instantiating DownloadTask to get places from Google Geocoding service
				    // in a non-ui thread
				    DownloadTask downloadTask = new DownloadTask(MyActivity.this);
				     
				    // Start downloading the geocoding places
				    downloadTask.execute(map_url);
				     
			}
        	
        });*/
        if(!gpsInit.setUpLocation()){
	    	Toast.makeText(this, 
	    			getString(R.string.warning_msg1), Toast.LENGTH_LONG);
		}
        else{
        	//CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(fastGPSloc(), 10);
           // gpsInit.fMap.animateCamera(cameraUpdate);
        }
        parent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FriendListAdapter fAdapter=(FriendListAdapter) arg0.getAdapter();
				FriendRowList row=(FriendRowList) fAdapter.getItem(arg2);
				Intent intent=new Intent(getApplicationContext(), TweetsActivity.class);
				Bundle bundle=new Bundle();
				bundle.putParcelable("User", row);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
    }
    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        gpsInit.setUpMap();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
    }

    public GPSinit getGPSInit(){
    	return gpsInit;
    }
    private void removeUpdates(){
    	gpsInit.locationManager.removeUpdates(this);
    }
    private LatLng fastGPSloc(){
    	List<String> providers=null;
    	try{
    		providers=gpsInit.locationManager.getAllProviders();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	Location l=null;
    	for (int i=providers.size()-1; i>=0; i--) {
    		  l = gpsInit.locationManager.getLastKnownLocation(providers.get(i));
    		  if (l != null) 
    			  break;
    		 }
    	return new LatLng(l.getLatitude(),l.getLongitude());
    }
    @Override
    public void onLocationChanged(Location location){
    	LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
    	CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng, 10);
    	gpsInit.fMap.animateCamera(cameraUpdate);
    	//locationManager.removeUpdates(this);
    }
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	

	
	/**
	 * Class for initialization of GPS of any ActivityGPS 
	 * @author Max
	 *
	 */

}

