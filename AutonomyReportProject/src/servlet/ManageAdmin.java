package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Autonomy.D2Map;
import Autonomy.DocumentoTO;

import dao.TroikaDao;

import model.JobDataDescr;
import model.Message;
import model.Troika;
import utility.AppConstants;
import utility.GenericServlet;
import utility.TroikaComparator;

/**
 * Servlet implementation class ManageAdmin
 */
public class ManageAdmin extends GenericServlet {
	private static final long serialVersionUID = 1L;
//	private int TYPE_DESCRIZIONE = 0;
//	private int TYPE_RISPOSTA = 1;
	private String type;

    /**
     * Default constructor. 
     */
    public ManageAdmin() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private JobDataDescr getValuesFirstCombo(JobDataDescr jobDataDescr) throws Exception{
		String tipoDiRappresentazione = jobDataDescr.getRadiceJob(); 
		String ambito = jobDataDescr.getAmbito();
		TroikaDao troikaDao = new TroikaDao();
		jobDataDescr.setComboValues(troikaDao.getColumnValues(null,ambito, tipoDiRappresentazione, TroikaDao.COLUMN_FIRST_VALUE_TROIKA));
		jobDataDescr.setComboCustomValues(troikaDao.getColumnValues(null, ambito, tipoDiRappresentazione, TroikaDao.COLUMN_CUSTOM_TROIKA));
		return jobDataDescr;
	}
	
