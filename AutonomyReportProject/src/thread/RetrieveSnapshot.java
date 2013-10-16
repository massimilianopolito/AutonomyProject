package thread;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import utility.AppConstants;
import utility.PropertiesManager;
import utility.ReportLogger;

public class RetrieveSnapshot extends AbstractThread {
	private Logger logger = ReportLogger.getLog("snapShot");

	@Override
	protected int getThreadType() {
		return AppConstants.thread.SNAPSHOT;
	}

	@Override
	public void run() {
		logger.debug("START: " + this.getClass().getName());
		try{
			Calendar currentDate = GregorianCalendar.getInstance();
			Calendar compareDate = GregorianCalendar.getInstance();
			
			while(true && isAlive()){
				String hours = PropertiesManager.getMyProperty("snapshot.thread.oraEsecuzioni");
				if("99:99".equalsIgnoreCase(hours)){

					break;
				}
				String[] executionHours = hours.split("\\|");
				for(int i=0; i<executionHours.length; i++){
					String currentHour = executionHours[i];
					String[] tokens = currentHour.split("\\:");
					int hour = Integer.parseInt(tokens[0].trim());
					int min = Integer.parseInt(tokens[1].trim());

					currentDate.setTimeInMillis(System.currentTimeMillis());
					compareDate.set(compareDate.get(Calendar.YEAR), compareDate.get(Calendar.MONTH), compareDate.get(Calendar.DATE), hour, min);

					//logger.debug("-------------------------------------------------");
					//logger.debug("currentDate.getTime(): " + currentDate.getTime());
					//logger.debug("compareDate.getTime(): " + compareDate.getTime());
					
					long sleep = compareDate.getTimeInMillis() - currentDate.getTimeInMillis();
					if(sleep>=0){
						try{
							Thread.sleep(sleep);
						}catch (Exception e) {
							// TODO: handle exception
						}

						//METODO
					}
					
					if(i==executionHours.length-1){
						compareDate.roll(Calendar.DATE, 1);
					}
				}
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			stopOnException(e.getMessage());
			//LOG
		}
	}

}
