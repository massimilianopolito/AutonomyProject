package utility;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ReportLogger extends Logger {
	private static ReportLogger thisInstance = null;

	private ReportLogger(String name) {
		super(name);
		try{
			Properties properties = new Properties();
			properties.load(this.getClass().getClassLoader().getResourceAsStream("reportAutonomyLog.properties"));
			PropertyConfigurator.configure(properties);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static Logger getLog(String baseName){
		if(thisInstance==null) thisInstance = new ReportLogger("INITIALIZE");
		return Logger.getLogger(baseName);
	}
}
