package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CategoryFrontEnd;
import model.ClusterFronEnd;
import model.HTClusterObject;
import model.JobDataDescr;
import model.Message;
import model.SnapShot;
import utility.AppConstants;
import utility.ConnectionManager;
import utility.DateConverter;
import utility.GenericServlet;
import utility.PropertiesManager;
import Autonomy.CategoryTO;
import Autonomy.ClusterData;
import Autonomy.D2Map;
import dao.HTAciResponseManagerDao;
import dao.HTClusterDao;
import dao.SnapShotDao;
import dao.TroikaDao;

/**
 * Servlet implementation class getJobList
 */
public class getJobList extends GenericServlet {
	private static final long serialVersionUID = 1L;
	private String SUFFISSO_CLUSTER = "_CLUSTERS";
	private String SUFFISSO_SPECTRO = "SG";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getJobList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	private JobDataDescr categoryOperation(JobDataDescr jobDataDescr, JobDataDescr globalConfiguration) throws Exception{

		jobDataDescr.setCategoryList(new ArrayList<CategoryFrontEnd>());

		String nomeRoot = ("Wind" + AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalConfiguration.getAmbito())).toUpperCase();
		String radiceJob = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, globalConfiguration.getRadiceJob()).toUpperCase();
		String suffissoJob =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, globalConfiguration.getSuffissoJob()).toUpperCase();
		String tipoDiRapprsentazione = jobDataDescr.getRappresentazione();
		
		String ds = "";
		if(suffissoJob.trim().length()==0){
			suffissoJob="SOCIAL";
			if(tipoDiRapprsentazione.equalsIgnoreCase(AppConstants.Rappresentazione.CATEGORY_DOMANDE)) ds = "DOMANDE";
			if(tipoDiRapprsentazione.equalsIgnoreCase(AppConstants.Rappresentazione.CATEGORY_RISPOSTE)) ds = "RISPOSTE";
		}
		
		D2Map d2Map = new D2Map();
		Collection<CategoryTO> resultCategory = d2Map.categoryHier(nomeRoot, radiceJob, suffissoJob, ds);
		
		String idRoot = null;
		for(CategoryTO currentCategory: resultCategory){
			String parent = currentCategory.getParent();
			CategoryFrontEnd categoryFrontEnd = new CategoryFrontEnd(currentCategory);
			if(currentCategory.isFoglia())
				categoryFrontEnd.setLinked(true);
			else
				categoryFrontEnd.setLinked(false);
			//if(parent==null || parent.equalsIgnoreCase(idRoot)){
				//La categoria corrente è la root o una sua figlia diretta
			//	if(parent == null) idRoot = currentCategory.getId();
			//	categoryFrontEnd.setLinked(false);
			//}
			jobDataDescr.getCategoryList().add(categoryFrontEnd);
		}

		
		//Nodo base
