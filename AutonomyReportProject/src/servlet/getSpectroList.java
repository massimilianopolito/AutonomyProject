package servlet;

import java.io.IOException;
import java.util.ArrayList;
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
import Autonomy.D2Map;
import Autonomy.SpectroTO;
 
/**
 * Servlet implementation class getSpectroList
 */
public class getSpectroList extends GenericServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getSpectroList() {
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
		JobDataDescr jobDataDescr = getToken(request);
		List<String> dateInizio = (ArrayList<String>) request.getSession().getAttribute("SPECTRO_dataInizio");
		List<String> dateFine = (ArrayList<String>) request.getSession().getAttribute("SPECTRO_dataFine");

		String nome_job = jobDataDescr.getTipoTicket();
		System.out.println("nome job Spettrografo: " + nome_job);
		String data_da = jobDataDescr.getDataInizioSelected();
		String data_a = jobDataDescr.getDataFineSelected();

		boolean findDataInizio = false;
		for(String currentDate: dateInizio){
			String humanDate = DateConverter.getDate(currentDate);
			if(humanDate.equalsIgnoreCase(data_da)){
				data_da = currentDate;
				findDataInizio = true;
				request.setAttribute("dataDa", data_da);
				break;
			}
		}

		boolean findDataFine = false;
		for(String currentDate: dateFine){
			String humanDate = DateConverter.getDate(currentDate);
			if(humanDate.equalsIgnoreCase(data_a)){
				data_a = currentDate;
				findDataFine = true;
				request.setAttribute("dataA", data_a);
				break;
			}
		}

		String redirect = "snapShot/viewSpectro.jsp?dataDa=" + data_da + "&dataA=" + data_a;

		try{
			if(data_da==null || data_a==null ||
					data_da.trim().length()==0 || data_a.trim().length()==0 || 
					"--".equalsIgnoreCase(data_da) || "--".equalsIgnoreCase(data_a) ){
				throw new Exception("E'necessario valorizzare entrambe le date.");
			}
			
			if(!findDataInizio){
				throw new Exception("Non ci sono elaborazioni che partano dalla data: " + data_da);
			}
			if(!findDataFine){
				throw new Exception("Non ci sono elaborazioni che terminino alla data: " + data_a);
			}
				

			D2Map d2Map = new D2Map();
			ArrayList<SpectroTO> jobDescription = d2Map.SGData(nome_job, data_da, data_a);
			
			if(jobDescription == null) throw new Exception("L'elaborazione: '" + nome_job + "' per le date: '"+ data_da +"' - '" +  data_a +"' non è valida.");
			request.getSession().setAttribute("jobDescription", jobDescription);
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
