package mymaps.twitter.download;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.gmarz.googleplaces.GooglePlaces;
import org.gmarz.googleplaces.models.DetailsResult;
import org.gmarz.googleplaces.models.PlacesResult;



import mymaps.list.items.FriendListItem;
import mymaps.utils.ColumnsForCursors;
import mymaps.utils.FriendListAdapter;
import mymaps.utils.TwitterSingleton;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.User;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;

public class FriendListDownloadTask extends AsyncTask <Void,Void, List<FriendListItem>>{
	

	private FriendListAdapter adapter;
	private Twitter twitter;
	
	public FriendListDownloadTask(Twitter t, FriendListAdapter adapter) {
		this.adapter=adapter;
		this.twitter=t;
		// TODO Auto-generated constructor stub
	}
	@Override
	protected List<FriendListItem> doInBackground(Void... arg0) {
		PagableResponseList<User> friendsList;
		Long cursorLong=(long) -1;
		ExecutorService exec=Executors.newCachedThreadPool();
		GooglePlaces places=TwitterSingleton.getInstance().getPlaces();
		List<FriendListItem> users= new ArrayList<FriendListItem>();
		PlacesResult pl;
			do{
				try{
					friendsList=twitter.getFriendsList(twitter.getId(), cursorLong);
					cursorLong=friendsList.getNextCursor();
					for(final User r:friendsList){
						FriendListItem fl=new FriendListItem(r);
						users.add(fl);
						exec.execute(new Task1(fl));

					}
					}catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException();
					}
			}while(cursorLong!=0);
			return users;
			
	}
	
	
	@Override
	protected void onPostExecute(List<FriendListItem> users){
		try{
			adapter.setUsers(users);
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		

	};
	
	@Override
	protected synchronized void onProgressUpdate(Void... values) {
		adapter.notifyDataSetChanged();
	}
  
	 private class Task1 implements Runnable{
		  
		  private FriendListItem rl;
		  public Task1(FriendListItem rl){
			  this.rl=rl;
		  }

			@Override
			public void run() {
				try{
					URL url=new URL(rl.getUser().getBiggerProfileImageURL());
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		            connection.setDoInput(true);
		            connection.connect();
		            InputStream input = connection.getInputStream();
		            Bitmap myBitmap = BitmapFactory.decodeStream(input);
		            
		            rl.setImage(myBitmap);
		            publishProgress();
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		  
	  }
}
