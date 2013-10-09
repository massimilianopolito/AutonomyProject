package thread;

import utility.AppConstants;
import model.ThreadTracer;
import dao.ThreadTracerDao;

public abstract class AbstractThread implements Runnable {

	private boolean alive = true;
	
	public boolean isAlive() {return alive;}
	public void setAlive(boolean alive) {this.alive = alive;}

	protected abstract int getThreadType();
	
	protected void stopOnException(String msg){
		try{
			ThreadTracerDao threadTreacerDao = new ThreadTracerDao();

			ThreadTracer currentThread = new ThreadTracer(getThreadType(), AppConstants.thread.STOPPED);
			currentThread.setNote(msg);
			
			threadTreacerDao.manageCurrentThread(currentThread);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