/*		CategoryFrontEnd root = new CategoryFrontEnd();
		root.setId("1");
		root.setLinked(false);
		
		root.setNome(nomeRoot);
		
			//Figli di I Livello
			CategoryFrontEnd categ1 = new CategoryFrontEnd();
			categ1.setId("11");
			categ1.setNome("Primo");
			categ1.setParent("1");
			
				//Figli di II Livello
				CategoryFrontEnd categ11 = new CategoryFrontEnd();
				categ11.setId("111");
				categ11.setNome("Primo1");
				categ11.setParent("11");

				CategoryFrontEnd categ12 = new CategoryFrontEnd();
				categ12.setId("112");
				categ12.setNome("Secondo1");
				categ12.setParent("11");
			
			
			CategoryFrontEnd categ2 = new CategoryFrontEnd();
			categ2.setId("12");
			categ2.setNome("Secondo");
			categ2.setParent("1");

				CategoryFrontEnd categ21 = new CategoryFrontEnd();
				categ21.setId("121");
				categ21.setNome("Primo2");
				categ21.setParent("12");

			CategoryFrontEnd categ3 = new CategoryFrontEnd();
			categ3.setId("13");
			categ3.setNome("Terzo");
			categ3.setParent("1");
			
		jobDataDescr.getCategoryList().add(root);

		jobDataDescr.getCategoryList().add(categ1);
		jobDataDescr.getCategoryList().add(categ11);
		jobDataDescr.getCategoryList().add(categ12);
		jobDataDescr.getCategoryList().add(categ2);
		jobDataDescr.getCategoryList().add(categ21);
		jobDataDescr.getCategoryList().add(categ3);
*/		
		return jobDataDescr;
	}

	private JobDataDescr snapShotGraphic(HttpServletRequest request, JobDataDescr jobDataDescr) throws Exception{
		String tipologiaTicket = jobDataDescr.getTipoTicket(); 
		
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = cm.getConnection(true);
		try{
			SnapShotDao snapShotDao = new SnapShotDao(connection);
			SnapShot snapShot = new SnapShot();
			snapShot.setSnapShot(tipologiaTicket);
			List<String> dates = snapShotDao.getDate(snapShot);
			jobDataDescr.setExtremeDate(dates);
		}catch(Exception e){
			throw e;
		}finally{
			cm.closeConnection(connection);
		}
		return jobDataDescr;

	}

	private JobDataDescr snapShotOperation(HttpServletRequest request, JobDataDescr jobDataDescr) throws Exception{
		String tipoDiRappresentazione = jobDataDescr.getRappresentazione(); 
		String tipologiaTicket = jobDataDescr.getTipoTicket(); 
		String nomeCorrente = tipologiaTicket;

		String idolServer = null;
		
		if(tipologiaTicket.equalsIgnoreCase("MOBILE_INTERAZIONI_CONSUMER"))
			idolServer = "a";
		if(tipologiaTicket.equalsIgnoreCase("FISSO_INTERAZIONI_CONSUMER"))
			idolServer = "b";
		if(tipologiaTicket.equalsIgnoreCase("FISSO_CASE_CONSUMER"))
			idolServer = "b";
		if(tipologiaTicket.equalsIgnoreCase("MOBILE_CASE_CONSUMER"))
			idolServer = "b";
		if(tipologiaTicket.equalsIgnoreCase("CASE_CORPORATE"))
			idolServer = "c";
		if(tipologiaTicket.equalsIgnoreCase("INTERAZIONI_CORPORATE"))
			idolServer = "c";
		//MOBILE_INTERZIONI_CONSUMER
		D2Map d2Map = new D2Map();
		ArrayList<ClusterData> allDataList = null;
		ArrayList<ClusterData> listToReturn = new ArrayList<ClusterData>();
		
		if(!"max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){
			allDataList = d2Map.getCuster(idolServer);
		}else{
			/*
			 * Questo blocco di codice viene eseguito localmente se su file di properties 
			 * la proprietà environment=max
			 */
			allDataList = new ArrayList<ClusterData>();
			ClusterData test = new ClusterData();
			test.setMapAcro(tipologiaTicket + SUFFISSO_CLUSTER);
			test.setSpectroAcro(tipologiaTicket + SUFFISSO_SPECTRO);
			test.setDataInizio(new ArrayList<String>());
			test.getDataInizioMap().add("1325438477");
			test.getDataInizioMap().add("1364919677");
			test.getDataInizioMap().add("1357060877");
			test.getDataInizioMap().add("1362158477");
			
			test.setDataInizioSpectro(new ArrayList<String>());
			test.getDataInizioSpectro().add("1325438487");
			test.getDataInizioSpectro().add("1364919697");
			test.getDataInizioSpectro().add("1357060847");
			test.getDataInizioSpectro().add("1362158417");

			test.setDataFineSpectro(new ArrayList<String>());
			test.getDataFineSpectro().add("2325438487");
			test.getDataFineSpectro().add("2364919697");
			test.getDataFineSpectro().add("2357060847");
			test.getDataFineSpectro().add("2362158417");

			allDataList.add(test);
		}

		if(tipoDiRappresentazione.startsWith(AppConstants.Rappresentazione.DMAP)){
			//L'utente ha selezionato 2DMAP
			nomeCorrente = nomeCorrente + SUFFISSO_CLUSTER;
		}else if(tipoDiRappresentazione.startsWith(AppConstants.Rappresentazione.SPECTRO)) {
			//L'utente ha selezionato SPECTRO
			nomeCorrente = nomeCorrente + SUFFISSO_SPECTRO;
		}

		if(allDataList!=null){
			logger.debug("Cerco le date per il JOB: " + nomeCorrente);
			for(ClusterData currentData: allDataList){
				/*
				 * Gli oggetti relativi a 2DMAP si chiamano xxx_CLUSTERS
				 * Gli oggetti relativi a SPECTRO si chiamano xxxSG
				 * xxx può valere FISSO o MOBILE
				 */
				String currentDataName = "";
				if(nomeCorrente.endsWith(SUFFISSO_CLUSTER)){
					currentDataName = currentData.getMapAcro();
				}else{
					currentDataName = currentData.getSpectroAcro();
				}
				
				if(currentDataName!=null && 
						currentDataName.trim().length()!=0 &&
						currentDataName.startsWith(nomeCorrente)){
					listToReturn.add(currentData);
					request.getSession().setAttribute("2DMAP_dataInizio", currentData.getDataInizioMap());
					request.getSession().setAttribute("SPECTRO_dataInizio", currentData.getDataInizioSpectro());
					request.getSession().setAttribute("SPECTRO_dataFine", currentData.getDataFineSpectro());
					
					logger.debug("Nome JOB: " + currentData.getMapAcro() + " "+ currentData.getSpectroAcro());
					logger.debug("Num Date inizio map: " + (currentData.getDataInizioMap()!=null?currentData.getDataInizioMap().size():""));
					logger.debug("Num Date fine map: " + (currentData.getDataFineMap()!=null?currentData.getDataFineMap().size():""));
					logger.debug("Num Date inzio spettro: " + (currentData.getDataInizioSpectro()!=null?currentData.getDataInizioSpectro().size():""));
					logger.debug("Num Date fine spettro: " + (currentData.getDataFineSpectro()!=null?currentData.getDataFineSpectro().size():""));
				}
			}
		}
		
		jobDataDescr.setList(listToReturn);
		return jobDataDescr;
	}
	
	private JobDataDescr getValuesFirstCombo(JobDataDescr jobDataDescr) throws Exception{
		String tipoDiRappresentazione = jobDataDescr.getRadiceJob(); 
		String ambito = jobDataDescr.getAmbito();
		TroikaDao troikaDao = new TroikaDao();
		jobDataDescr.setComboValues(troikaDao.getColumnValues(null,ambito, tipoDiRappresentazione));
		return jobDataDescr;
	}

	private String getTipoTicket(JobDataDescr jobDataDescr){
		String nome = "";
		String radiceJob = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, jobDataDescr.getRadiceJob()).toUpperCase();
		String suffissoJob =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, jobDataDescr.getSuffissoJob()).toUpperCase();
		String ambito =AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, jobDataDescr.getAmbito()).toUpperCase();
		String classeReportDescr = AppConstants.getLabelFromIndex(AppConstants.classeReportLabel, jobDataDescr.getClasseReport()).toUpperCase();
		if(AppConstants.ClasseReport.SOCIAL.equalsIgnoreCase(jobDataDescr.getClasseReport())){suffissoJob = classeReportDescr;}

		nome = radiceJob + "_" + suffissoJob + "_" + ambito;
		if(radiceJob.isEmpty()){
			nome = suffissoJob + "_" + ambito;
		}

		return nome;
	}
	
	private void makeHTTableDate(HttpServletRequest request)throws Exception{
		Map<String, Timestamp> tableDate = new HashMap<String, Timestamp>();
		Collection<String> date = new ArrayList<String>();

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = null;
		try{
			connection = cm.getConnection(true);

			HTAciResponseManagerDao htAciResponseManagerDao = new HTAciResponseManagerDao(connection);
			Collection<Timestamp> dateTS = htAciResponseManagerDao.getDate();
			for(Timestamp dataElaborazione: dateTS){
				String dateOnPage = DateConverter.getDate(dataElaborazione, DateConverter.PATTERN_VIEW);
				date.add(dateOnPage);
				tableDate.put(dateOnPage, dataElaborazione);
			}
			
			String[] dateA = date.toArray(new String[date.size()]);
			request.getSession().setAttribute("HT_tableDate", tableDate);
			request.getSession().setAttribute("HT_date", dateA);
			
		}finally{
			cm.closeConnection(connection);
		}
		
	}
	
	private String[] getParametriCluster(HttpServletRequest request){
		String dataSelected = request.getParameter("dataElaborazioneScelta");
		String[] dateA = (String[])request.getSession().getAttribute("HT_date");
		Timestamp dataSelectedDB = null;
		String nomeTabellaCluster = AppConstants.HT_TABLE_CLUSTER;
		String nomeTabellaDoc = AppConstants.HT_TABLE_DOC;
		String maxDate = dateA[dateA.length-1];
		if(dataSelected!=null && !dataSelected.isEmpty()){
			if(!dataSelected.equalsIgnoreCase(maxDate)){
				//Non è la data relativa all'ultima elaborazione
				Map<String, Timestamp> tableDate = (Map<String, Timestamp>)request.getSession().getAttribute("HT_tableDate");	
				dataSelectedDB = tableDate.get(dataSelected);
				if(dataSelectedDB!=null){
					String suffix = DateConverter.getDate(dataSelectedDB, DateConverter.PATTERN_DB);
					nomeTabellaCluster = nomeTabellaCluster + "_" + suffix;
					nomeTabellaDoc = nomeTabellaDoc  + "_" + suffix;
					request.setAttribute("dataElaborazioneScelta", dataSelected);
				}else{
					//E'stata indicata una data nell'intervallo (minDate, maxDate) cui non corrisponde alcuna elaborazione.
					Message message = new Message();
					message.setText("In data <b>" + dataSelected + "</b> non è stato prodotta importazione.");
					message.setType(Message.WARNING);
					request.setAttribute("message", message);
					dataSelected = null;
				}
			}else{
				dataSelected = null;
			}
		}
		request.setAttribute("nomeTabellaDoc", nomeTabellaDoc);
		return new String[]{nomeTabellaCluster, dataSelected};
	}
	
	private JobDataDescr getHotTopicsCluster(String[] parametriCluster, 
											 JobDataDescr jobDataDescr, 
											 JobDataDescr globalConfiguration)  throws Exception{
		String nomeTabellaCluster = parametriCluster[0];
		String dataSelected = parametriCluster[1];
		jobDataDescr.setClusterList(new ArrayList<ClusterFronEnd>());
		
		ConnectionManager cm = ConnectionManager.getInstance();
		
		String tipo = globalConfiguration.getRadiceJob();
		String categoria = globalConfiguration.getSuffissoJob();
		int indiceQuery = AppConstants.getQueryNum(tipo, categoria);
		
		String rootName = AppConstants.getLabelFromIndex(AppConstants.clusteringRoot, (indiceQuery-1)+"") + (dataSelected!=null?" (" + dataSelected + ")":"");
		ClusterFronEnd root = new ClusterFronEnd();
		root.setID("-1");
		root.setNome(rootName);
		root.setLinked(false);
		jobDataDescr.getClusterList().add(root);
		
		Connection connection = null;
		try{
			connection = cm.getConnection(true);
			HTClusterDao htClusterDao = new HTClusterDao(connection, nomeTabellaCluster);
			
			HTClusterObject htClusterObject = new HTClusterObject();
			htClusterObject.setIdQuery(indiceQuery+"");
			
			List<HTClusterObject> result = htClusterDao.getClusterByQuery(htClusterObject);
			for(HTClusterObject currentCluster: result){
				jobDataDescr.getClusterList().add(new ClusterFronEnd(currentCluster));
			}
			
		}finally{
			cm.closeConnection(connection);
		}
		return jobDataDescr;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobDataDescr jobDataDescr = null;
		String redirect = null;
		String operation = null;
		Message message = new Message();
		
		try{
			jobDataDescr = getToken(request);
			
			String tipoDiRappresentazione = jobDataDescr.getRappresentazione(); //2DMap o Spectro
			String tipologiaTicket = jobDataDescr.getTipoTicket(); //FISSO o MOBILE
			String classeReport = jobDataDescr.getClasseReport();
			
			if(tipologiaTicket==null || tipologiaTicket.trim().length()==0){
				tipologiaTicket = getTipoTicket(jobDataDescr);
				jobDataDescr.setTipoTicket(tipologiaTicket);
			}else if(tipoDiRappresentazione!=null && AppConstants.ClasseReport.SOCIAL.equalsIgnoreCase(jobDataDescr.getClasseReport())){
				String[] tokens = tipologiaTicket.split("\\_");
				
				tipologiaTicket = tokens[0] + "_"+tokens[1]+"_"+tokens[2];
				
				if(AppConstants.Rappresentazione.DMAP_DOMANDE.equals(tipoDiRappresentazione) || 
				   AppConstants.Rappresentazione.SPECTRO_DOMANDE.equals(tipoDiRappresentazione)) tipologiaTicket = tipologiaTicket + "_DOMANDE";
				
				if(AppConstants.Rappresentazione.DMAP_RISPOSTE.equals(tipoDiRappresentazione) || 
				   AppConstants.Rappresentazione.SPECTRO_RISPOSTE.equals(tipoDiRappresentazione)) tipologiaTicket = tipologiaTicket + "_RISPOSTE";
/*				if(AppConstants.Rappresentazione.SPECTRO_DOMANDE.equals(tipoDiRappresentazione)) tokens[2] = tokens[2] + "_DOMANDE";
				if(AppConstants.Rappresentazione.SPECTRO_RISPOSTE.equals(tipoDiRappresentazione)) tokens[2] = tokens[2] + "_RISPOSTE";
				tipologiaTicket = tokens[0] + "_"+tokens[1]+"_"+tokens[2];
*/
				jobDataDescr.setTipoTicket(tipologiaTicket);
			}
			operation = jobDataDescr.getOperation();			

			redirect = "defineReport.jsp";
			if(AppConstants.ClasseReport.SOCIAL.equalsIgnoreCase(classeReport)) redirect = "social/" + redirect;

			//Recupero tutti i dati relativi a 2DMap e Spectro
			if(!"0".equalsIgnoreCase(operation) && !"1".equalsIgnoreCase(operation) && 
					(tipoDiRappresentazione==null || tipologiaTicket==null || tipoDiRappresentazione.trim().length()==0 || tipologiaTicket.trim().length()==0)){
				throw new Exception("E'necessario valorizzare il tipo di rappresentazione e il tipo di ticket");
			} 
			
			if(tipoDiRappresentazione!=null){
				boolean warnCondition = false;
				if(tipoDiRappresentazione.startsWith(AppConstants.Rappresentazione.DMAP) || tipoDiRappresentazione.startsWith(AppConstants.Rappresentazione.SPECTRO)){
					jobDataDescr = snapShotOperation(request,jobDataDescr);
					warnCondition = jobDataDescr.getList()==null || jobDataDescr.getList().isEmpty();
				}else if(tipoDiRappresentazione.equalsIgnoreCase(AppConstants.Rappresentazione.GRAPH)){
					jobDataDescr = snapShotGraphic(request, jobDataDescr);
					warnCondition = jobDataDescr.getExtremeDate()==null || jobDataDescr.getExtremeDate().isEmpty();
				}

				if(warnCondition){
					message.setType(Message.WARNING);
					message.setText("Non ci sono elaborazioni disponibili nel sistema.");
					request.setAttribute("message", message);
				}
			}
			
/*			if(tipoDiRappresentazione!=null && 
					(tipoDiRappresentazione.startsWith(AppConstants.Rappresentazione.DMAP) || tipoDiRappresentazione.startsWith(AppConstants.Rappresentazione.SPECTRO))){
				jobDataDescr = snapShotOperation(request,jobDataDescr);
				if(jobDataDescr.getList()==null || jobDataDescr.getList().isEmpty()){
					message.setType(Message.WARNING);
					message.setText("Non ci sono elaborazioni disponibili nel sistema.");
					request.setAttribute("message", message);
				}
			}
*/			
			if(tipoDiRappresentazione!=null && 
					tipoDiRappresentazione.startsWith(AppConstants.Rappresentazione.CATEGORY)){
				jobDataDescr = categoryOperation(jobDataDescr, (JobDataDescr)request.getSession().getAttribute("globalEnvironment"));
				redirect = "category/categoryTree.jsp";
				
			}

			if(AppConstants.Rappresentazione.TRIPLETTA.equalsIgnoreCase(tipoDiRappresentazione) ){
				jobDataDescr = getValuesFirstCombo((JobDataDescr)request.getSession().getAttribute("globalEnvironment"));
				redirect = "tripletta/tripletta.jsp";
			}

			if(AppConstants.Rappresentazione.QUERYSOCIAL.equalsIgnoreCase(tipoDiRappresentazione) ){
				redirect = "querySocial/query.jsp";
			}

			if(AppConstants.Rappresentazione.HOTTOPICS.equalsIgnoreCase(tipoDiRappresentazione) ){
				makeHTTableDate(request);
				String[] parametriCluster = getParametriCluster(request);
				getHotTopicsCluster(parametriCluster, jobDataDescr, (JobDataDescr)request.getSession().getAttribute("globalEnvironment"));
				redirect = "hotTopics/hotTopicsTree.jsp";
			}

		}catch(Exception e){
			e.printStackTrace();
			if("0".equalsIgnoreCase(operation)) redirect = "start.jsp";
			message.setText("Si è verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			message.setType(Message.ERROR);
			request.setAttribute("message", message);
		}finally{
			request.setAttribute("jobDataDescr", jobDataDescr);
			if("0".equalsIgnoreCase(operation)) request.getSession().setAttribute("globalEnvironment", jobDataDescr);
		}
		
		forward(request, response, redirect);
	
	}

}
