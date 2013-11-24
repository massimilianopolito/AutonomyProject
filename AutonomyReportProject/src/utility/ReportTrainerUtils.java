package utility;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import model.InboxCreazione;

import org.apache.log4j.Logger;

import dao.InboxCreazioneDao;

@SuppressWarnings("serial")
public class ReportTrainerUtils implements Serializable {
	private Logger logger = ReportLogger.getLog("ReportTrainerUtils");
	private static Map<String, String> inboxCreazioneFisso = new LinkedHashMap<String,String>();
	private static Map<String, String> inboxCreazioneMobile = new LinkedHashMap<String, String>();

	private static String[] mercati = {AppConstants.TipoTicket.FISSO, AppConstants.TipoTicket.MOBILE};

	private void load(String mercatoCorrente) throws Exception{
		logger.debug("START - LOAD");
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = cm.getConnection(true);
		InboxCreazioneDao inboxCreazioneDao = new InboxCreazioneDao(connection);
		try{
			logger.debug("LOAD - In corso.... per: " + mercatoCorrente);
			InboxCreazione inboxCreazione = new InboxCreazione();
			inboxCreazione.setMercato(mercatoCorrente);
			Collection<InboxCreazione> values = inboxCreazioneDao.getValues(inboxCreazione);
			if(values!=null){
				for(InboxCreazione currentInbox: values){
					getMapByMercato(mercatoCorrente).put(currentInbox.getID(), currentInbox.getDescrizione());
				}
			}
			logger.debug("LOAD - ....Terminato");
		}catch(Exception e){
			throw e;
		}finally{
			cm.closeConnection(connection);
		}

		logger.debug("END - LOAD");
	}

	public static Map<String, String> getMapByMercato(String mercato){
		Map<String, String> currentMap = null;
		if(AppConstants.TipoTicket.FISSO.equals(mercato)){
			currentMap = inboxCreazioneFisso;
		}else{
			currentMap = inboxCreazioneMobile;
		}
		return currentMap;
	}
	
	public static String getDescriptionByKey(String mercato, String ID) throws Exception{
		String value = null;
		value = getMapByMercato(mercato).get(ID);
		return value;
	}

	public static void clear() throws Exception{
		for(int i=0; i<mercati.length; i++){
			String mercatoCorrente = mercati[i];
			getMapByMercato(mercatoCorrente).clear();
		}
		
	}
	
	public static void load() throws Exception{
		ReportTrainerUtils reportTrainerUtils = new ReportTrainerUtils();
		for(int i=0; i<mercati.length; i++){
			String mercatoCorrente = mercati[i];
			Map<String, String> currentMap = getMapByMercato(mercatoCorrente);
			if(currentMap.isEmpty()){
				reportTrainerUtils.load(mercatoCorrente);
			}
		}
	}
	
}
