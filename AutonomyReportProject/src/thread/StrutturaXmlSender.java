package thread;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import model.DatiQuery;
import model.LogThreadStruttura;
import model.QueryObject;
import utility.AppConstants;
import utility.ConnectionManager;
import utility.PropertiesManager;
import Autonomy.D2Map;
import Autonomy.DocumentoQueryTO;
import dao.LogThreadStrutturaDao;
import dao.QueryDatiDao;
import dao.QuerySalvateDao;
import dao.StrutturaDao;

public class StrutturaXmlSender extends AbstractThread {

	@Override
	protected int getThreadType() {
		return AppConstants.thread.STRUTTURA;
	}
	
	private List<String> getDates(String from, String to){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<String> dates = new ArrayList<String>();
		try{
			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			
			if(to!=null && !to.isEmpty()){
				end.setTime(sdf.parse(to));
			}

			if(from!=null && !from.isEmpty()){
				start.setTime(sdf.parse(from));
			}else{
				start.setTime(end.getTime());
				String months = PropertiesManager.getMyProperty("penthao.thread.month");
				int monthInt = 3;
				try{
					monthInt = Integer.parseInt(months);
				}catch(Exception e){
					
				}
				start.add(Calendar.MONTH, -monthInt);
			}
			
			start.set(Calendar.HOUR_OF_DAY, 0);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);
			 
			end.set(Calendar.HOUR_OF_DAY, 0);
			end.set(Calendar.MINUTE, 0);
			end.set(Calendar.SECOND, 0);
			end.set(Calendar.MILLISECOND, 0);
			
			//Splitto l'intervallo di date nei singoli giorni che lo compongono.
			dates.add(sdf.format(start.getTime()));
			while(start.before(end)){
				start.add(Calendar.DATE, 1);
				dates.add(sdf.format(start.getTime()));
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return dates;
	}
	
	private HashMap<String, Object> getChiaveValore(QueryObject queryObject) throws Exception{
		//HashMap<String, String> chiaveValore = new  HashMap<String, String>();
		HashMap<String, Object> chiaveValore = new HashMap<String, Object>();
		QueryDatiDao queryDatiDao = new QueryDatiDao();
		Collection<DatiQuery> fieldValues = queryDatiDao.getQueryPublic(queryObject);

		String tipoTicket = queryObject.getTicket();
		String dateStart = null;
		String dateEnd = null;

		if(fieldValues!=null){
			for(DatiQuery currentData: fieldValues){
				String nomeCampo = currentData.getIdCampo();
				String currentValue = currentData.getValoreCampo();
				String nomeColonna = null;
				if("first".equalsIgnoreCase(nomeCampo)) nomeColonna = "MOTIVO";
				else if("second".equalsIgnoreCase(nomeCampo)) nomeColonna = "ARGOMENTO";
				else if("third".equalsIgnoreCase(nomeCampo)) nomeColonna = "SPECIFICA";

				if(nomeColonna!=null){
					if(AppConstants.categoriaTicket.CASE.equalsIgnoreCase(tipoTicket)){
						nomeColonna = nomeColonna + "_TRIPLETTA";
					}
					chiaveValore.put(nomeColonna, currentValue);
				}else{
					if (nomeCampo.equalsIgnoreCase("DATA_CREAZIONE_DA")){
						dateStart = currentValue;
					}else if(nomeCampo.equalsIgnoreCase("DATA_CREAZIONE_A")){
						dateEnd = currentValue;
					}
				}
				
				
				
/*				String idCampo = currentData.getIdCampo();
				
				ConfigurazioneQueryDao configurazioneQueryDao = new ConfigurazioneQueryDao();
				ConfigurationObject configurationObject = new ConfigurationObject();
				configurationObject.setID(idCampo);
				configurationObject = configurazioneQueryDao.getSingleConfig(configurationObject);
				String nomeColonna = configurationObject.getNomeColonna();
				//aggiunto ora da marco di legge
				String currentValue = currentData.getValoreCampo();
				String tipoCampo = configurationObject.getTipoCampo();
				if(AppConstants.tipoDiCampo.CALENDAR.equalsIgnoreCase(tipoCampo)){
					
					String dateName = nomeColonna.replace("_DA", "").replace("_A", "");
					String[] dateValues = {".", "."};
					if((String[])chiaveValore.get(dateName)!=null){
						dateValues = (String[])chiaveValore.get(dateName);
					}
					if(currentValue!=null && currentValue.trim().length()!=0){
						if(nomeColonna.endsWith("_DA")){
							dateValues[0] = currentValue;
							}else if(nomeColonna.endsWith("_A")){
								dateValues[1] = currentValue;
							}
					}
					chiaveValore.put(dateName, dateValues);

				}else if(currentValue!=null && currentValue.trim().length()!=0 && !"--".equalsIgnoreCase(currentValue)){
					System.out.println("Aggiungo: " + nomeColonna + " - " + currentValue);
					chiaveValore.put(nomeColonna, currentValue);
				}

*/				//String valore = currentData.getValoreCampo();
				//chiaveValore.put(nomeColonna, valore);
			}
			
			/**
			 * Alla tabella aggiungo sempre una lista di date associate a DATA_CREAZIONE,
			 * se le date DA e A sono valorizzate nella query, scompongo l'intervallo indicato in una lista composta dai singoli giorni,
			 * in caso contrario (ovvero se almeno uno dei due estremi mancasse) indico la data di partenza uguale a quella corrente e considero
			 * in ogni caso un intervallo dell'ampiezza di un mese.
			 */
			
		}
		chiaveValore.put("DATA_CREAZIONE", getDates(dateStart, dateEnd));
		
		return chiaveValore;
	}
	
	private String createXml(QueryObject queryObject) throws Exception{
		String esito = null;
		String userName = queryObject.getNomeUtente().replace(".", "");
		Collection<DocumentoQueryTO> result = null;
		String tipoTicket = queryObject.getTicket();
		String categoriaTicket = queryObject.getTipo();
		String ambito = queryObject.getArea();
		String nomeQuery = queryObject.getNomeQuery();

		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, ambito);
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
		String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket).toUpperCase();
		
