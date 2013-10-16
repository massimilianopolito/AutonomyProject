package utility;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import model.JobDataDescr;
import model.ThreadTracer;
import thread.HotTopicsQueryMaker;
import thread.RetrieveSnapshot;
import thread.StrutturaXmlSender;
import dao.ThreadTracerDao;

public abstract class GenericServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Logger logger = ReportLogger.getLog("general");

	protected JobDataDescr getToken(HttpServletRequest request) throws ServletException{
		JobDataDescr jobDataDescr = new JobDataDescr();
		
		String ambito = request.getParameter("ambito"); //CUSTOMER o CONSUMER
		String radiceJob = request.getParameter("radiceJob"); //FISSO o MOBILE
		String suffissoJob = request.getParameter("suffissoJob"); //CASE o INTERAZIONI
		String tipoDiRappresentazione = request.getParameter("rappresentazione"); //2DMap o Spectro
		String tipologiaTicket = request.getParameter("nomeJob"); //FISSO_XXX o MOBILE_XXX
		String nomeAction = request.getParameter("servletName"); 
		String data_da = request.getParameter("dataDa");
		String data_a = request.getParameter("dataA");
		String operation = request.getParameter("operation");
		String classeReport = request.getParameter("classeReport");
		String admin = request.getParameter("amministrazione");

		jobDataDescr.setRappresentazione(tipoDiRappresentazione);
		jobDataDescr.setAmbito(ambito);
		jobDataDescr.setRadiceJob(radiceJob);
		jobDataDescr.setSuffissoJob(suffissoJob);
		jobDataDescr.setTipoTicket(tipologiaTicket);
		jobDataDescr.setActionName(nomeAction);
		jobDataDescr.setDataInizioSelected(data_da);
		jobDataDescr.setDataFineSelected(data_a);
		jobDataDescr.setOperation(operation);
		jobDataDescr.setClasseReport(classeReport);
		//jobDataDescr.setAmministrazione(admin);

		return jobDataDescr;
	}

	protected void forward (HttpServletRequest request, HttpServletResponse response, String redirect) throws IOException, ServletException{
		RequestDispatcher dispatcher = request.getRequestDispatcher(redirect);
		dispatcher.forward(request, response);
	}

}
