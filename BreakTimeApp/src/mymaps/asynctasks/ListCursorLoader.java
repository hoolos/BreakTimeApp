package mymaps.asynctasks;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;

public class ListCursorLoader extends AsyncTaskLoader<MatrixCursor>  {

	public ListCursorLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MatrixCursor loadInBackground() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
