package servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ConfigurationObject;
import model.DatiQuery;
import model.JobDataDescr;
import model.Message;
import model.PenthaoObject;
import model.QueryObject;
import utility.AppConstants;
import utility.GenericServlet;
import utility.PropertiesManager;
import Autonomy.D2Map;
import Autonomy.DocumentoQueryTO;
import dao.ConfigurazioneQueryDao;
import dao.PenthaoDao;
import dao.QueryDatiDao;
import dao.QuerySalvateDao;
import dao.TroikaDao;

/**
 * Servlet implementation class ManageRealTime
 */
public class ManageRealTime extends GenericServlet {
	private static final long serialVersionUID = 1L;
	protected String operation = null;
  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageRealTime() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	protected Collection<DatiQuery> getDynamicFieldValues(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		Collection<DatiQuery> listDynamicField = null;
		String tipoTicket = globalEnv.getRadiceJob();
		String categoriaTicket = globalEnv.getSuffissoJob();

		ConfigurazioneQueryDao configurazioneQueryDao = new ConfigurazioneQueryDao();
		Collection<ConfigurationObject> objectsOnPage = configurazioneQueryDao.getConfig(tipoTicket, categoriaTicket);
		if(objectsOnPage!=null){
			for(ConfigurationObject currentObject: objectsOnPage){
				String id = currentObject.getID();
				String nomeColonna = currentObject.getNomeColonna().trim();
				String currentValue = request.getParameter(nomeColonna);
				
				if(currentValue!=null && currentValue.trim().length()!=0 && !"--".equalsIgnoreCase(currentValue)){
					if(listDynamicField == null) listDynamicField = new ArrayList<DatiQuery>();
					DatiQuery datiQuery = new DatiQuery();
					datiQuery.setIdCampo(id);
					datiQuery.setValoreCampo(currentValue);
					listDynamicField.add(datiQuery);
				}
			}
		}		
		
		return listDynamicField;
	}

