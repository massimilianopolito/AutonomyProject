package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.JobDataDescr;
import utility.AppConstants;
import utility.GenericServlet;
import Autonomy.D2Map;
import Autonomy.DocumentoTO;
import Autonomy.DocumentoQueryTO;

/**
 * Servlet implementation class GetContent
 */
public class GetContent extends GenericServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetContent() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.err.println("Do GET!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JobDataDescr global =( JobDataDescr)request.getSession().getAttribute("globalEnvironment");;
		String reference = request.getParameter("reference").replace(AppConstants.REFERENCE_DOPPIOAPICE, "\"");
		String idoldb = request.getParameter("idoldb");
		String redirect = request.getParameter("redirect");
		String query = request.getParameter("query");
		String title = "Titolo";
		String content = "Contenuto - testo";
		DocumentoTO documento = null;
		
		try{
			D2Map d2Map = new D2Map();
			
			if(AppConstants.ClasseReport.SOCIAL.equalsIgnoreCase(global.getClasseReport())){
				documento = d2Map.GetContentSocial(reference, idoldb);//new DocumentoQueryTO();
			}else if(AppConstants.ClasseReport.ONTOLOGYTRAINER.equalsIgnoreCase(global.getClasseReport())){
				
				documento = d2Map.GetContentTriplette(reference, idoldb, query);//new DocumentoQueryTO();
			}else if(AppConstants.ClasseReport.REAL_TIME.equalsIgnoreCase(global.getClasseReport())){
				
				documento = d2Map.GetContentSottolineato(reference, idoldb, query);//new DocumentoQueryTO();
			}else if(AppConstants.ClasseReport.STRUTTURA.equalsIgnoreCase(global.getClasseReport())){
				
				documento = d2Map.GetContentSottolineato(reference, idoldb, query);//new DocumentoQueryTO();
			}
			else{
				
				documento = d2Map.GetContentQuery(reference, idoldb);//new DocumentoQueryTO();
			}
				

			title = documento.getTitleDoc();
			content = documento.getContent();
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("title", title);
		request.setAttribute("content", content);
		request.setAttribute("documento", documento);

		forward(request, response, redirect);
	}

}
