package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dao.SnapShotDao;
import model.JobDataDescr;
import model.Message;
import model.SnapShot;
import utility.ConnectionManager;
import utility.DateConverter;
import utility.GenericServlet;
import utility.PropertiesManager;

/**
 * Servlet implementation class ManageGraph
 */
public class ManageGraph extends GenericServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ManageGraph() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("CHIAMATA DAL GRAFICO!");
		String nome_job = request.getParameter("nomeJob");
		String data_da = request.getParameter("dataDa");
		String data_a =request.getParameter("dataA");
		try{
			JSONObject json = makeDataByGraph(nome_job, data_da, data_a);
			
			logger.debug(json.toJSONString());
			request.setCharacterEncoding("UTF8");
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write(json.toString());

		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private Map<Integer, Collection<SnapShot>> makeFamily(Collection<SnapShot> records) throws Exception{
		 Map<Integer, Collection<SnapShot>> result = new TreeMap<Integer, Collection<SnapShot>>();
		 for(SnapShot snapShot: records){
			 Integer familyId = Integer.valueOf(snapShot.getKey());
			 if(!result.containsKey(familyId)) result.put(familyId, new ArrayList<SnapShot>());
			 result.get(familyId).add(snapShot);
		 }
		 
		 return result;
	}

	private Timestamp getDateRolled(Timestamp date, int amount)throws Exception{
		Timestamp rolledDate = date;
		Calendar rolledDateCalendar = GregorianCalendar.getInstance();
		rolledDateCalendar.setTime(new Date(date.getTime()));
		rolledDateCalendar.add(Calendar.DATE, amount);
		rolledDate = new Timestamp(rolledDateCalendar.getTimeInMillis());
		return rolledDate;
	}
	
	private Timestamp getNextDate(Timestamp date) throws Exception{
		Timestamp nextDate = getDateRolled(date, 1);
		return nextDate;
	}

	private Timestamp getBeforeDate(Timestamp date) throws Exception{
		Timestamp nextDate = getDateRolled(date, -1);
		return nextDate;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject createNode(Timestamp date, String nomeCluster, long numdoc){
		JSONObject node = new JSONObject();
		node.put("name", DateConverter.getDate(date, DateConverter.PATTERN_VIEW) + " " + nomeCluster);
		node.put("date", DateConverter.getDate(date, DateConverter.PATTERN_VIEW));
		node.put("numdoc", numdoc);
		node.put("shortname", nomeCluster.length()>10?nomeCluster.substring(0, 9)+"...":nomeCluster);
		return node;
	}

	@SuppressWarnings("unchecked")
	private JSONObject createLink(Timestamp sourceDate, String sourceNomeCluster, Timestamp targetDate, String targetNomeCluster, long targetNumdoc){
		JSONObject link = new JSONObject();
		link.put("source", DateConverter.getDate(sourceDate, DateConverter.PATTERN_VIEW) + " " + sourceNomeCluster);
		link.put("target", DateConverter.getDate(targetDate, DateConverter.PATTERN_VIEW) + " " + targetNomeCluster);
		link.put("value", targetNumdoc);
		return link;
	}
	
	private void completeOrphan(){
		
	}

	@SuppressWarnings("unchecked")
	private JSONObject makeDataByGraph(String nome_job, String data_da, String data_a ) throws Exception{
		List<String> dayByDay = DateConverter.getDates(data_da, data_a, null);
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = cm.getConnection(true);
		//Map<Timestamp,  Map<Integer, Collection<SnapShot>>> snapByDate = new TreeMap<Timestamp,  Map<Integer, Collection<SnapShot>>>();
		JSONObject result = new JSONObject();

		JSONArray nodes = new JSONArray();
		JSONArray links = new JSONArray();

		try{
			SnapShotDao snapShotDao = new SnapShotDao(connection);
			
			for(String day: dayByDay){
				Timestamp dayTime = DateConverter.getDate(day, DateConverter.PATTERN_DB_TIMESTAMP, DateConverter.PATTERN_VIEW);
				
				Timestamp nextDate = getNextDate(dayTime);
				String nextDatePW = DateConverter.getDate(nextDate, DateConverter.PATTERN_VIEW);
				
				Timestamp beforeDate = getBeforeDate(dayTime);
				String beforeDatePW = DateConverter.getDate(beforeDate, DateConverter.PATTERN_VIEW);
 
				Collection<SnapShot> recordsInDate = snapShotDao.getRecordByDate(dayTime, dayTime, nome_job);
				
				Set<String> clusterCache = new HashSet<String>();
				Map<String, SnapShot> singleCache = new HashMap<String, SnapShot>();
				Map<String, SnapShot> orphanCache = new HashMap<String, SnapShot>();
				
				for(SnapShot currentSnapShot: recordsInDate){
					String nomeCluster = currentSnapShot.getClusterName();
					int familyId = currentSnapShot.getKey();
					int idLegame = currentSnapShot.getIdLegame();
					if(!clusterCache.contains(nomeCluster)){
						/**
						 * L'elemento corrente è un cluster mai trattato 
						 * quindi deve essere aggiunto all'elenco dei nodi.
						 */
						if(dayByDay.contains(nextDatePW)) singleCache.put(nomeCluster, currentSnapShot);
						clusterCache.add(nomeCluster);
						JSONObject node =createNode(currentSnapShot.getDate(), nomeCluster, currentSnapShot.getNumDoc());
						nodes.add(node);
						if(dayByDay.contains(beforeDatePW)){
							/**
							 * Se la data precedente a day ricade in dayByDay questo elemento potrebbe essere
							 * orfano di un padre diretto.
							 */
							SnapShot linkFatherSnapShot = new SnapShot();
							linkFatherSnapShot.setDate(beforeDate);
							linkFatherSnapShot.setKey(familyId);
							linkFatherSnapShot.setIdLegame(idLegame);
							linkFatherSnapShot.setSnapShot(nome_job);
							linkFatherSnapShot = snapShotDao.getLink(linkFatherSnapShot);
							if(nomeCluster.equalsIgnoreCase("mese verso tutti, attivazione gratuita, bonus")){
								System.err.println();
							}
							if(linkFatherSnapShot.getClusterName()==null){
								orphanCache.put(nomeCluster, currentSnapShot);
							}
						}
						
					}else if(dayByDay.contains(nextDatePW)){
						/**
						 * L'elemento corrente indica che il cluster corrente ha un figlio
						 * che ricade nell'intervallo di tempo indicato in dayByDay 
						 */
						SnapShot linkSnapData = new SnapShot();
						linkSnapData.setDate(nextDate);
						linkSnapData.setKey(familyId);
						linkSnapData.setIdLegame(idLegame);
						linkSnapData.setSnapShot(nome_job);
						linkSnapData = snapShotDao.getLink(linkSnapData);
						
						JSONObject link = createLink(currentSnapShot.getDate(), nomeCluster, 
													 linkSnapData.getDate(), linkSnapData.getClusterName(), linkSnapData.getNumDoc());
						links.add(link);
						singleCache.remove(nomeCluster);
					}
				}

				for(SnapShot single: singleCache.values()){
					Timestamp fooDate = getNextDate(single.getDate());
					String nomeCluster = single.getClusterName();

					JSONObject node = createNode(fooDate, "fooSon", single.getNumDoc()); 
					nodes.add(node);
					
					JSONObject link = createLink(single.getDate(), nomeCluster, fooDate, "fooSon", single.getNumDoc());
					links.add(link);
				}
			
				for(SnapShot orphan: orphanCache.values()){
					Timestamp date = orphan.getDate();
					String nomeCluster = orphan.getClusterName();
					long numdoc = orphan.getNumDoc();
					
					Timestamp sonDate = date;
					Timestamp fatherDateFoo = getBeforeDate(sonDate);
					String fatherDateFooPW =  DateConverter.getDate(fatherDateFoo, DateConverter.PATTERN_VIEW);
					
					while (dayByDay.contains(fatherDateFooPW)) {
						JSONObject node = createNode(fatherDateFoo, "fooFather", -1); 
						nodes.add(node);
						
						JSONObject link = createLink(fatherDateFoo, "fooFather", sonDate, nomeCluster, singleCache.containsKey(nomeCluster)?numdoc:-1);
						links.add(link);

						nomeCluster = "fooFather";
						sonDate = fatherDateFoo;
						fatherDateFoo = getBeforeDate(sonDate);
						fatherDateFooPW =  DateConverter.getDate(fatherDateFoo, DateConverter.PATTERN_VIEW);
					}
					
				}

			}
			
						
			result.put("links", links);
			result.put("nodes", nodes);
						
		}catch(Exception e){
			throw e;
		}finally{
			cm.closeConnection(connection);
		}
		
		return result;
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobDataDescr jobDataDescr = getToken(request);

		String nome_job = jobDataDescr.getTipoTicket();
		logger.debug("nome job Spettrografo: " + nome_job);
		String data_da = jobDataDescr.getDataInizioSelected();
		String data_a = jobDataDescr.getDataFineSelected();

		String redirect = "graph/viewer.jsp";

		try{
			if(data_da==null || data_a==null ||
					data_da.trim().length()==0 || data_a.trim().length()==0 || 
					"--".equalsIgnoreCase(data_da) || "--".equalsIgnoreCase(data_a) ){
				throw new Exception("E'necessario valorizzare entrambe le date.");
			}
			
		}catch (Exception e) {
			Message errorMessage = new Message();
			e.printStackTrace();
			errorMessage.setText("Si Ë verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			redirect = "getJobList";
			errorMessage.setType(Message.ERROR);
			request.setAttribute("message", errorMessage);
		}finally{
			request.setAttribute("jobDataDescr", jobDataDescr);
		}


		RequestDispatcher dispatcher = request.getRequestDispatcher(redirect);
		dispatcher.forward(request, response);


	}

}
