package servlet;

import java.io.IOException;
import java.util.Collection;

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
 * Servlet implementation class ManageTroika
 */
public class ManageTroika extends GenericServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageTroika() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String first = request.getParameter("first");
		String second = request.getParameter("second");
		String third = request.getParameter("third");
		String relevance = request.getParameter("relevance");
		String redirect = "tripletta/triplettaResult.jsp";
		try{
			if("--".equalsIgnoreCase(first)) first = null;
			if("--".equalsIgnoreCase(second)) second = null;
			if("--".equalsIgnoreCase(third)) third = null;
			if(first==null || second==null || third==null) throw new Exception("Almeno uno dei valori selezionati non è valido.");
			
			JobDataDescr globalEnv = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
			
			String ambito = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnv.getAmbito());
			String contesto = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, globalEnv.getRadiceJob()).toUpperCase();
			String specifica = AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, globalEnv.getSuffissoJob()).toUpperCase();
			String tripletta = first + "/" + second + "/" + third;

			D2Map d2Map = new D2Map();			
			Collection<DocumentoTO> listaRisultati = d2Map.TripletteSuggestAutonomy(ambito, contesto, specifica, tripletta, relevance);
			
/*			DocumentoTO doc = new DocumentoTO();
			doc.setReferenceDoc("test");
			doc.setTitleDoc("Titolo");
			doc.setSummary("Summary");
			doc.setDataBase("idoldb1");
			listaRisultati.add(doc);

			DocumentoTO doc1 = new DocumentoTO();
			doc1.setReferenceDoc("test");
			doc1.setTitleDoc("Titolo");
			doc1.setSummary("Summary");
			doc1.setDataBase("idoldb1");
			listaRisultati.add(doc1);

			DocumentoTO doc2 = new DocumentoTO();
			doc2.setReferenceDoc("test");
			doc2.setTitleDoc("Titolo");
			doc2.setSummary("Summary");
			doc2.setDataBase("idoldb1");
			listaRisultati.add(doc2);
*/
			request.setAttribute("listaRisultati", listaRisultati);
			
		}catch(Exception e){
			Message errorMessage = new Message();
			e.printStackTrace();
			errorMessage.setText("Si è verificato un errore in fase di recupero dati.<br>" + e.getMessage());
			errorMessage.setType(Message.ERROR);
			request.setAttribute("message", errorMessage);
		}

		request.setAttribute("first", first);
		request.setAttribute("second", second);
		request.setAttribute("third", third);

		forward(request, response, redirect);

	}

}
