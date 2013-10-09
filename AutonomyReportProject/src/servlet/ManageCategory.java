package servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.JobDataDescr;
import model.Message;
import utility.AppConstants;
import utility.GenericServlet;
import Autonomy.D2Map;
import Autonomy.DocumentoTO;

/**
 * Servlet implementation class GenericServlet
 */
public class ManageCategory extends GenericServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageCategory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idCategory = request.getParameter("idCategory");
		String nomeParent = request.getParameter("parentCategory");
		String nomeCategory = request.getParameter("nomeCategory");
		
		try{
			JobDataDescr globalEnv = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
			String ambito = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
			String nomeRoot = ("Wind" + ambito).toUpperCase();
			String ticket = globalEnv.getTipoTicket();
			D2Map d2Map = new D2Map();
			Collection<DocumentoTO> listResult = d2Map.categoryQuery(ticket, nomeRoot, nomeParent, idCategory);
			
			request.setAttribute("listaRisultati", listResult);
			
		}catch (Exception e) {
			Message errorMessage = new Message();
			e.printStackTrace();
			errorMessage.setText("Si è verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			errorMessage.setType(Message.ERROR);
			request.setAttribute("message", errorMessage);
		}
		
		request.setAttribute("idCategory", idCategory);
		request.setAttribute("parentCategory", nomeParent);
		request.setAttribute("nomeCategory", nomeCategory);
		RequestDispatcher dispatcher = request.getRequestDispatcher("category/categoryResult.jsp");
		
		dispatcher.forward(request, response);

	}
 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
