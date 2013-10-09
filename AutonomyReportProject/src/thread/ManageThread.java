package thread;

import java.util.Iterator;
import java.util.Set;

import model.ThreadTracer;
import utility.AppConstants;
import dao.ThreadTracerDao;

public class ManageThread {
	
	private void updateThreadTracer(ThreadTracer threadTracer)throws Exception{
		ThreadTracerDao threadTracerDao = new ThreadTracerDao();
		threadTracerDao.manageCurrentThread(threadTracer);
	}
	
	public boolean searchAndStopped(int thread, String msg)throws Exception{
		String nomeThread = AppConstants.getNomiThread(thread);
		boolean ret = false;
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Iterator<Thread> i = threadSet.iterator();
		while(i.hasNext()) {
		  Thread t = i.next();
		  if(t.getName().equals(nomeThread)){
			  t.interrupt();
			  ret = true;
			  break;
		  }
		}		
		
		if(ret) stopThread(thread, msg);

		return ret;
	}

	public void stopThread(int thread, String msg) throws Exception{
		ThreadTracer threadTracer = new ThreadTracer(thread, AppConstants.thread.STOPPED);
		threadTracer.setNote(msg);
		updateThreadTracer(threadTracer);
	}
	
	public synchronized String launchThread(int threadNum, String className) throws Exception{

		String nomeThread = AppConstants.getNomiThread(threadNum);
		String esito = null;
		ThreadTracer threadTracer = new ThreadTracer(threadNum, AppConstants.thread.RUNNING);
		try{
			Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
			Iterator<Thread> iter = threadSet.iterator();
			boolean find = false;
			while(iter.hasNext()){
				String name = iter.next().getName();
				if(nomeThread.equalsIgnoreCase(name)){
					find = true;
					break;
				}
			}

			if(!find){
				Object instance = Class.forName("thread." + className).newInstance();
				Runnable runnable = (AbstractThread)instance;
				Thread thread = new Thread(runnable, nomeThread);
				thread.start();
			}
			
			threadTracer.setNote(nomeThread + ": CORRETTAMENTE AVVIATO");

			esito = "1";
		}catch (Exception e) {
			threadTracer.setStato(AppConstants.thread.FAILED);
			e.printStackTrace();
			esito = "0";
		}finally{
			updateThreadTracer(threadTracer);
		}

		return esito;
	}
}
