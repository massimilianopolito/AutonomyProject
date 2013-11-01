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
	
	private Timestamp getNextDate(Timestamp date) throws Exception{
		Timestamp nextDate = date;
		Calendar nextDateCalendar = GregorianCalendar.getInstance();
		nextDateCalendar.setTime(new Date(date.getTime()));
		nextDateCalendar.add(Calendar.DATE, 1);
		nextDate = new Timestamp(nextDateCalendar.getTimeInMillis());
		return nextDate;
	}
	
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
				Collection<SnapShot> recordsInDate = snapShotDao.getRecordByDate(dayTime, dayTime, nome_job);
				Set<String> clusterCache = new HashSet<String>();
				for(SnapShot currentSnapShot: recordsInDate){
					String nomeCluster = currentSnapShot.getClusterName();
					if(!clusterCache.contains(nomeCluster)){
						clusterCache.add(nomeCluster);
						JSONObject node = new JSONObject();
						node.put("name", DateConverter.getDate(currentSnapShot.getDate(), DateConverter.PATTERN_VIEW) + " " + nomeCluster);
						node.put("date", DateConverter.getDate(currentSnapShot.getDate(), DateConverter.PATTERN_VIEW));
						nodes.add(node);
					}else if(dayByDay.contains(DateConverter.getDate(nextDate, DateConverter.PATTERN_VIEW))){
						int familyId = currentSnapShot.getKey();
						int idLegame = currentSnapShot.getIdLegame();
						SnapShot linkSnapData = new SnapShot();
						linkSnapData.setDate(nextDate);
						linkSnapData.setKey(familyId);
						linkSnapData.setIdLegame(idLegame);
						linkSnapData.setSnapShot(nome_job);
						linkSnapData = snapShotDao.getLink(linkSnapData);
						
						JSONObject link = new JSONObject();
						link.put("source", DateConverter.getDate(currentSnapShot.getDate(), DateConverter.PATTERN_VIEW) + " " + nomeCluster);
						link.put("target", DateConverter.getDate(linkSnapData.getDate(), DateConverter.PATTERN_VIEW) + " " + linkSnapData.getClusterName());
						link.put("value", currentSnapShot.getNumDoc());
						links.add(link);
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

		String redirect = "graph/viewerGraph.jsp";

		try{
			if(data_da==null || data_a==null ||
					data_da.trim().length()==0 || data_a.trim().length()==0 || 
					"--".equalsIgnoreCase(data_da) || "--".equalsIgnoreCase(data_a) ){
				throw new Exception("E'necessario valorizzare entrambe le date.");
			}
			
		}catch (Exception e) {
			Message errorMessage = new Message();
			e.printStackTrace();
			errorMessage.setText("Si è verificato un errore in fase di recupero dati.<br>" + e.getMessage());
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
