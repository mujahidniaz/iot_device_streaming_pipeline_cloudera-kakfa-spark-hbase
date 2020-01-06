package Models;

public class IOT_Data {
	Data data;

	// Getter Methods

	public Data getData() {
		return data;
	}

	// Setter Methods

	public void setData(Data dataObject) {
		this.data= dataObject;
	}
	
	public IOT_Data()
	{
		this.data=new Data();
	}

	public class Data {
		private String deviceId;
		private int temperature;
		Location location;
		private String time;

		// Getter Methods
		
		public Data()
		{
			this.location=new Location();
		}
		public String getDeviceId() {
			return deviceId;
		}

		public float getTemperature() {
			return temperature;
		}

		public Location getLocation() {
			return location;
		}

		public String getTime() {
			return time;
		}

		// Setter Methods

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}

		public void setTemperature(float temperature) {
			this.temperature = (int)temperature;
		}
		public void setTemperature(int temperature) {
			this.temperature = temperature;
		}

		public void setLocation(Location locationObject) {
			this.location = locationObject;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}

	public class Location {
		private int latitude;
		private int longitude;

		// Getter Methods
		
		public Location()
		{
			
		}
		
		public Location(int lat,int lon)
		{
			this.latitude=lat;
			this.longitude=lon;
		}

		

		public int getLongitude() {
			return longitude;
		}

		// Setter Methods

		public void setLatitude(int latitude) {
			this.latitude = latitude;
		}

		public void setLongitude(int longitude) {
			this.longitude = longitude;
		}
	}
}
