package utility;

import java.util.Properties;

public class PropertiesManager {
	private static PropertiesManager propertiesManager;
	private loaderPropertiesFile loadPropertiesFile;
	
	private class loaderPropertiesFile{
		private Properties properties = null;
		
		public loaderPropertiesFile() throws Exception{
			properties = new Properties();
			properties.load(this.getClass().getClassLoader().getResourceAsStream("reportAutonomy.properties"));
		}

		public Properties getProperties() {return properties;}
		
	}
	
	
	private PropertiesManager() throws Exception{
		super();
		loadPropertiesFile = new loaderPropertiesFile();
	}

	public static String getMyProperty(String propertyName) throws Exception{
		String value = null;
		if(propertiesManager==null) propertiesManager = new PropertiesManager();
		value = propertiesManager.loadPropertiesFile.getProperties().getProperty(propertyName);
		if(value!=null) value = value.trim();
		return value;
	}

	public static int getMyPropertyAsInt(String propertyName) throws Exception{
		int ret = 0;
		String value = getMyProperty(propertyName);
		if(value!=null) ret = Integer.parseInt(value);
		return ret;
	}

}
