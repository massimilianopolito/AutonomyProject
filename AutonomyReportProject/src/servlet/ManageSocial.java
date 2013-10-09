package servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Autonomy.D2Map;
import Autonomy.DocumentoQueryTO;
import Autonomy.DocumentoTO;

import model.JobDataDescr;
import model.Message;
import utility.AppConstants;
import utility.GenericServlet;

/**
 * Servlet implementation class ManageSocial
 */
public class ManageSocial extends GenericServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ManageSocial() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private Collection<DocumentoTO> query(HttpServletRequest request, JobDataDescr globalEnv) throws Exception{
		Collection<DocumentoTO> result = null;

		String tipoTicket = globalEnv.getRadiceJob();
		String categoriaTicket = globalEnv.getSuffissoJob();

		String root = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
		String ticket = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, tipoTicket).toUpperCase();
		String tipo =AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, categoriaTicket).toUpperCase();
		
		String testo = request.getParameter("testo");
		String domanda = request.getParameter("domanda");//DOMANDA
		String risposta = request.getParameter("risposta");//RISPOSTA
		String codiceTicket = request.getParameter("codiceTicket");//DRETITLE
		String numRis = request.getParameter("numRisultati");
		String relevance = request.getParameter("relevance");
		
		if(testo==null || testo.trim().length()==0) testo="";
		testo = testo.trim();
		HashMap<String, String> chiaveValore = new HashMap<String, String>();
		if(domanda!=null && domanda.trim().length()!=0) chiaveValore.put("DOMANDA", domanda);
		if(risposta!=null && risposta.trim().length()!=0) chiaveValore.put("RISPOSTA", risposta);
		if(codiceTicket!=null && codiceTicket.trim().length()!=0) chiaveValore.put("DRETITLE", codiceTicket);

		D2Map d2Map = new D2Map();
		result = d2Map.QuerySocial(root, ticket, chiaveValore, numRis, relevance, testo);

		return result;
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobDataDescr jobDataDescr = null;
		String redirect = "querySocial/queryResult.jsp";
		String operation = request.getParameter("operation");
		String msg = null;

		try{
			jobDataDescr = getToken(request);
			JobDataDescr global = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
			
			if("1".equalsIgnoreCase(operation)){
				Collection<DocumentoTO> listaRisultati = query(request, global);
				request.setAttribute("listaRisultati", listaRisultati);
			}
			
		}catch(Exception e){
			Message errorMessage = new Message();
			e.printStackTrace();
			errorMessage.setText("Si è verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			errorMessage.setType(Message.ERROR);
			request.setAttribute("message", errorMessage);
		}finally{
			request.setAttribute("jobDataDescr", jobDataDescr);
		}
		
		forward(request, response, redirect);
	}

}
