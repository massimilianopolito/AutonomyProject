package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DatiQuery;
import model.JobDataDescr;
import model.Message;
import model.PenthaoObject;
import model.QueryObject;
import thread.ManageThread;
import utility.AppConstants;
import utility.DateConverter;
import utility.PropertiesManager;
import Autonomy.D2Map;
import Autonomy.DocumentoQueryTO;
import dao.PenthaoDao;
import dao.QueryDatiDao;
import dao.QuerySalvateDao;
import dao.StrutturaDao;
import dao.TroikaDao;

/**
 * Servlet implementation class ManageStruttura
 */
public class ManageStruttura extends ManageRealTime {
	private static final long serialVersionUID = 1L;
    /**
     * Default constructor. 
     */
    public ManageStruttura() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String redirect="struttura/chooser.jsp";
		try{
			makeConsolePath(request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		forward(request, response, redirect);
}
	
	private QueryObject getQueryObject(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		String radice = globalEnv.getRadiceJob();
		String suffisso = globalEnv.getSuffissoJob();
		String area = globalEnv.getAmbito();

		QueryObject queryObject = new QueryObject();
		queryObject.setID(request.getParameter("ID"));
		queryObject.setNomeQuery(request.getParameter("nomeQuery"));
		queryObject.setNomeUtente(request.getRemoteUser());
		queryObject.setNumRisultati(request.getParameter("numRisultati"));
		queryObject.setRelevance(request.getParameter("relevance"));
		queryObject.setTesto(request.getParameter("testo"));
		queryObject.setTicket(radice);
		queryObject.setTipo(suffisso);
		queryObject.setArea(area);
		
		/*if("8".equalsIgnoreCase(operation))
		{	
			request.setAttribute("queryObjectStrutturaPub", queryObject);
			request.getSession().setAttribute("queryObjectStrutturaPub", queryObject);
		}
		else
		{
			request.setAttribute("queryObjectStruttura", queryObject);
			request.getSession().setAttribute("queryObjectStruttura", queryObject);
		}*/	
		if("1".equalsIgnoreCase(operation) && queryObject.getID() == null){
			if(queryObject.getNomeQuery()==null || queryObject.getNomeQuery().trim().length()==0) throw new Exception("Il nome della query è obbligatorio.");
			QuerySalvateDao querySalvateDao = new QuerySalvateDao();
			querySalvateDao.checkQueryNameByUser(queryObject);
		}
		
		return queryObject;
	}
	
	private void getListQuery(HttpServletRequest request, JobDataDescr globalEnv)throws Exception{
		String radice = globalEnv.getRadiceJob();
		String suffisso = globalEnv.getSuffissoJob();
		String area = globalEnv.getAmbito();

		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryObject queryObject = new QueryObject();
		queryObject.setNomeUtente(request.getRemoteUser());
		queryObject.setTicket(radice);
		queryObject.setTipo(suffisso);
		queryObject.setArea(area);
		Collection<QueryObject> queryList = querySalvateDao.getQuery(queryObject);
		request.setAttribute("queryList", queryList);

	}
	
	private Collection<DocumentoQueryTO> viewDetails(Collection<DatiQuery> listResult, String nomeQuery, JobDataDescr globalEnv)throws Exception{
		
		StrutturaDao strutturaDao = new StrutturaDao();
		
		String radice = globalEnv.getRadiceJob();
		String suffisso = globalEnv.getSuffissoJob();
		String area = globalEnv.getAmbito();
		
		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, radice).toUpperCase();
		String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, suffisso);
		String table = null;
		String dbS = null;
		if(root.equalsIgnoreCase("Consumer"))
		{
			if(ticket.equalsIgnoreCase("FISSO"))
			{
				if(tipo.equalsIgnoreCase("INTERAZIONI"))
				{	
					table = "autonomy_int_fisso";
					dbS = "IntFissoConsumer"; 
				}
				else
				{	
					table = "autonomy_case_fisso";
					dbS = "CaseFissoConsumer";
				}	
					
			}
			else
			{
				if(tipo.equalsIgnoreCase("INTERAZIONI"))
				{
					table = "autonomy_int_mobile";
					dbS = "IntMobileConsumer";
				}	
				else
				{	
					table = "autonomy_case_mobile";
					dbS = "CaseMobileConsumer";
				}	
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
		
		HashMap<String, Object> chiaveValore = makeHashPublic(listResult, globalEnv, tipo);
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
//		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			Object valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(valoreCorrente instanceof String){
				if(!valoreCorrente.equals("--"))
				{
/*					if(i > 0)
						valori = valori + "+AND+";
*/					
					valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//					i++;
				}
			}else if(valoreCorrente instanceof String[]){
				String[] values = (String[])valoreCorrente;
				String testNull = values[0] + values[1];
				if(testNull.trim().length()>2){
					valori = valori + "RANGE{"+values[0]+","+values[1]+ "}:" + chiaveCorrente + "+AND+";
				}
			}
		}
		if(valori.endsWith("+AND+")){
			int i = valori.lastIndexOf("+AND+");
			valori = valori.substring(0,i);
		}
		D2Map d2Map = new D2Map();
		String numDoc = "";
		if(!valori.equals(""))
			numDoc= d2Map.getNumTotHits(dbS, valori);
		else
			numDoc = d2Map.getNumTotaleDocumenti(dbS);
		
