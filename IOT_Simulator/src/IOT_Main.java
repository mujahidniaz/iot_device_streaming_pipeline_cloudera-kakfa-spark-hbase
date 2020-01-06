import OkHttpClients.*;
import Parsers.GsonParser;
import helpers.HelperClass;

import java.util.Scanner;

import Models.Coordinates;
import Models.IOT_Data.Location;
import Models.IOT_Data;

public class IOT_Main {

	private static Scanner input;

	public static void main(String[] args) {

		String location="";
		input = new Scanner(System.in);
		System.out.print("Please Enter the Location of Device (City Name) for Simulation: ");
		location =input.nextLine();
		
		
		OkHttpClient_IOT obj = new OkHttpClient_IOT();
	    Coordinates loc = obj.GetLocationCoordinates(location); 
		IOT_Data iot_data=obj.getIOTData(loc);
		int i=1;
		while(true)
		{
			iot_data.getData().setTime(Long.toString(HelperClass.getUnixTimeStamp()));
			String IOT_Json = GsonParser.SerializeIOT_Data_ToJson(iot_data);
			obj.sendPostToKafka(IOT_Json);
			System.out.println(i);
		    System.out.println(IOT_Json);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
			
			if(i%108000==0)
			{
				//iot_data=obj.getIOTData(loc); // update after 30 minutes
			}
		}	
			
		
	}

}
