package utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import thread.ManageThread;

public class AppServletContextListener implements ServletContextListener {
	private ManageThread gestisciThread = new ManageThread();


	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try{
			System.out.println("Arresto i thread...");
			
			String msg = "Servizio interrotto per arresto server";
			gestisciThread.stopThread(AppConstants.thread.STRUTTURA, msg);
			gestisciThread.stopThread(AppConstants.thread.HOT_TOPICS, msg);
			
			System.out.println("...Thread arrestati!");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try{
			System.out.println("Avvio i thread...");
			gestisciThread.launchThread(AppConstants.thread.STRUTTURA, "StrutturaXmlSender");
			gestisciThread.launchThread(AppConstants.thread.HOT_TOPICS, "HotTopicsQueryMaker");
			System.out.println("...Thread avviati!");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
