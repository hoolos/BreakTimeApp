package mymaps.asynctasks;

import org.json.JSONObject;

public interface JSONParser<T> {
	
	public T getParsedDataFromJson(JSONObject jObject);

}
