package mymaps.asynctasks;


import org.json.JSONObject;


import android.os.AsyncTask;
import android.util.Log;

public abstract class AbstractParser<T extends String,S,F> extends AsyncTask<T,S,F> {

	private JSONObject jObject;
	private JSONParser<F> parser;

	public AbstractParser(JSONParser<F> parser){
		super();
		this.parser=parser;
	}
	@Override
	protected F doInBackground(T... jsonData) {
		
		F data = null;
		
        try{
        	jObject = new JSONObject(jsonData[0]);
        	
            /** Getting the parsed data as a an ArrayList */
            data = parser.getParsedDataFromJson(jObject);
            
        }catch(Exception e){
                Log.d("Exception",e.toString());
        }
        return data;
	}
	
	public JSONParser<F> getParser() {
		return parser;
	}
	
	public JSONObject getjObject() {
		return jObject;
	}
	
}