		System.out.println("numDoc: " + numDoc);
		
		Collection<DocumentoQueryTO> documentList = strutturaDao.getResult(numDoc,nomeQuery,table);
		
		return documentList;
	}
	private void getListQueryPublic(HttpServletRequest request, JobDataDescr globalEnv)throws Exception{
		String radice = globalEnv.getRadiceJob();
		String suffisso = globalEnv.getSuffissoJob();
		String area = globalEnv.getAmbito();

		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryObject queryObject = new QueryObject();
		queryObject.setNomeUtente(request.getRemoteUser());
		queryObject.setTicket(radice);
		queryObject.setTipo(suffisso);
		queryObject.setArea(area);
		Collection<QueryObject> queryListPublic = querySalvateDao.getQueryPublic(queryObject);
		request.setAttribute("queryListPublic", queryListPublic);

	}
	
	private Collection<DatiQuery> getListDynamicField(HttpServletRequest request)throws Exception{
		
		Collection<DatiQuery>  listDynamicField = new ArrayList<DatiQuery>();
		String dataDa = request.getParameter("DATA_CREAZIONE_DA");
		String dataA = request.getParameter("DATA_CREAZIONE_A");
		String gap = request.getParameter("GAP");
		String first = request.getParameter("first");
		String second = request.getParameter("second");
		String third = request.getParameter("third");
		
		if(first!=null && first.trim().length()!=0 && !first.equalsIgnoreCase("--"))
		{	
			DatiQuery datiQuery = new DatiQuery();
			datiQuery.setIdCampo("first");
			datiQuery.setValoreCampo(first);
			listDynamicField.add(datiQuery);
							
		}	
		if(second!=null && second.trim().length()!=0 && !second.equalsIgnoreCase("--"))
		{	
			DatiQuery datiQuery = new DatiQuery();
			datiQuery.setIdCampo("second");
			datiQuery.setValoreCampo(second);
			listDynamicField.add(datiQuery);
							
		}
			
		if(third!=null && third.trim().length()!=0 && !third.equalsIgnoreCase("--"))
		{	
			DatiQuery datiQuery = new DatiQuery();
			datiQuery.setIdCampo("third");
			datiQuery.setValoreCampo(third);
			listDynamicField.add(datiQuery);
							
		}
			
	
		if(!dataDa.equals(""))
		{	
			DatiQuery datiQuery = new DatiQuery();
			datiQuery.setIdCampo("DATA_CREAZIONE_DA");
			datiQuery.setValoreCampo(dataDa);
			listDynamicField.add(datiQuery);
		}

		if(!dataA.equals(""))
		{
			DatiQuery datiQuery = new DatiQuery();
			datiQuery.setIdCampo("DATA_CREAZIONE_A");
			datiQuery.setValoreCampo(dataA);
			listDynamicField.add(datiQuery);
		}

		if(gap!=null && !gap.isEmpty() && !gap.equalsIgnoreCase("--"))
		{
			DatiQuery datiQuery = new DatiQuery();
			datiQuery.setIdCampo("GAP");
			datiQuery.setValoreCampo(gap);
			listDynamicField.add(datiQuery);
		}

		return listDynamicField;
	}
	
	protected Collection<DatiQuery> getFieldValue(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		
		String tipoTicket = globalEnv.getRadiceJob();
		String categoriaTicket = globalEnv.getSuffissoJob();
		Collection<DatiQuery> listDynamicField = null;
		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
		String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket);
		if(root.equalsIgnoreCase("consumer"))
		{	
			listDynamicField = getListDynamicField(request);
		}
		if(listDynamicField!=null && listDynamicField.size()>0)
			request.getSession().setAttribute("listFieldvalue", listDynamicField);
		else
			request.getSession().setAttribute("listFieldvalue", null);
		
		return listDynamicField;
	}
	
protected void getFieldValueUp(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		
		String tipoTicket = globalEnv.getRadiceJob();
		String categoriaTicket = globalEnv.getSuffissoJob();
		Collection<DatiQuery> listDynamicField = null;
		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
		String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket);
		if(root.equalsIgnoreCase("consumer"))
		{			
			listDynamicField = getListDynamicField(request);
		}
		if(listDynamicField!=null && listDynamicField.size()>0)
			request.getSession().setAttribute("listFieldvalue", listDynamicField);
		else
			request.getSession().setAttribute("listFieldvalue", null);
	}

