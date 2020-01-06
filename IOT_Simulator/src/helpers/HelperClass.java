package helpers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HelperClass {

	public static String getDeviceID() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		return Constants.Constants.BASE_DEVICE_ID+"-"+dtf.format(now);
	}
	public static long getUnixTimeStamp()
	{
		long ut1 = Instant.now().getEpochSecond();
		return ut1;
	}

}
