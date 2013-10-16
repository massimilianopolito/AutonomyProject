package utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import thread.ManageThread;

public class AppServletContextListener implements ServletContextListener {
	private ManageThread gestisciThread = new ManageThread();
	private Logger logger = ReportLogger.getLog("general");


	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try{
			logger.debug("Arresto i thread...");
			
			String msg = "Servizio interrotto per arresto server";
			gestisciThread.stopThread(AppConstants.thread.STRUTTURA, msg);
			gestisciThread.stopThread(AppConstants.thread.HOT_TOPICS, msg);
			
			logger.debug("...Thread arrestati!");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try{
			logger.debug("Avvio i thread...");
			gestisciThread.launchThread(AppConstants.thread.STRUTTURA, "StrutturaXmlSender");
			gestisciThread.launchThread(AppConstants.thread.HOT_TOPICS, "HotTopicsQueryMaker");
			logger.debug("...Thread avviati!");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