protected void getFieldValueQuery(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
	
	String tipoTicket = globalEnv.getRadiceJob();
	String categoriaTicket = globalEnv.getSuffissoJob();
	Collection<DatiQuery> listDynamicField = null;
	String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
	String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
	String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket);
	if(root.equalsIgnoreCase("consumer"))
	{
		listDynamicField = getListDynamicField(request);
	}
	if(listDynamicField!=null && listDynamicField.size()>0)
		request.getSession().setAttribute("listFieldvalue", listDynamicField);
	else
		request.getSession().setAttribute("listFieldvalue", null);
	
	QueryObject queryObject = new QueryObject();
	queryObject.setID(request.getParameter("ID"));
	queryObject.setNomeQuery(request.getParameter("nomeQuery"));
	queryObject.setNomeUtente(request.getRemoteUser());
	queryObject.setNumRisultati(request.getParameter("numRisultati"));
	queryObject.setRelevance(request.getParameter("relevance"));
	queryObject.setTesto(request.getParameter("testo"));
	queryObject.setTicket(ticket);
	queryObject.setTipo(tipo);
	queryObject.setArea(root);
	
	request.setAttribute("queryObjectStruttura", queryObject);
	request.getSession().setAttribute("queryObjectStruttura", queryObject);
	
	
}