	private Map<String, String> getIdCampoValore(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{ 
		Map<String, String> idCampoValore = new HashMap<String, String>();
		if(!"4".equalsIgnoreCase(operation)){
			Collection<DatiQuery> fieldValues = getDynamicFieldValues(request, globalEnv);;
			if(request.getParameter("ID")!=null && request.getParameter("ID").trim().length()>0  && fieldValues == null){
				QueryDatiDao queryDatiDao = new QueryDatiDao();
				QueryObject queryObject = new QueryObject();
				queryObject.setID(request.getParameter("ID"));
				fieldValues = queryDatiDao.getQuery(queryObject);
			}
			if(fieldValues!=null){
				for(DatiQuery currentData: fieldValues){
					String idCampo = currentData.getIdCampo();
					String valore = currentData.getValoreCampo();
					idCampoValore.put(idCampo, valore);
				}
			}
		}
		return idCampoValore;
	}

	protected void makeDynamicPanelField(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		Map<String, String> idCampoValore = getIdCampoValore(request, globalEnv);
		
		ConfigurazioneQueryDao configurazioneQueryDao = new ConfigurazioneQueryDao();
		String ambito = globalEnv.getAmbito();
		String radice = globalEnv.getRadiceJob();
		String suffisso = globalEnv.getSuffissoJob();
		
		Collection<String> objLeft = new ArrayList<String>();
		Collection<String> objRight = new ArrayList<String>();
		
		Collection<ConfigurationObject> objectsOnPage = configurazioneQueryDao.getConfig(radice, suffisso);
		if(objectsOnPage!=null){
			String troikaFirstValue = null;
			String troikaSecondValue = null;
			for(ConfigurationObject currentObject: objectsOnPage){
				int posizionePagina = currentObject.getColonnaPagina();
				String idCampo = currentObject.getID();
				String tipoCampo = currentObject.getTipoCampo();
				String nomeColonna = currentObject.getNomeColonna();
				String label = currentObject.getLabel();
				String valore = currentObject.getValore();
				String elementOnPage = "";
				
				String valueOnDB = idCampoValore.get(idCampo)==null?"":idCampoValore.get(idCampo);
				
				if(AppConstants.tipoDiCampo.STRING.equalsIgnoreCase(tipoCampo)){
					elementOnPage = elementOnPage + "<input  type=\"text\" name=\""+ nomeColonna + "\" id=\"" + nomeColonna + "\" maxlength=\"200\" value=\""+ valueOnDB +"\">";
				
				}else if(AppConstants.tipoDiCampo.CALENDAR.equalsIgnoreCase(tipoCampo)){
					elementOnPage = elementOnPage + "<input  type=\"text\" name=\""+ nomeColonna + "\" id=\"" + nomeColonna + "\" readonly=\"readonly\" value=\""+ valueOnDB +"\"/>";
				
				}else if(AppConstants.tipoDiCampo.COMBO.equalsIgnoreCase(tipoCampo)){
					elementOnPage = elementOnPage + "<select name=\""+ nomeColonna + "\" id=\"" + nomeColonna + "\" >";
					elementOnPage = elementOnPage + "<option value=\"--\" >- Selezionare - </option>";
					String[] values = valore.split("\\,");
					for(int i =0; i<values.length; i++){
						String currentOptionValue = values[i];
						String selected = currentOptionValue.equalsIgnoreCase(valueOnDB)?"selected=\"selected\"":"";
						elementOnPage = elementOnPage + "<option value=\"" + currentOptionValue + "\" " + selected +" >" + currentOptionValue + "</option>";
					}
					elementOnPage = elementOnPage + "</select>";
				
				}else if(AppConstants.tipoDiCampo.TRIPLETTA.equalsIgnoreCase(tipoCampo)){
					String nomeCampoTripletta = "";
					elementOnPage = elementOnPage + "<select name=\""+ nomeColonna +"\" id=\"[NCT]\" >";
					elementOnPage = elementOnPage + "<option value=\"--\" >- Selezionare - </option>";
					
					TroikaDao troikaDao = new TroikaDao();
					if("1".equalsIgnoreCase(valore)){
						nomeCampoTripletta = "first";
						troikaFirstValue = valueOnDB;
						Collection<String> values = troikaDao.getColumnValues(null,ambito, radice);
						if(values!=null){
							for(String currentOptionValue: values){
								String selected = currentOptionValue.equalsIgnoreCase(troikaFirstValue)?"selected=\"selected\"":"";
								elementOnPage = elementOnPage + "<option value=\"" + currentOptionValue + "\" " + selected +">" + currentOptionValue + "</option>";
							}
						}
					}
					
					if("2".equalsIgnoreCase(valore)){
						nomeCampoTripletta = "second";
						troikaSecondValue = valueOnDB;
						if(troikaFirstValue!=null && troikaSecondValue!=null){
							Collection<String> values = troikaDao.getColumnValues(new String[]{troikaFirstValue},ambito, radice);
							if(values!=null){
								for(String currentOptionValue: values){
									String selected = currentOptionValue.equalsIgnoreCase(troikaSecondValue)?"selected=\"selected\"":"";
									elementOnPage = elementOnPage + "<option value=\"" + currentOptionValue + "\" " + selected +">" + currentOptionValue + "</option>";
								}
							}
						}
					}

					if("3".equalsIgnoreCase(valore)){
						nomeCampoTripletta = "third";
						if(troikaSecondValue!=null && valueOnDB!=null){
							Collection<String> values = troikaDao.getColumnValues(new String[]{troikaFirstValue, troikaSecondValue},ambito, radice);
							if(values!=null){
								for(String currentOptionValue: values){
									String selected = currentOptionValue.equalsIgnoreCase(valueOnDB)?"selected=\"selected\"":"";
									elementOnPage = elementOnPage + "<option value=\"" + currentOptionValue + "\" " + selected +">" + currentOptionValue + "</option>";
								}
							}
						}
					}
					
					elementOnPage = elementOnPage.replace("[NCT]", nomeCampoTripletta) + "</select>";
				}
				
				elementOnPage = "<label>" + label + "</label>" + elementOnPage;
				elementOnPage = elementOnPage + "<br/>";
				if(posizionePagina==1){
					objLeft.add(elementOnPage);
				}else{
					objRight.add(elementOnPage);
				}
			}
		}
		
		request.setAttribute("objLeft", objLeft);
		request.setAttribute("objRight", objRight);
	}
	
	protected HashMap<String, Object> getDynamicFieldFromRequest(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
			HashMap<String, Object> chiaveValore = new HashMap<String, Object>();
			String tipoTicket = globalEnv.getRadiceJob();
			String categoriaTicket = globalEnv.getSuffissoJob();
	
			ConfigurazioneQueryDao configurazioneQueryDao = new ConfigurazioneQueryDao();
			Collection<ConfigurationObject> objectsOnPage = configurazioneQueryDao.getConfig(tipoTicket, categoriaTicket);
			if(objectsOnPage!=null){
				for(ConfigurationObject currentObject: objectsOnPage){
					String nomeColonna = currentObject.getNomeColonna().trim();
					String currentValue = request.getParameter(nomeColonna);
					String tipoCampo = currentObject.getTipoCampo();
	
					if(AppConstants.tipoDiCampo.CALENDAR.equalsIgnoreCase(tipoCampo)){
	/*					String[] tokens = currentValue.split("\\/");
						currentValue = tokens[2] + "-" + tokens[1] + "-" + tokens[0];
						
	*/
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
						logger.debug("Aggiungo: " + nomeColonna + " - " + currentValue);
						chiaveValore.put(nomeColonna, currentValue);
					}
				}
			}
			return chiaveValore;
		}

		
	private void launchPenthao() throws Exception{
		String endPoint = PropertiesManager.getMyProperty("penthao.servletEndPoint");
		String programPath = PropertiesManager.getMyProperty("penthao.commandPath");
		if(endPoint!=null && programPath!=null){
			HttpURLConnection connection = null;  
			URL url = new URL(endPoint);
			String charset = "UTF-8";
			String query = String.format("path=%s", URLEncoder.encode(programPath, charset));

			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(endPoint.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  
					
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			wr.writeBytes (query);
			wr.flush ();
			wr.close ();
			
			connection.connect();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
			    logger.debug(decodedString);
			}
			in.close();

		}
	}
	
	private Collection<DocumentoQueryTO> query(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		Collection<DocumentoQueryTO> result = null;
		String userName = request.getRemoteUser().replace(".", "");

		String tipoTicket = globalEnv.getRadiceJob();
		String categoriaTicket = globalEnv.getSuffissoJob();

		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
		String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket);
		
		String numRis = request.getParameter("numRisultati");
		String relevance = request.getParameter("relevance");
		String testo = request.getParameter("testo");
		if(testo==null || testo.trim().length()==0) testo="";
		testo = testo.trim();
		HashMap<String, Object> chiaveValore = getDynamicFieldFromRequest(request, globalEnv);
		
		/*
		 * Creazione URL per report Penthao
		 */
		String baseUrl = PropertiesManager.getMyProperty("penthao.base.report.path");
		String classeReportDescr = AppConstants.getLabelFromIndex(AppConstants.classeReportLabel, globalEnv.getClasseReport()).replace(" ", "_");
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
	
	private void insertQuery(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		QuerySalvateDao querySalvateDao = new QuerySalvateDao();
		QueryObject queryObject = getQueryObject(request, globalEnv);
		Collection<DatiQuery> listDatiQuery = getDynamicFieldValues(request, globalEnv);
		queryObject = querySalvateDao.manageQuerySalvate(queryObject, listDatiQuery);
		request.setAttribute("queryObject", queryObject);
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
		
		request.setAttribute("queryObject", queryObject);
		
		if("3".equalsIgnoreCase(operation) && queryObject.getID() == null){
			if(queryObject.getNomeQuery()==null || queryObject.getNomeQuery().trim().length()==0) throw new Exception("Il nome della query è obbligatorio.");
			QuerySalvateDao querySalvateDao = new QuerySalvateDao();
			querySalvateDao.checkQueryNameByUser(queryObject);
		}
		
		return queryObject;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//JobDataDescr global =( JobDataDescr)request.getSession().getAttribute("globalEnvironment");;
		JobDataDescr global = null;
		String redirect = "realTime/query.jsp";
		operation = request.getParameter("operation");
		String nome = request.getParameter("nomeQuery");
		
		String msg = null;
		Message message = new Message();
		message.setType(Message.INFO);
		try{
			//global = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
			
			if("0".equalsIgnoreCase(operation)){
				global = getToken(request);
				makeDynamicPanelField(request, global);
				request.getSession().setAttribute("listaRisultati", null);
			}else if("1".equalsIgnoreCase(operation)){
				global = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
				redirect = "realTime/queryResult.jsp";
				Collection<DocumentoQueryTO> listaRisultati = query(request, global);
				request.setAttribute("listaRisultati", listaRisultati);
				request.getSession().setAttribute("listaRisultati", listaRisultati);
			}else if("2".equalsIgnoreCase(operation)){
				request.setAttribute("redirect", "excel/reportExcel.jsp");
				redirect = "ManageExcel";
			}
			else if("3".equalsIgnoreCase(operation)){
				//global = getToken(request);
				global =( JobDataDescr)request.getSession().getAttribute("globalEnvironment");
				if(!nome.equals(""))
				{	
					insertQuery(request, global);
					msg = "La query è stata salvata con successo.";
				}
				else
					msg = "Il nome della query è obbligatorio.";
				
				message.setText(msg);
				if(msg!=null) request.setAttribute("message", message);
				if(request.getSession().getAttribute("listaRisultati")!=null)
				{	
					redirect = "realTime/queryResult.jsp";
					/*Collection<DocumentoQueryTO> listaRisultati = query(request, global);
					request.setAttribute("listaRisultati", listaRisultati);
					request.getSession().setAttribute("listaRisultati", listaRisultati);*/
				}
				//redirect = "ManageExcel";
			}
			
		}catch(Exception e){
			Message errorMessage = new Message();
			e.printStackTrace();
			if("0".equalsIgnoreCase(operation)) redirect = "start.jsp";
			errorMessage.setText("Si è verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			errorMessage.setType(Message.ERROR);
			request.setAttribute("message", errorMessage);
		}finally{
			if("0".equalsIgnoreCase(operation)) request.getSession().setAttribute("globalEnvironment", global);
		}
		
		forward(request, response, redirect);
	}

}