	private void putSearchCriteriaInRequest(HttpServletRequest request) throws Exception{
		String searchCriteria = "searchCriteria";
		if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R)){
			searchCriteria = "searchCriteriaGR";
		}
		Map<String, String> numColValue = (Map<String, String>) request.getSession().getAttribute(searchCriteria);
		if(numColValue!=null){
			request.setAttribute("first", numColValue.get(TroikaDao.COLUMN_FIRST_VALUE_TROIKA));
			request.setAttribute("second", numColValue.get(TroikaDao.COLUMN_SECOND_VALUE_TROIKA));
			request.setAttribute("third", numColValue.get(TroikaDao.COLUMN_THIRD_VALUE_TROIKA));
			request.setAttribute("custom", numColValue.get(TroikaDao.COLUMN_CUSTOM_TROIKA));
		}
	}
	
	private void getLista(HttpServletRequest request) throws Exception{
		String tableSessionName = "resultTable";
		String tableRequestName = "listResult";
		String searchCriteria = "searchCriteria";
		
		if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R)){
			tableSessionName = "resultTableGR";
			tableRequestName = "listResultGR";
			searchCriteria = "searchCriteriaGR";
		}
		
		JobDataDescr jobDataDescr = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
		TroikaDao troikaDao = new TroikaDao();
		String ambito = jobDataDescr.getAmbito();
		String tipologia = jobDataDescr.getRadiceJob(); 

		String first = request.getParameter("first")!=null?request.getParameter("first"):(String)request.getAttribute("first");
		if("--".equalsIgnoreCase(first))
			first = null;
		String second = request.getParameter("second")!=null?request.getParameter("second"):(String)request.getAttribute("second");
		if("--".equalsIgnoreCase(second))
			second = null;
		String third = request.getParameter("third")!=null?request.getParameter("third"):(String)request.getAttribute("third");
		if("--".equalsIgnoreCase(third))
			third = null;
		String custom = request.getParameter("custom")!=null?request.getParameter("custom"):(String)request.getAttribute("custom");
		if("--".equalsIgnoreCase(custom))
			custom = null;
		
		Map<String, String> numColValue = new HashMap<String, String>();
		
		if(first!=null) numColValue.put(TroikaDao.COLUMN_FIRST_VALUE_TROIKA, first.trim());
		if(second!=null) numColValue.put(TroikaDao.COLUMN_SECOND_VALUE_TROIKA, second.trim());
		if(third!=null) numColValue.put(TroikaDao.COLUMN_THIRD_VALUE_TROIKA, third.trim());
		if(custom!=null) numColValue.put(TroikaDao.COLUMN_CUSTOM_TROIKA, custom.trim());

		Collection<Troika> listResult = troikaDao.getAllColumnValues( numColValue,  ambito, tipologia);
		request.getSession().setAttribute(searchCriteria, numColValue);

		ArrayList lr = null;
		if(listResult!=null){
			Map<String, Troika> currentResult = new HashMap<String, Troika>();
			for(Troika troika: listResult){
				currentResult.put(troika.toString(), troika);
				request.getSession().setAttribute(tableSessionName, currentResult);
				
			}

			lr = new ArrayList(listResult);
			Collections.sort(lr, new TroikaComparator());
		}
		

		request.setAttribute(tableRequestName, lr);
	}
	
	private void saveAll(HttpServletRequest request) throws Exception{
		String baseFieldName = "descrizione_";
		String baseCaratterizzazioneName = "caratterizzazione_";
		String troikasCodeName = "codice";
		String tableSessionName = "resultTable";
		
		if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R)){
			baseFieldName = "risposta_";
			troikasCodeName = "codiceGR";
			tableSessionName = "resultTableGR";
		}

		String[] troikas = request.getParameterValues(troikasCodeName);
		Map<String, Troika> currentResult  = (Map<String, Troika>) request.getSession().getAttribute(tableSessionName);
		//Collection<Troika> listResult = new ArrayList<Troika>();
		
		JobDataDescr jobDataDescr = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
		TroikaDao troikaDao = new TroikaDao();
		String ambito = jobDataDescr.getAmbito();
		String tipologia = jobDataDescr.getRadiceJob(); 

		Troika currValue = null;
		String caratterizzazione = null;
		for(int i=0; i<troikas.length; i++){
			String code = troikas[i];
			String value = request.getParameter(baseFieldName + code);
			currValue = currentResult.get(code);
			if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_D)){
				currValue.setDescrizione(value);
				if(currValue.isFather()){
					caratterizzazione = request.getParameter(baseCaratterizzazioneName + code);
					if("--".equalsIgnoreCase(caratterizzazione)) caratterizzazione = null;
				}
				currValue.setCaratterizzazione(caratterizzazione);
				troikaDao.updateCampiRecord(currValue, ambito, tipologia);
			}else if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R)){
				currValue.setRisposta(value);
				troikaDao.updateRispostaRecord(currValue, ambito, tipologia);
			}
			//listResult.add(currValue);
		}

		createSon(request, currValue, troikas.length);

		putSearchCriteriaInRequest(request);
		getLista(request);
	}

	private void save(HttpServletRequest request) throws Exception{
		String baseFieldName = "descrizione_";
		String baseCaratterizzazioneName = "caratterizzazione_";
		String triplettaCorrenteName = "triplettaCorrente";
		String troikasCodeName = "codice";
		String tableSessionName = "resultTable";
		
		if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R)){
			baseFieldName = "risposta_";
			triplettaCorrenteName = "triplettaCorrenteGR";
			troikasCodeName = "codiceGR";
			tableSessionName = "resultTableGR";
		}
		
		String[] troikas = request.getParameterValues(troikasCodeName);
		String troikaCorrente = request.getParameter(triplettaCorrenteName);
		
		Map<String, Troika> currentResult  = (Map<String, Troika>) request.getSession().getAttribute(tableSessionName);
		//Collection<Troika> listResult = new ArrayList<Troika>();

		JobDataDescr jobDataDescr = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
		TroikaDao troikaDao = new TroikaDao();
		String ambito = jobDataDescr.getAmbito();
		String tipologia = jobDataDescr.getRadiceJob(); 

		Troika currValue = null;
		for(int i=0; i<troikas.length; i++){
			String code = troikas[i];
			String value = request.getParameter(baseFieldName + code);
			String caratterizzazione = request.getParameter(baseCaratterizzazioneName + code);
			if("--".equalsIgnoreCase(caratterizzazione)) caratterizzazione = null;
			currValue = currentResult.get(code);
			if(code.equalsIgnoreCase(troikaCorrente)){
				if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_D)){
					currValue.setDescrizione(value);
					currValue.setCaratterizzazione(caratterizzazione);
					troikaDao.updateCampiRecord(currValue, ambito, tipologia);
				}else if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R)){
					currValue.setRisposta(value);
					troikaDao.updateRispostaRecord(currValue, ambito, tipologia);
				}
				break;
			}
			//listResult.add(currValue);
		}
		
 		if("custom".equalsIgnoreCase(troikaCorrente)) createSon(request, currValue, troikas.length);

 		putSearchCriteriaInRequest(request);
 		getLista(request);
	}
	
	private void createSon(HttpServletRequest request, Troika currentFather, int length )throws Exception{
		String customName = request.getParameter("customName");
		String customDesc = request.getParameter("customDescr");
		String idTroika = request.getParameter("uid");
		if(customName!=null && customName.trim().length()!=0){
			JobDataDescr jobDataDescr = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
			TroikaDao troikaDao = new TroikaDao();
			String ambito = jobDataDescr.getAmbito();
			String tipologia = jobDataDescr.getRadiceJob(); 

			Troika troikaCustom = new Troika();
			troikaCustom.setID(troikaDao.getKey(idTroika, ambito, tipologia));
			troikaCustom.setCustom(customName);
			troikaCustom.setDescrizione(customDesc);
			troikaCustom.setCaratterizzazione(currentFather.getCaratterizzazione());
			troikaCustom.setFirstValue(currentFather.getFirstValue());
			troikaCustom.setSecondValue(currentFather.getSecondValue());
			troikaCustom.setThirdValue(currentFather.getThirdValue());
			troikaCustom.setCodTripletta(currentFather.getCodTripletta());
			troikaCustom.setAttiva(currentFather.getAttiva());

			troikaDao.insertDescrizioneRecord(troikaCustom, ambito, tipologia);
		}else if(customDesc!=null && customDesc.trim().length()!=0){
			throw new Exception("Nel campo agguntivo il nome del campo è obbigatorio quando la descrizione è valorizzata!");
		}
	}
	
	private void delete(HttpServletRequest request) throws Exception{
		String triplettaCorrenteName = "triplettaCorrente";
		String troikasCodeName = "codice";
		String tableSessionName = "resultTable";
		
		if(type.equalsIgnoreCase(AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R)){
			triplettaCorrenteName = "triplettaCorrenteGR";
			troikasCodeName = "codiceGR";
			tableSessionName = "resultTableGR";
		}

		String[] troikas = request.getParameterValues(troikasCodeName);
		String troikaCorrente = request.getParameter(triplettaCorrenteName);
		
		Map<String, Troika> currentResult  = (Map<String, Troika>) request.getSession().getAttribute(tableSessionName);
		//Collection<Troika> listResult = new ArrayList<Troika>();

		JobDataDescr jobDataDescr = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
		TroikaDao troikaDao = new TroikaDao();
		String ambito = jobDataDescr.getAmbito();
		String tipologia = jobDataDescr.getRadiceJob(); 

		Troika currValue = null;
		for(int i=0; i<troikas.length; i++){
			String code = troikas[i];
			currValue = currentResult.get(code);
			if(code.equalsIgnoreCase(troikaCorrente)){
				troikaDao.deleteCustom(currValue, ambito, tipologia);
				break;
			}
			//listResult.add(currValue);
		}

		putSearchCriteriaInRequest(request);
		getLista(request);
	}

	
	private void queryTraining(HttpServletRequest request) throws Exception{
		List<DocumentoTO> listResult = new ArrayList<DocumentoTO>();
		String text = request.getParameter("testo");
		String relevance = request.getParameter("relevance");
		String numRisultati = request.getParameter("numRisultati");
		String caratterizzazione = request.getParameter("caratterizzazione");
		if("--".equalsIgnoreCase(caratterizzazione)) caratterizzazione = null;

		JobDataDescr globalEnv = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, globalEnv.getRadiceJob()).toUpperCase();

		if(text==null || text.trim().length()==0) throw new Exception("Il testo di ricerca non è valido.");
		
		D2Map d2Map = new D2Map();
		listResult = d2Map.SuggestOnText(root, ticket, relevance, numRisultati, text, caratterizzazione);
		
		request.setAttribute("listResult", listResult);
	}

	private void makeCompleteList(HttpServletRequest request) throws Exception{
		JobDataDescr jobDataDescr = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
		String ambito = jobDataDescr.getAmbito();
		String tipologia = jobDataDescr.getRadiceJob(); 

		Collection<Troika> listResult = new ArrayList<Troika>();
		TroikaDao troikaDao = new TroikaDao();
		listResult = troikaDao.getAllColumnValues(null, ambito, tipologia);
		request.getSession().setAttribute("listaRisultati", listResult);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobDataDescr jobDataDescr = null;
		String redirect = "/pages/ontologyTrainer/ontologyTab.jsp";
		String operation = null;
		String msg = null;
		try{
			jobDataDescr = getToken(request);
			//if(jobDataDescr==null) 
			String admin = jobDataDescr.getClasseReport();
			operation = jobDataDescr.getOperation();
			
			if(AppConstants.ClasseReport.ONTOLOGYTRAINER.equalsIgnoreCase(admin)){
				type = request.getParameter("typeOfField");
				if(operation == null || "0".equalsIgnoreCase(operation)){
					request.getSession().setAttribute("globalEnvironment", jobDataDescr);
					jobDataDescr = getValuesFirstCombo(jobDataDescr);
					//getLista(request);
					request.setAttribute("jobDataDescr", jobDataDescr);
				}else if("1".equalsIgnoreCase(operation)){
					getLista(request);
					redirect = "tripletta/gestioneResult.jsp";
				}else if("2".equalsIgnoreCase(operation)){
					redirect = "tripletta/gestioneResult.jsp";
					msg = "Le descrizioni sono state salvate correttamente.";
					saveAll(request);
				}else if("3".equalsIgnoreCase(operation)){
					redirect = "tripletta/gestioneResult.jsp";
					msg = "La descrizione è stata salvata correttamente.";
					save(request);
				}else if("4".equalsIgnoreCase(operation)){
					redirect = "tripletta/queryTrainingResult.jsp";
					queryTraining(request);
				}else if("5".equalsIgnoreCase(operation)){
					redirect = "tripletta/gestioneResult.jsp";
					msg = "La cancellazione è terminata correttamente.";
					delete(request);
				}else if("6".equalsIgnoreCase(operation)){
					redirect = "ManageExcel";
					request.setAttribute("redirect", "excel/troikaExcel.jsp");
					request.setAttribute("nomeFile", "Tripletta");
					makeCompleteList(request);
				}else if("7".equalsIgnoreCase(operation)){
					getLista(request);
					redirect = "tripletta/gestioneRispostaResult.jsp";
				}else if("8".equalsIgnoreCase(operation)){
					redirect = "tripletta/gestioneRispostaResult.jsp";
					msg = "La risposta è stata salvata correttamente.";
					save(request);
				}else if("9".equalsIgnoreCase(operation)){
					redirect = "tripletta/gestioneRispostaResult.jsp";
					msg = "Le risposte sono state salvate correttamente.";
					saveAll(request);
				}				
				if(msg!=null){
					Message message = new Message();
					message.setText(msg);
					message.setType(Message.INFO);
					request.setAttribute("message", message);					
				}
			}

		}catch(Exception e){
			Message errorMessage = new Message();
			e.printStackTrace();
			if("0".equalsIgnoreCase(operation) || "1".equalsIgnoreCase(operation)) redirect = "start.jsp";
			errorMessage.setText("Si è verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			errorMessage.setType(Message.ERROR);
			request.setAttribute("message", errorMessage);
		}finally{
			if("0".equalsIgnoreCase(operation)) request.getSession().setAttribute("globalEnvironment", jobDataDescr);
		}
		
		forward(request, response, redirect);

	}

}