protected void getFieldValueQueryPublic(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
	
	String tipoTicket = globalEnv.getRadiceJob();
	String categoriaTicket = globalEnv.getSuffissoJob();
	Collection<DatiQuery> listDynamicField = null;
	String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
	String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
	String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket);
	if(root.equalsIgnoreCase("consumer"))
	{
		listDynamicField = getListDynamicField(request);

	}
	if(listDynamicField!=null && listDynamicField.size()>0)
		request.getSession().setAttribute("listFieldvaluePub", listDynamicField);
	else
		request.getSession().setAttribute("listFieldvaluePub", null);
	
	QueryObject queryObject = new QueryObject();
	queryObject.setID(request.getParameter("ID"));
	queryObject.setNomeQuery(request.getParameter("nomeQuery"));
	queryObject.setNomeUtente(request.getRemoteUser());
	queryObject.setNumRisultati(request.getParameter("numRisultati"));
	queryObject.setRelevance(request.getParameter("relevance"));
	queryObject.setTesto(request.getParameter("testo"));
	queryObject.setTicket(ticket);
	queryObject.setTipo(tipo);
	queryObject.setArea(root);
	
	request.setAttribute("queryObjectStrutturaPub", queryObject);
	request.getSession().setAttribute("queryObjectStrutturaPub", queryObject);
	
	
}
	private void insertQuery(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryObject queryObject = getQueryObject(request, globalEnv);
		//Collection<DatiQuery> listDatiQuery = getDynamicFieldValues(request, globalEnv);
		Collection<DatiQuery> listDatiQuery = getFieldValue(request, globalEnv);
		queryObject = querySalvateDao.manageQuerySalvate(queryObject, listDatiQuery);
		request.setAttribute("queryObjectStruttura", queryObject);
		request.getSession().setAttribute("queryObjectStruttura", queryObject);
		request.getSession().setAttribute("listaRisultatiStruttura", null);
	}
	
	private String insertQueryPublic(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryDatiDao queryDatiDao = new QueryDatiDao();
		QueryObject queryObject = getQueryObject(request, globalEnv);
		queryObject = querySalvateDao.getQueryById(queryObject);
		Collection<DatiQuery> listDatiQuery = queryDatiDao.getQuery(queryObject);
		String id = querySalvateDao.isExistsPub(queryObject.getNomeQuery());
		String mes = null;
		if(id==null)
			queryObject = querySalvateDao.manageQuerySalvatePublic(queryObject, listDatiQuery);
		else
			mes = "Non è possibile pubblicare la stessa query. Esiste già una query pubblica con questo nome";
		
		String texto = request.getParameter("testo");
		System.out.println("testo: " +texto);
		//request.setAttribute("queryObjectStruttura", queryObject);
		//request.getSession().setAttribute("queryObjectStruttura", queryObject);
		return mes;
	}

	private void updateQuery(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryDatiDao queryDatiDao = new QueryDatiDao();
		QueryObject queryObject = getQueryObject(request, globalEnv);
		queryObject = querySalvateDao.getQueryById(queryObject);
		Collection<DatiQuery> listDatiQuery = queryDatiDao.getQuery(queryObject);
		if(listDatiQuery!=null && listDatiQuery.size()>0)
			request.getSession().setAttribute("listFieldvalue", listDatiQuery);
		else
			request.getSession().setAttribute("listFieldvalue", null);
		
		request.setAttribute("queryObjectStruttura", queryObject);
		request.getSession().setAttribute("queryObjectStruttura", queryObject);
		request.getSession().setAttribute("listaRisultatiStruttura", null);
	}
	
	private void updateQueryPublic(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryDatiDao queryDatiDao = new QueryDatiDao();
		QueryObject queryObject = getQueryObject(request, globalEnv);
		queryObject = querySalvateDao.getQueryByIdPublic(queryObject);
		Collection<DatiQuery> listDatiQuery = queryDatiDao.getQueryPublic(queryObject);
		if(listDatiQuery!=null && listDatiQuery.size()>0)
			request.getSession().setAttribute("listFieldvaluePub", listDatiQuery);
		else
			request.getSession().setAttribute("listFieldvaluePub", null);
		
		request.setAttribute("queryObjectStrutturaPub", queryObject);
		request.getSession().setAttribute("queryObjectStrutturaPub", queryObject);
		request.getSession().setAttribute("listaRisultatiStruttura", null);
	}
	
	private String savePublicInPrivate(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryDatiDao queryDatiDao = new QueryDatiDao();
		QueryObject queryObject = getQueryObject(request, globalEnv);
		queryObject = querySalvateDao.getQueryByIdPublic(queryObject);
		String user = request.getRemoteUser();
		querySalvateDao.checkQueryNameByUser(queryObject, user);
		
		Collection<DatiQuery> listDatiQuery = queryDatiDao.getQueryPublic(queryObject);
		
		queryObject = querySalvateDao.insertQuerySalvate(queryObject, listDatiQuery, user);
		String msg = null;
		if(queryObject.getID()!=null)
			msg="Salvataggio concluso con successo";
		
		if(listDatiQuery!=null && listDatiQuery.size()>0)
			request.getSession().setAttribute("listFieldvaluePub", listDatiQuery);
		else
			request.getSession().setAttribute("listFieldvaluePub", null);
		
		request.setAttribute("queryObjectStrutturaPub", queryObject);
		request.getSession().setAttribute("queryObjectStrutturaPub", queryObject);
	
		return msg;
	}

	private void deleteQuery(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryObject queryObject = getQueryObject(request, globalEnv);
		querySalvateDao.deleteQuerySalvate(queryObject);
		request.setAttribute("queryObjectStruttura", null);
		request.getSession().setAttribute("queryObjectStruttura", null);
		request.getSession().setAttribute("listaRisultatiStruttura", null);
		request.getSession().setAttribute("listFieldvalue", null);
		
	}
	
	private void deleteQueryPublic(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryObject queryObject = getQueryObject(request, globalEnv);
		querySalvateDao.deleteQuerySalvatePublic(queryObject);
		request.getSession().setAttribute("listFieldvaluePub", null);
		request.getSession().setAttribute("queryObjectStrutturaPub", null);
		request.getSession().setAttribute("listaRisultatiStruttura", null);
		//request.setAttribute("queryObjectStruttura", null);
	}
	
	private void makeConsolePath(HttpServletRequest request)throws Exception{
		String path = PropertiesManager.getMyProperty("penthao.base.console.path");
		String user = request.getRemoteUser().replace(".", "");
		String pwd = "password";
		path = path.replace("[NOMEUTENTE]", user).replace("[PASSWORD]", pwd);
		request.setAttribute("pathConsole", path);
	}
	
	protected HashMap<String, Object> makeHash(HttpServletRequest request, JobDataDescr globalEnv, String tipoTicket) throws Exception{
		HashMap<String, Object> chiaveValore = new HashMap<String, Object>();
		//String tipoTicket = globalEnv.getRadiceJob();
		//String categoriaTicket = globalEnv.getSuffissoJob();
		System.out.println("tipo ticket" + tipoTicket);
		String dataDa = request.getParameter("DATA_CREAZIONE_DA");
		String dataA = request.getParameter("DATA_CREAZIONE_A");
		String gap = request.getParameter("GAP");
		if(dataA==null)
			System.out.println("data A null: " + dataA);
		if(dataA=="")
			System.out.println("data A è stringa vuota: " + dataA);
		String first = request.getParameter("first");
		String second = request.getParameter("second");
		String third = request.getParameter("third");
		
		if("--".equalsIgnoreCase(gap)) gap = null;
		
		if(tipoTicket.equalsIgnoreCase("INTERAZIONI"))
		{	
			if(first!=null && first.trim().length()!=0 && !first.equalsIgnoreCase("--"))
				chiaveValore.put("MOTIVO", first);
			if(second!=null && second.trim().length()!=0 && !second.equalsIgnoreCase("--"))
				chiaveValore.put("ARGOMENTO", second);
			if(third!=null && third.trim().length()!=0 && !third.equalsIgnoreCase("--"))
				chiaveValore.put("SPECIFICA", third);
		}
		else
		{
			if(first!=null && first.trim().length()!=0 && !first.equalsIgnoreCase("--"))
				chiaveValore.put("MOTIVO_TRIPLETTA", first);
			if(second!=null && second.trim().length()!=0 && !second.equalsIgnoreCase("--"))
				chiaveValore.put("ARGOMENTO_TRIPLETTA", second);
			if(third!=null && third.trim().length()!=0 && !third.equalsIgnoreCase("--"))
				chiaveValore.put("SPECIFICA_TRIPLETTA", third);
		}	
		
/*		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		if(dataA!=null && !dataA.isEmpty()){
			end.setTime(sdf.parse(dataA));
		}

		if(dataDa!=null && !dataDa.isEmpty()){
			start.setTime(sdf.parse(dataDa));
		}else{
			start.setTime(end.getTime());
			if(gap==null || gap.isEmpty()){
				String months = PropertiesManager.getMyProperty("penthao.thread.month");
				int monthInt = 3;
				try{
					monthInt = Integer.parseInt(months);
				}catch(Exception e){
					
				}
				start.add(Calendar.MONTH, -monthInt);
			}else{
				int gapInt = Integer.parseInt(gap);
				start.add(Calendar.DATE, -gapInt);
			}
		}

		String[] dateValues = {sdf.format(start.getTime()), sdf.format(end.getTime())};
		chiaveValore.put("DATA_CREAZIONE", dateValues);
*/
		if(!dataDa.equals("") && !dataA.equals(""))
		{	
			String[] dateValues = {dataDa, dataA};
			chiaveValore.put("DATA_CREAZIONE", dateValues);
		}
		else if(!dataDa.equals("") && dataA.equals(""))
		{
			String[] dateValues = {dataDa, "."};
			chiaveValore.put("DATA_CREAZIONE", dateValues);
		}
		else if(dataDa.equals("") && !dataA.equals(""))
		{
			dataDa = ".";
			if(gap!=null && !gap.isEmpty()){
				Timestamp startDate =  DateConverter.getDate(dataA, DateConverter.PATTERN_VIEW);
				dataDa = getDataDa(startDate.getTime(), gap);
			}
			
			String[] dateValues = {dataDa, dataA};
			chiaveValore.put("DATA_CREAZIONE", dateValues);
		}
		else if(dataDa.equals("") && dataA.equals("") && gap!=null && !gap.isEmpty())
		{
			long currenTimeMillis = System.currentTimeMillis();
			dataA =  DateConverter.getDate(new Timestamp(currenTimeMillis), DateConverter.PATTERN_VIEW);
			dataDa = getDataDa(currenTimeMillis, gap);

			String[] dateValues = {dataDa, dataA};
			chiaveValore.put("DATA_CREAZIONE", dateValues);
		}
		return chiaveValore;
	}
	
	protected HashMap<String, Object> makeHashPublic(Collection<DatiQuery> listResult, JobDataDescr globalEnv, String tipoTicket) throws Exception{
		HashMap<String, Object> chiaveValore = new HashMap<String, Object>();
		//String tipoTicket = globalEnv.getRadiceJob();
		//String categoriaTicket = globalEnv.getSuffissoJob();
		System.out.println("tipo ticket" + tipoTicket);
		String dataDa = "";
		String dataA = "";
		String gap = "";
		String first = "";
		String second = "";
		String third = "";
		
		if(listResult!=null && !listResult.isEmpty())
		{
			for(DatiQuery currentData: listResult){
			
				if(currentData.getIdCampo().equalsIgnoreCase("DATA_CREAZIONE_DA"))
					dataDa = currentData.getValoreCampo();
				if(currentData.getIdCampo().equalsIgnoreCase("DATA_CREAZIONE_A"))
					dataA = currentData.getValoreCampo();
				if(currentData.getIdCampo().equalsIgnoreCase("GAP"))
					gap = currentData.getValoreCampo();
				if(currentData.getIdCampo().equalsIgnoreCase("first"))
					first = currentData.getValoreCampo();
				if(currentData.getIdCampo().equalsIgnoreCase("second"))
					second = currentData.getValoreCampo();
				if(currentData.getIdCampo().equalsIgnoreCase("third"))
					third = currentData.getValoreCampo();
			}
		}
		
		
		
		if("--".equalsIgnoreCase(gap)) gap = null;
		
		if(tipoTicket.equalsIgnoreCase("INTERAZIONI"))
		{	
			if(first!=null && first.trim().length()!=0 && !first.equalsIgnoreCase("--"))
				chiaveValore.put("MOTIVO", first);
			if(second!=null && second.trim().length()!=0 && !second.equalsIgnoreCase("--"))
				chiaveValore.put("ARGOMENTO", second);
			if(third!=null && third.trim().length()!=0 && !third.equalsIgnoreCase("--"))
				chiaveValore.put("SPECIFICA", third);
		}
		else
		{
			if(first!=null && first.trim().length()!=0 && !first.equalsIgnoreCase("--"))
				chiaveValore.put("MOTIVO_TRIPLETTA", first);
			if(second!=null && second.trim().length()!=0 && !second.equalsIgnoreCase("--"))
				chiaveValore.put("ARGOMENTO_TRIPLETTA", second);
			if(third!=null && third.trim().length()!=0 && !third.equalsIgnoreCase("--"))
				chiaveValore.put("SPECIFICA_TRIPLETTA", third);
		}	
		
/*		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		if(dataA!=null && !dataA.isEmpty()){
			end.setTime(sdf.parse(dataA));
		}

		if(dataDa!=null && !dataDa.isEmpty()){
			start.setTime(sdf.parse(dataDa));
		}else{
			start.setTime(end.getTime());
			if(gap==null || gap.isEmpty()){
				String months = PropertiesManager.getMyProperty("penthao.thread.month");
				int monthInt = 3;
				try{
					monthInt = Integer.parseInt(months);
				}catch(Exception e){
					
				}
				start.add(Calendar.MONTH, -monthInt);
			}else{
				int gapInt = Integer.parseInt(gap);
				start.add(Calendar.DATE, -gapInt);
			}
		}

		String[] dateValues = {sdf.format(start.getTime()), sdf.format(end.getTime())};
		chiaveValore.put("DATA_CREAZIONE", dateValues);
*/
		if(!dataDa.equals("") && !dataA.equals(""))
		{	
			String[] dateValues = {dataDa, dataA};
			chiaveValore.put("DATA_CREAZIONE", dateValues);
		}
		else if(!dataDa.equals("") && dataA.equals(""))
		{
			String[] dateValues = {dataDa, "."};
			chiaveValore.put("DATA_CREAZIONE", dateValues);
		}
		else if(dataDa.equals("") && !dataA.equals(""))
		{
			dataDa = ".";
			if(gap!=null && !gap.isEmpty()){
				Timestamp startDate =  DateConverter.getDate(dataA, DateConverter.PATTERN_VIEW);
				dataDa = getDataDa(startDate.getTime(), gap);
			}
			
			String[] dateValues = {dataDa, dataA};
			chiaveValore.put("DATA_CREAZIONE", dateValues);
		}
		else if(dataDa.equals("") && dataA.equals("") && gap!=null && !gap.isEmpty())
		{
			long currenTimeMillis = System.currentTimeMillis();
			dataA =  DateConverter.getDate(new Timestamp(currenTimeMillis), DateConverter.PATTERN_VIEW);
			dataDa = getDataDa(currenTimeMillis, gap);

			String[] dateValues = {dataDa, dataA};
			chiaveValore.put("DATA_CREAZIONE", dateValues);
		}
		return chiaveValore;
	}
	private String getDataDa(long timeInMillis, String gap)throws Exception{
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(new Date(timeInMillis));
		int gapInt = Integer.parseInt(gap);
		startDate.add(Calendar.DATE, -gapInt);
		String dataDa = DateConverter.getDate(new Timestamp(startDate.getTimeInMillis()), DateConverter.PATTERN_VIEW);
		return dataDa;
	}
	
	private Collection<DocumentoQueryTO> query(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		Collection<DocumentoQueryTO> result = null;
		String userName = request.getRemoteUser().replace(".", "");

		String tipoTicket = globalEnv.getRadiceJob();
		String categoriaTicket = globalEnv.getSuffissoJob();

		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
		String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket);
		
		//String numRis = "5000";
		//String numRis = "6";
		String numRis = request.getParameter("numRisultati");
		String relevance = request.getParameter("relevance");
		String testo = request.getParameter("testo");
		if(testo==null || testo.trim().length()==0) testo="";
		testo = testo.trim();
		Collection<DatiQuery> datiQueryList = (Collection<DatiQuery>) request.getSession().getAttribute("listFieldvalue");
		//HashMap<String, Object> chiaveValore = makeHash(request, globalEnv, tipo);
		HashMap<String, Object> chiaveValore = makeHashPublic(datiQueryList, globalEnv, tipo);
		String baseUrl = PropertiesManager.getMyProperty("penthao.base.report.path");
		//String classeReportDescr = AppConstants.getLabelFromIndex(AppConstants.classeReportLabel, globalEnv.getClasseReport()).replace(" ", "_");
		String classeReportDescr = "Real_Time";
		baseUrl = baseUrl + "solution=" + classeReportDescr + "&path=" + userName + "/&password=password&userid=" + userName + "&name=" + tipo + "_" + userName + ".prpt";
		request.setAttribute("penthaoReportUrl", baseUrl);
		
		if(!"max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){
			D2Map d2Map = new D2Map();
			result = d2Map.Query(root, ticket, tipo.toUpperCase(), chiaveValore, numRis, relevance, testo, userName);
		}else{
			for(int i=0; i<6; i++){
				DocumentoQueryTO documentoQueryTO = new DocumentoQueryTO();
				documentoQueryTO.setTitleDoc("Titolo: " + i);
				documentoQueryTO.setSummary("Summary: " + i);
				documentoQueryTO.setReferenceDoc(i+"");
				documentoQueryTO.setMotivo("Motivo: " + i);
				documentoQueryTO.setArgomento("Argomento: " + i);
				documentoQueryTO.setSpecifica("Specifica: " + i);
				documentoQueryTO.setCodCase((i+4) + "");
				documentoQueryTO.setCodInterazione((i*5) + "");
				if(result==null) result = new ArrayList<DocumentoQueryTO>();
				result.add(documentoQueryTO);
			}
		}
		
		/*
		 * Popolamento tabelle penthao
		 */
		if(result!=null && !result.isEmpty()){
			PenthaoObject penthaoObject = new PenthaoObject();
			penthaoObject.setListaDocumenti(result);
			penthaoObject.setUser(userName);
			penthaoObject.setCategoriaTicket(categoriaTicket);
			
			PenthaoDao penthaoDao = new PenthaoDao();
			penthaoDao.managePenthaoTables(penthaoObject);
		}
		

		return result;
	}

	private JobDataDescr getValuesFirstCombo(JobDataDescr jobDataDescr) throws Exception{
		String tipoDiRappresentazione = jobDataDescr.getRadiceJob(); 
		String ambito = jobDataDescr.getAmbito();
		TroikaDao troikaDao = new TroikaDao();
		jobDataDescr.setComboValues(troikaDao.getColumnValues(null,ambito, tipoDiRappresentazione, TroikaDao.COLUMN_FIRST_VALUE_TROIKA));
		jobDataDescr.setComboCustomValues(troikaDao.getColumnValues(null, ambito, tipoDiRappresentazione, TroikaDao.COLUMN_CUSTOM_TROIKA));
		return jobDataDescr;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobDataDescr global =( JobDataDescr)request.getSession().getAttribute("globalEnvironment");
		String redirect = "struttura/query.jsp";
		operation = request.getParameter("operation");
		String rappr = request.getParameter("rapprStruttura");
		//JobDataDescr jobDataDescr = null;
		
		String msg = null;

		try{
			makeConsolePath(request);
			//jobDataDescr = getToken(request);
			if("0".equalsIgnoreCase(operation)){
				redirect="struttura/chooser.jsp";
				global = getToken(request);
				String tipoTicket = global.getRadiceJob();
				String categoriaTicket = global.getSuffissoJob();

				String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, global.getAmbito());
				String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
				String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket);
				
				String dbS = null;
				if(root.equalsIgnoreCase("Consumer"))
				{
					if(ticket.equalsIgnoreCase("FISSO"))
					{
						if(tipo.equalsIgnoreCase("INTERAZIONI"))
							dbS = "IntFissoConsumer";
						else
							dbS = "CaseFissoConsumer";
							
					}
					else
					{
						if(tipo.equalsIgnoreCase("INTERAZIONI"))
							dbS = "IntMobileConsumer";
						else
							dbS = "CaseMobileConsumer";
					}
				}
				else
				{
					if(ticket.equalsIgnoreCase("FISSO"))
					{
						if(tipo.equalsIgnoreCase("INTERAZIONI"))
							dbS = "IntFissoCorporate";
						else
							dbS = "CaseFissoCorporate";
					
					}
					else
					{
						if(tipo.equalsIgnoreCase("INTERAZIONI"))
							dbS = "IntMobileCorporate";
						else
							dbS = "CaseMobileCorporate";
					}
				}
				D2Map d2m = new D2Map();
				String totaleInutile = d2m.getNumTotaleDocumenti(dbS);
				request.getSession().setAttribute("totaleInut", totaleInutile);
			}else{
				//makeDynamicPanelField(request, global);
				if("5".equalsIgnoreCase(operation)||"".equalsIgnoreCase(operation))
				{	
					
					global = getValuesFirstCombo(global);
					request.getSession().setAttribute("pagina", "M");
					request.getSession().setAttribute("globalEnvironment", global);
					request.getSession().setAttribute("queryObjectStruttura", null);
					request.getSession().setAttribute("listaRisultatiStruttura", null);
					request.getSession().setAttribute("listFieldvalue", null);
					request.getSession().setAttribute("listFieldvaluePub", null);
					request.getSession().setAttribute("queryObjectStrutturaPub", null);
				}	
			}

			try{
				String esitoXml = null;
				Message message = new Message();
				message.setType(Message.INFO);

				if("1".equalsIgnoreCase(operation)){
					//INSERT
					request.getSession().setAttribute("pagina", "M");
					insertQuery(request, global);
					ManageThread gh = new ManageThread();
					esitoXml = gh.launchThread(AppConstants.thread.STRUTTURA, "StrutturaXmlSender");
					msg = "L'operazione è terminata correttamente.";
					if("0".equalsIgnoreCase(esitoXml)){
						message.setType(Message.WARNING);
						msg = "L'esecuzione della query NON E'ANDATA A BUON FINE si è verificato un errore interno al server. <br> I dati sono stati correttamente salvati e la query è visibile nell'elenco presente sulla pagina.";
					}
				}else if("2".equalsIgnoreCase(operation)){
					//UPDATE
					request.getSession().setAttribute("pagina", "M");
					request.getSession().setAttribute("listFieldvalue", null);
					updateQuery(request, global);
					request.setAttribute("operation","2");
				}else if("3".equalsIgnoreCase(operation)){
					//DELETE
					request.getSession().setAttribute("pagina", "M");
					deleteQuery(request, global);
					msg = "La query è stata eliminata correttamente.";
					request.setAttribute("operation","3");
				}else if("4".equalsIgnoreCase(operation)){
					//NUOVO
					//redirect = "struttura/vuoto.jsp";
					//request.setAttribute("queryObject", null);
					request.getSession().setAttribute("pagina", "M");
					request.getSession().setAttribute("queryObjectStruttura", null);
					request.getSession().setAttribute("listFieldvalue", null);
					request.getSession().setAttribute("listaRisultatiStruttura", null);
					request.setAttribute("operation","4");
				}else if("6".equalsIgnoreCase(operation)){
					//QUERY
					request.getSession().setAttribute("pagina", "M");
					global = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
					//redirect = "struttura/queryTestStruttura.jsp";
					/*if(request.getParameter("ID")!=null && request.getParameter("ID")!="")
					{
						QuerySalvateDao querySalvateDao = new QuerySalvateDao();
						QueryDatiDao queryDatiDao = new QueryDatiDao();
						QueryObject queryObject = getQueryObject(request, global);
						queryObject = querySalvateDao.getQueryById(queryObject);
						Collection<DatiQuery> listDatiQuery = queryDatiDao.getQuery(queryObject);
						request.getSession().setAttribute("listFieldvalue", listDatiQuery);
					}
					else
					{*/
						getFieldValueQuery(request, global);
					//}	
					Collection<DocumentoQueryTO> listaRisultatiStruttura = query(request, global);
					//request.setAttribute("listaRisultatiStruttura", listaRisultatiStruttura);
					//global = getValuesFirstCombo(global);
					//request.getSession().setAttribute("globalEnvironment", global);
					
					request.setAttribute("operation","6");
					request.getSession().setAttribute("listaRisultatiStruttura", listaRisultatiStruttura);
				}else if("7".equalsIgnoreCase(operation)){
					//INSERT
					msg = insertQueryPublic(request, global);
					//esitoXml = launchThread();
					request.getSession().setAttribute("pagina", "M");
					if(msg==null)
						msg = "Query pubblicata con successo.";
					/*if("0".equalsIgnoreCase(esitoXml)){
						message.setType(Message.WARNING);
						msg = "L'esecuzione della query NON E'ANDATA A BUON FINE si è verificato un errore interno al server. <br> I dati sono stati correttamente salvati e la query è visibile nell'elenco presente sulla pagina.";
					}*/
				}else if("8".equalsIgnoreCase(operation)){
					//INSERT
					request.getSession().setAttribute("listFieldvalueP", null);
					request.getSession().setAttribute("pagina", "P");
					updateQueryPublic(request, global);
					request.setAttribute("operation","8");
					/*if("0".equalsIgnoreCase(esitoXml)){
						message.setType(Message.WARNING);
						msg = "L'esecuzione della query NON E'ANDATA A BUON FINE si è verificato un errore interno al server. <br> I dati sono stati correttamente salvati e la query è visibile nell'elenco presente sulla pagina.";
					}*/
				}else if("9".equalsIgnoreCase(operation)){
					//INSERT
					request.getSession().setAttribute("listFieldvalueP", null);
					request.getSession().setAttribute("pagina", "P");
					deleteQueryPublic(request, global);
					msg = "La query è stata eliminata correttamente.";
					request.setAttribute("operation","9");
					/*if("0".equalsIgnoreCase(esitoXml)){
						message.setType(Message.WARNING);
						msg = "L'esecuzione della query NON E'ANDATA A BUON FINE si è verificato un errore interno al server. <br> I dati sono stati correttamente salvati e la query è visibile nell'elenco presente sulla pagina.";
					}*/
				}else if("10".equalsIgnoreCase(operation)){
					//INSERT
					//request.getSession().setAttribute("listFieldvalueP", null);
					request.getSession().setAttribute("pagina", "P");
					msg = savePublicInPrivate(request, global);
					if(msg==null)
						msg = "si è verificato un errore nel salvataggio della query.";
					request.setAttribute("operation","10");
					/*if("0".equalsIgnoreCase(esitoXml)){
						message.setType(Message.WARNING);
						msg = "L'esecuzione della query NON E'ANDATA A BUON FINE si è verificato un errore interno al server. <br> I dati sono stati correttamente salvati e la query è visibile nell'elenco presente sulla pagina.";
					}*/
				}else if("11".equalsIgnoreCase(operation)){
					//INSERT
					//request.getSession().setAttribute("listFieldvalueP", null);
					request.getSession().setAttribute("pagina", "P");
					global = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
					//redirect = "struttura/queryTestStruttura.jsp";
					QuerySalvateDao querySalvateDao = new QuerySalvateDao();
					QueryDatiDao queryDatiDao = new QueryDatiDao();
					QueryObject queryObject = getQueryObject(request, global);
					queryObject = querySalvateDao.getQueryByIdPublic(queryObject);
					Collection<DatiQuery> listDatiQuery = queryDatiDao.getQueryPublic(queryObject);
					request.getSession().setAttribute("listFieldvaluePub", listDatiQuery);
					request.getSession().setAttribute("queryObjectStrutturaPub", queryObject);
					
					Collection<DocumentoQueryTO> listaRisultatiStruttura = viewDetails(listDatiQuery, queryObject.getNomeQuery(), global);
					request.setAttribute("operation","11");
					request.getSession().setAttribute("listaRisultatiStruttura", listaRisultatiStruttura);
					/*if("0".equalsIgnoreCase(esitoXml)){
						message.setType(Message.WARNING);
						msg = "L'esecuzione della query NON E'ANDATA A BUON FINE si è verificato un errore interno al server. <br> I dati sono stati correttamente salvati e la query è visibile nell'elenco presente sulla pagina.";
					}*/
				}else if("12".equalsIgnoreCase(operation)){
					request.setAttribute("redirect", "excel/reportExcel.jsp");
					redirect = "ManageExcel";
				}
				message.setText(msg);
				if(msg!=null) request.setAttribute("message", message);
			}finally{
				getListQuery(request, global);
				getListQueryPublic(request, global);
			}
			
		}catch(Exception e){
			Message errorMessage = new Message();
			e.printStackTrace();
			if("0".equalsIgnoreCase(operation)) redirect = "start.jsp";
			if("5".equalsIgnoreCase(operation)) redirect = "struttura/chooser.jsp";
			errorMessage.setText(e.getMessage());
			errorMessage.setType(Message.ERROR);
			request.setAttribute("message", errorMessage);
		}finally{
			if("0".equalsIgnoreCase(operation)) request.getSession().setAttribute("globalEnvironment", global);
			request.setAttribute("rapprStruttura", rappr);
		}
		
		forward(request, response, redirect);
	}

}
