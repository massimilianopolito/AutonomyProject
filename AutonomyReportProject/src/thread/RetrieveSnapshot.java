package thread;

import java.io.File;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.OccorrenzePallografoObject;
import model.SnapShot;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;

import Autonomy.D2Map;
import dao.OccorrenzePallografoDao;
import dao.SnapShotDao;
import utility.AppConstants;
import utility.ConnectionManager;
import utility.DateConverter;
import utility.PropertiesManager;
import utility.ReportLogger;

public class RetrieveSnapshot extends AbstractThread {
	private Logger logger = ReportLogger.getLog("snapShot");

	@Override
	protected int getThreadType() {
		return AppConstants.thread.SNAPSHOT;
	}
	
/*	private void makeOccorrenze(String fileName) throws Exception{
		String nomeSnapShot = fileName.substring(0, fileName.indexOf("-")-2);
		OccorrenzePallografoObject occorrenzePallografoObject = new OccorrenzePallografoObject();
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection =  cm.getConnection(true);
		try{
			OccorrenzePallografoDao occorrenzePallografoDao = new OccorrenzePallografoDao(connection);
			D2Map d2Map = new D2Map();
			String[] tokens = nomeSnapShot.split("_");//Contiene 2 token per CORPORATE, 3 per CONSUMER.
			
			 * Il primo TOKEN si riferisce a FISSO/MOBILE
			 * il secondo TOKEN a CASE/INTERAZIONI
			 * IL terso TOKEN a CORPORATE/CONSUMER
			 * Il corporate non ha il primo token.
			 
			
			int i = 0;
			if(tokens.length==2){
				occorrenzePallografoObject.setTicket(null);
			}else{
				occorrenzePallografoObject.setTicket(AppConstants.getIndexFromLabel(tokens[i]));
				i = i+1;
			}
			
			occorrenzePallografoObject.setTipo(AppConstants.getIndexFromLabel(tokens[i]));
			occorrenzePallografoObject.setArea(AppConstants.getIndexFromLabel(tokens[i+1]));
			
			occorrenzePallografoObject.setData(DateConverter.getDateRolled(System.currentTimeMillis(), -2));
			if("max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){
				occorrenzePallografoObject.setOccorrenze(Long.parseLong("10111970"));
			}else{
				String dataInizio = DateConverter.getDate(occorrenzePallografoObject.getData(), DateConverter.PATTERN_VIEW);
				String num = d2Map.getNumTotHitsPall(occorrenzePallografoObject.getArea(), 
													 occorrenzePallografoObject.getTicket(), 
													 occorrenzePallografoObject.getTipo(), 
													 dataInizio, dataInizio);
				occorrenzePallografoObject.setOccorrenze(Long.parseLong(num));
			}
			
			if(!occorrenzePallografoDao.isExistData(occorrenzePallografoObject)) occorrenzePallografoDao.insertOccorrenze(occorrenzePallografoObject);
		}catch (Exception e) {
			//cm.rollBack(connection);
			e.printStackTrace();
			throw e;
		}finally{
			cm.closeConnection(connection);
		}
	}
*/	
	private void makeRecord(List<String> lines, String nomeFile) throws Exception{
		if(lines!=null && !lines.isEmpty()){
			ConnectionManager cm = ConnectionManager.getInstance();
			Connection connection =  cm.getConnection(true);
			SnapShotDao snapShotDao = new SnapShotDao(connection);
			try{
				for(String currentRow: lines){
					if(currentRow.startsWith("#")) continue;
					String[] tokens = currentRow.split("\t");
					String autonomyDate = tokens[0];
					String data = DateConverter.getDate(autonomyDate);
					String nomeSnapshot = tokens[1];
					String numeroOrdine = tokens[2];
					String idLegame = tokens[5];
					String nomeCluster = tokens[7];
					String numDoc = tokens[8];
					String key = tokens[9];
					
					//String uniqueKey = data+"|"+numeroOrdine+"|"+nomeCluster+"|"+key;
					//if(alreadyParsed.contains(uniqueKey)) continue;
					//alreadyParsed.add(uniqueKey);
					SnapShot currentSnapShot = new SnapShot();
					currentSnapShot.setDate(DateConverter.getDate(data, DateConverter.PATTERN_VIEW));
					currentSnapShot.setAutonomyDate(autonomyDate);
					currentSnapShot.setClusterName(nomeCluster);
					currentSnapShot.setKey(Integer.parseInt(key));
					currentSnapShot.setNumDoc(Long.parseLong(numDoc));
					currentSnapShot.setOrder(Integer.parseInt(numeroOrdine));
					currentSnapShot.setSnapShot(nomeSnapshot);
					currentSnapShot.setNomeFile(nomeFile);
					currentSnapShot.setIdLegame(Integer.parseInt(idLegame));
					
					snapShotDao.manageDocumentBatch(currentSnapShot);
					
				}

				snapShotDao.executeBatchByUpdate();
			}catch (Exception e) {
				//cm.rollBack(connection);
				e.printStackTrace();
				throw e;
			}finally{
				cm.closeConnection(connection);
			}
			
		}
	}
	
	private void truncate() throws Exception{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection =  cm.getConnection(false);
		SnapShotDao snapShotDao = new SnapShotDao(connection);
		try{
			snapShotDao.truncateTable();
			cm.commit(connection);
		}catch (Exception e) {
			cm.rollBack(connection);
			e.printStackTrace();
			throw e;
		}finally{
			cm.closeConnection(connection);
		}
	}
	
	private boolean isLastFile(String fileName) throws Exception{
		boolean isLast = false;
		if("max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))) return true;
		for(int i=0; i>-2; i--){
			Calendar referenceDate = GregorianCalendar.getInstance();
			referenceDate.add(Calendar.DATE, i);
			
			String autonomyDateFromFile = fileName.substring(fileName.indexOf("-")+1, fileName.indexOf("."));
			String humanDateFromFile = DateConverter.getDate(autonomyDateFromFile);
			String referenceDateString = DateConverter.getDate(new Timestamp(referenceDate.getTimeInMillis()), DateConverter.PATTERN_VIEW);
			logger.debug("Data nel file: " + humanDateFromFile);
			logger.debug("Data di confronto: " + referenceDateString);
			if(humanDateFromFile.equals(referenceDateString)){
				logger.debug(fileName + " corrisponde alla data: " + referenceDateString);
				isLast = true;
				break;
			}else{
				logger.debug(fileName + " NON corrisponde alla data: " + referenceDateString);
			}
		}
		return isLast;
	}
	
	private void reader()  throws Exception{
		String snapshotDir = PropertiesManager.getMyProperty("snapshot.thread.pathFiles");
		File containderDir = new File(snapshotDir);
		List<File> files = (List<File>) FileUtils.listFiles(containderDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		if(files.size()!=0)  truncate();
		for (File file : files) {
			logger.debug("file: " + file.getCanonicalPath());
			if(file.getName().endsWith("SGD")){
				if(isLastFile(file.getName())){
					List<String> lines = FileUtils.readLines(file, "UTF-8");
					try{
						makeRecord(lines, file.getName());
						if(!"max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))) FileUtils.deleteQuietly(file);
						//makeOccorrenze(file.getName());
					}catch(Exception e){
						logger.debug("Si e' verificato un errore nella lavorazione del file: " + file.getName());
					}
				}
			}
		}
		logger.debug("---------- FINE ELABORAZIONE -------------------");
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
					reader();
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
						reader();
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
