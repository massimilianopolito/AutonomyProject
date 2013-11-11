package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.JobDataDescr;
import model.Message;
import utility.DateConverter;
import utility.GenericServlet;
import utility.PropertiesManager;
import Autonomy.Bean2DMapTO;
import Autonomy.ClusterData;
import Autonomy.D2Map;
import Autonomy.DocumentoTO;

/**
 * Servlet implementation class get2DMapList
 */
public class get2DMapList extends GenericServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public get2DMapList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nome_job = request.getParameter("jobName");
		String nome_Cluster = request.getParameter("nomeCluster");
		String data = request.getParameter("data");
		String numDoc = request.getParameter("numDoc");
		
		logger.debug("Ho invocato: " + nome_job);
		
		String idolServer = null;
		
		if(nome_job.equalsIgnoreCase("MOBILE_INTERAZIONI_CONSUMER"))
			idolServer = "a";
		if(nome_job.equalsIgnoreCase("FISSO_INTERAZIONI_CONSUMER"))
			idolServer = "b";
		if(nome_job.equalsIgnoreCase("FISSO_CASE_CONSUMER"))
			idolServer = "b";
		if(nome_job.equalsIgnoreCase("MOBILE_CASE_CONSUMER"))
			idolServer = "b";
		if(nome_job.equalsIgnoreCase("CASE_CORPORATE"))
			idolServer = "c";
		if(nome_job.equalsIgnoreCase("INTERAZIONI_CORPORATE"))
			idolServer = "c";
		//MOBILE_INTERZIONI_CONSUMER

		List<DocumentoTO> result = null;
		try{
			D2Map d2Map = new D2Map();
			ArrayList<ClusterData> allClusterList = d2Map.getCuster(idolServer);
			
			//Recupero da allDataList la data autonomy che corrisponde a data
			String autonomyDate = null;
			if(allClusterList!=null){
				for(ClusterData currentCluster: allClusterList){
					String nomeJobFromCluster = currentCluster.getMapAcro();
					boolean findDate = false;
					if(nomeJobFromCluster.contains(nome_job) || 
							nomeJobFromCluster.startsWith(nome_job) ||
							nomeJobFromCluster.equalsIgnoreCase(nome_job)){
						List<String> dateFromCluster = currentCluster.getDataInizioMap();
						for(String dataFromCluster: dateFromCluster){
							if(data.equals(DateConverter.getDate(dataFromCluster))){
								autonomyDate = dataFromCluster;
								findDate = true;
								break;
							}
						}
					}
					
					if(findDate) break;
				}
			}
	
			ArrayList<Bean2DMapTO> jobDescription = null;
			if(!"max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){
				jobDescription = d2Map.view2DMap(nome_job, autonomyDate, autonomyDate);
			}else{
				jobDescription = new ArrayList<Bean2DMapTO>();
				Bean2DMapTO bean2dMapTO = new Bean2DMapTO();
				bean2dMapTO.setClusterName(nome_Cluster);
				bean2dMapTO.setResultList(new ArrayList<DocumentoTO>());
				for(int i=0; i<200; i++){
					DocumentoTO documentoTO = new DocumentoTO();
					documentoTO.setTotaleDocumenti("324738573475435");
					documentoTO.setTitleDoc("Titolo: " + i);
					documentoTO.setSummary("Summary: " + 1);
					documentoTO.setScore("12");
					bean2dMapTO.getResultList().add(documentoTO);
				}
				jobDescription.add(bean2dMapTO);
			
			}
			
			Iterator<Bean2DMapTO> iterJobDescription = jobDescription.iterator();
			Bean2DMapTO currentBean = null;
			while(iterJobDescription.hasNext()){
				currentBean = iterJobDescription.next();
				if(currentBean.getClusterName().equals(nome_Cluster)) break;
			}

			result = currentBean.getResultList();

			
		}catch (Exception e) {
			Message errorMessage = new Message();
			e.printStackTrace();
			errorMessage.setType(Message.ERROR);
			errorMessage.setText("Si e' verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			request.setAttribute("message", errorMessage);
		}finally{
			request.setAttribute("result", result);
			request.setAttribute("nomeCluster", nome_Cluster);
			request.setAttribute("data", data);
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("graph/viewerResult.jsp");
		dispatcher.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobDataDescr jobDataDescr = getToken(request);
		List<String> dateInizio = (ArrayList<String>) request.getSession().getAttribute("2DMAP_dataInizio");


		String nome_job = jobDataDescr.getTipoTicket();
		String data_da = jobDataDescr.getDataInizioSelected();
		
		boolean find = false;
		for(String currentDate: dateInizio){
			String humanDate = DateConverter.getDate(currentDate);
			if(humanDate.equalsIgnoreCase(data_da)){
				data_da = currentDate;
				find = true;
				request.setAttribute("dataDa", data_da);
				break;
			}
		}
		
		String data_a = data_da;// (String)request.getParameter("dataA");
		String redirect = "snapShot/view2DMap.jsp?dataDa=" + data_da;
		
		try{
			if(data_da==null || data_da.trim().length()==0 || "--".equalsIgnoreCase(data_da)){
				throw new Exception("E'necessario valorizzare la data.");
			}
			
			if(!find){
				throw new Exception("Non ci sono elaborazioni in data: " + data_da);
			}

			D2Map d2Map = new D2Map();
			ArrayList<Bean2DMapTO> jobDescription = null;
			if(!"max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){
				jobDescription = d2Map.view2DMap(nome_job, data_da, data_a);
			}else{
				jobDescription = new ArrayList<Bean2DMapTO>();
				Bean2DMapTO bean2dMapTO = new Bean2DMapTO();
				bean2dMapTO.setX(0);
				bean2dMapTO.setY(0);
				bean2dMapTO.setClusterName("TEST");
				bean2dMapTO.setResultList(new ArrayList<DocumentoTO>());
				for(int i=0; i<200; i++){
					DocumentoTO documentoTO = new DocumentoTO();
					documentoTO.setTitleDoc("Titolo: " + i);
					documentoTO.setSummary("Summary: " + 1);
					documentoTO.setScore("12");
					bean2dMapTO.getResultList().add(documentoTO);
				}
				jobDescription.add(bean2dMapTO);
			
			}

			if(jobDescription == null) throw new Exception("L'elaborazione: '" + nome_job + "' per la data: '"+ data_da +"'non e' valida.");
			request.getSession().setAttribute("jobDescription", jobDescription);
		}catch (Exception e) {
			Message errorMessage = new Message();
			e.printStackTrace();
			errorMessage.setType(Message.ERROR);
			errorMessage.setText("Si e' verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			redirect = "getJobList";
			request.setAttribute("message", errorMessage);
		}finally{
			request.setAttribute("jobDataDescr", jobDataDescr);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(redirect);
		dispatcher.forward(request, response);
	}

}