		//String numRis = queryObject.getNumRisultati();
		//String numRis = "1000000";
		//String numRis = "500000";
		String numRis = "100000";
		String relevance = queryObject.getRelevance();
		String testo = queryObject.getTesto();
		if(testo==null || testo.trim().length()==0) testo="";
		testo = testo.trim();
		
		HashMap<String, Object> chiaveValore = getChiaveValore(queryObject);
		String table = null;
		if(root.equalsIgnoreCase("Consumer"))
		{
			if(ticket.equalsIgnoreCase("FISSO"))
			{
				if(tipo.equalsIgnoreCase("INTERAZIONI"))
					table = "autonomy_int_fisso";
				else
					table = "autonomy_case_fisso";
					
			}
			else
			{
				if(tipo.equalsIgnoreCase("INTERAZIONI"))
					table = "autonomy_int_mobile";
				else
					table = "autonomy_case_mobile";
			}
		}
		else
		{
			if(ticket.equalsIgnoreCase("FISSO"))
			{
				if(tipo.equalsIgnoreCase("INTERAZIONI"))
					table = "IntFissoCorporate";
				else
					table = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equalsIgnoreCase("INTERAZIONI"))
					table = "IntMobileCorporate";
				else
					table = "CaseMobileCorporate";
			}
		}
		
		ConnectionManager connectionManager = ConnectionManager.getInstance();
		DataSource ds = connectionManager.getDataSource();
		Connection connection = ds.getConnection();
		
		connection.setAutoCommit(false);
		String results = "1";
		D2Map d2Map = new D2Map();
		if(tipo.equalsIgnoreCase("CASE"))
			results = d2Map.BuildQueryStructCaseTest(root, ticket, tipo, chiaveValore, numRis, relevance, testo, userName, nomeQuery, connection, categoriaTicket, table);
		else
			results = d2Map.BuildQueryStructIntTest(root, ticket, tipo, chiaveValore, numRis, relevance, testo, userName, nomeQuery, connection, categoriaTicket, table);
		
		System.out.println("finito ciclo di inserimento");
		if(results.equalsIgnoreCase("0"))
			connection.commit();
		else
			connection.rollback();
		
		if(connection!=null) connection.close();
		
		return results;
	}
	
	/*private String createXml(QueryObject queryObject) throws Exception{
		String esito = null;
		String userName = queryObject.getNomeUtente().replace(".", "");
		
		String tipoTicket = queryObject.getTicket();
		String categoriaTicket = queryObject.getTipo();
		String ambito = queryObject.getArea();
		String nomeQuery = queryObject.getNomeQuery();
		String numRis = queryObject.getNumRisultati();
		
		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, ambito);
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
		String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket).toUpperCase();
		
		
		//String numRis = "200000";
		//if(numRis == null || numRis.trim().length()==0)
		numRis = "200000";
		
		String relevance = queryObject.getRelevance();
		String testo = queryObject.getTesto();
		if(testo==null || testo.trim().length()==0) testo="";
		testo = testo.trim();
		
		HashMap<String, Object> chiaveValore = getChiaveValore(queryObject) ;

		D2Map d2Map = new D2Map();
		esito = d2Map.BuildXMLStructCorporate("corporate", "INTERAZIONI", chiaveValore, "6", "70", "problemi", "marcocossu", "corporateinteraz");
		//esito = d2Map.BuildXMLStructTest("corporate", "CASE", chiaveValore, numRis, "50", "*", "marcocossu", "corporatecase");
		//esito = d2Map.BuildXMLStructTest(root, ticket, tipo, chiaveValore, numRis, relevance, testo, userName, nomeQuery);
		//System.out.println("esito build xml: " + esito);
		return esito;
	}*/
	
	private long getPausa(){
		long pausa = 10000L;
		try{
			String timeForConfig = PropertiesManager.getMyProperty("penthao.thread.pausa");
			if(timeForConfig!=null && timeForConfig.trim().length()>0){
				pausa = Long.parseLong(timeForConfig) * 1000;//pausa = Long.parseLong(timeForConfig);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return pausa;
	}
	
	private void getAllQuery() throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		Collection<QueryObject> queryList = querySalvateDao.getAllPublic();
		//Collection<QueryObject> queryList = null;
		//HashMap<String, Object> chiaveValore = new HashMap<String, Object>();
		//D2Map d2Map = new D2Map();
		//String ret = d2Map.BuildXMLStructCorporate("corporate", "CASE", chiaveValore, "6", "70", "problemi", "marcocossu", "corporatecase");
		StrutturaDao strutturaDaoTrunc = new StrutturaDao();
		//System.out.println("istanziato oggetto strutturaDaoTrunc, prima di truncateTables");
		strutturaDaoTrunc.truncateTables();
		long pausa = getPausa();
		if(queryList!=null && !queryList.isEmpty()){
			for(QueryObject currentQuery: queryList){
				String esito = null;

				LogThreadStrutturaDao logThreadStrutturaDao = new LogThreadStrutturaDao();
				LogThreadStruttura logThreadStruttura = new LogThreadStruttura();
				logThreadStruttura.setIdQueryEseguita(currentQuery.getID());
				logThreadStruttura.setDataEsecuzione(new Timestamp(System.currentTimeMillis()));
	
				try{
					esito = createXml(currentQuery);
					
					if("1".equalsIgnoreCase(esito)){
						logThreadStruttura.setLivello(LogThreadStruttura.WARNING);
						logThreadStruttura.setTesto("La creazione del file XML non è terminata correttamente!");
					}else{
						logThreadStruttura.setLivello(LogThreadStruttura.INFO);
						logThreadStruttura.setTesto("Operazione terminata correttamente!");
					}

				}catch (Exception e) {
					logThreadStruttura.setLivello(LogThreadStruttura.ERROR);
					logThreadStruttura.setTesto(e.getMessage());
				}finally{
					logThreadStrutturaDao.writeLog(logThreadStruttura);
				}
				
				//pausa di lancio
				try{
					Thread.sleep(pausa);
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
	}
	
	@Override
	public void run() {
		System.out.println("START: " + this.getClass().getName());
		try{
			Calendar currentDate = GregorianCalendar.getInstance();
			Calendar compareDate = GregorianCalendar.getInstance();
			
			while(true && isAlive()){
				String hours = PropertiesManager.getMyProperty("penthao.thread.oraEsecuzioni");
				if("99:99".equalsIgnoreCase(hours)){
					getAllQuery();
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

					//System.out.println("-------------------------------------------------");
					//System.out.println("currentDate.getTime(): " + currentDate.getTime());
					//System.out.println("compareDate.getTime(): " + compareDate.getTime());
					
					long sleep = compareDate.getTimeInMillis() - currentDate.getTimeInMillis();
					if(sleep>=0){
						try{
							Thread.sleep(sleep);
						}catch (Exception e) {
							// TODO: handle exception
						}

						getAllQuery();
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
