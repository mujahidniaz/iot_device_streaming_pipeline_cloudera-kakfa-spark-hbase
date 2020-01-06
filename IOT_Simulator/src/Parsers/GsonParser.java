package Parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Models.IOT_Data;

public class GsonParser {

	public static String SerializeIOT_Data_ToJson(IOT_Data iot_data) {
		final GsonBuilder builder = new GsonBuilder();
		Gson gson;
		builder.setPrettyPrinting();
		gson = builder.create();
		String jsonString = gson.toJson(iot_data);
		return jsonString;
	}

}
