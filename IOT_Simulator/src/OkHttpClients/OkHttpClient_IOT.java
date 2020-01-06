package OkHttpClients;

import okhttp3.*;

import java.io.IOException;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Models.IOT_Data;
import Models.IOT_Data.Location;
import Models.Coordinates;
import Parsers.GsonParser;
import helpers.HelperClass;
import Constants.Constants;

public class OkHttpClient_IOT {

	// one instance, reuse
	private final OkHttpClient httpClient = new OkHttpClient();
	final String device_id = HelperClass.getDeviceID();

	public OkHttpClient_IOT() {
		
		
	}

	public Coordinates GetLocationCoordinates(String location) {
		try {
			HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GOOGLE_SEARCH_URL).newBuilder();
			urlBuilder.addQueryParameter("q", location+Constants.COORDINATES_QUERY);
			//urlBuilder.addQueryParameter("access_key",Constants.WEATHER_API_KEY );
			String url = urlBuilder.build().toString();
			Request request = new Request
					.Builder()
					.url(url)
					.addHeader("User-Agent", "OK-IOT_" + location)
					.build();

			Response response = httpClient.newCall(request).execute();
			

			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			System.out.println(response.body());
			String res=response.body().string();
			
			Document html = Jsoup.parse(res);
			String coordinates = html.body().getElementsByClass(Constants.HTML_CLASS).last().text();
			coordinates=coordinates.replace(" ", "");
			coordinates=coordinates.replace("°E", "");
			coordinates=coordinates.replace("°N", "");
			coordinates=coordinates.replace("°W", "");
			coordinates=coordinates.replace("°S", "");
			StringTokenizer tokens = new StringTokenizer(coordinates);
			String lat=tokens.nextToken(",");
			String lon=tokens.nextToken(",");
			Coordinates loc=new Coordinates(((int)Float.parseFloat(lat)),((int)Float.parseFloat(lon)),location);
			return loc;
			// Get response body
			//System.out.println(response.body().string());
			

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}
	
	public IOT_Data getIOTData(Coordinates location)
	{
		try
		{
		HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GOOGLE_SEARCH_URL).newBuilder();
		urlBuilder.addQueryParameter("q", location.city+Constants.WEATHER_QUERY);
		//urlBuilder.addQueryParameter("access_key",Constants.WEATHER_API_KEY );
		String url = urlBuilder.build().toString();
		Request request = new Request
				.Builder()
				.url(url)
				.addHeader("User-Agent", "OkHttp IOT" + location)
				.build();

		Response response = httpClient.newCall(request).execute();

		if (!response.isSuccessful())
			throw new IOException("Unexpected code " + response);
		String res=response.body().string();
		Document html = Jsoup.parse(res);
		String temp=html.getElementsByClass(Constants.HTML_CLASS).last().text();
		temp=temp.replace("°C","").trim();
		float temprature=Float.parseFloat(temp);
		//System.out.println(res);
		IOT_Data iot_data = new IOT_Data();
		iot_data.getData().setDeviceId(device_id);
		iot_data.getData().setTemperature(temprature);
		iot_data.getData().getLocation().setLatitude(location.latitude);
		iot_data.getData().getLocation().setLongitude(location.longitude);
		iot_data.getData().setTime(Long.toString(HelperClass.getUnixTimeStamp()));
        return iot_data;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void sendPostToKafka(String json) {
		try
		{
		// form parameters
		final MediaType JSON
	    = MediaType.parse("application/json; charset=utf-8");
		
		RequestBody formBody = RequestBody.create(JSON, json);
		Request request = new Request
				.Builder()
				.url("http://localhost:4000/api/v1/kafka_post_endpoint")
				.addHeader("User-Agent", "OkHttp IOT")
				.post(formBody)
				.build();

		try (Response response = httpClient.newCall(request).execute()) {

			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			// Get response body
			System.out.println(response.body().string());
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}