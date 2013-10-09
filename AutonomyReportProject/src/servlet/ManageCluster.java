package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.HTDocumentDao;

import model.HTDocumentObject;
import model.JobDataDescr;
import model.Message;
import utility.AppConstants;
import utility.ConnectionManager;
import utility.GenericServlet;
import Autonomy.D2Map;
import Autonomy.DocumentoQueryTO;
import Autonomy.DocumentoTO;

/**
 * Servlet implementation class GenericServlet
 */
public class ManageCluster extends GenericServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageCluster() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCluster = request.getParameter("idCluster");
		String nomeCluster = request.getParameter("nomeCluster");
		String nomeTabella = request.getParameter("nomeTabella");
		String dataSelezionata = request.getParameter("dataSelezionata");

		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = null;
		try{

			connection = cm.getConnection(true);
			HTDocumentDao htDocumentDao = new HTDocumentDao(connection, nomeTabella);
			
			HTDocumentObject htDocumentObject = new HTDocumentObject();
			htDocumentObject.setIdCluster(idCluster);
			
			List<HTDocumentObject> list = htDocumentDao.getDocumentByCluster(htDocumentObject);
			
			List<DocumentoQueryTO> listResult = null;
			for(HTDocumentObject currentDoc: list){
				DocumentoQueryTO documentoQueryTO = (DocumentoQueryTO)currentDoc.getDocumento();
				if(listResult==null) listResult = new ArrayList<DocumentoQueryTO>();
				listResult.add(documentoQueryTO);
			}
			
			request.setAttribute("listaRisultati", listResult);
			request.setAttribute("dataSelezionata", dataSelezionata);
			
		}catch (Exception e) {
			Message errorMessage = new Message();
			e.printStackTrace();
			errorMessage.setText("Si è verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			errorMessage.setType(Message.ERROR);
			request.setAttribute("message", errorMessage);
		}finally{
			try{
				cm.closeConnection(connection);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		request.setAttribute("nomeCluster", nomeCluster);
		RequestDispatcher dispatcher = request.getRequestDispatcher("hotTopics/hotTopicsResult.jsp");
		
		dispatcher.forward(request, response);

	}
 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
