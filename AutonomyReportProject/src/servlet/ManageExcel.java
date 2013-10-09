package servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.GenericServlet;

/**
 * Servlet implementation class ManageExcel
 */
public class ManageExcel extends GenericServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageExcel() {
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
		String redirect = (String)request.getAttribute("redirect");
		String nome = (String)request.getAttribute("nomeFile");
		String filename = "Result_"+request.getRemoteUser();
		if(nome!=null&& nome.trim().length()>0) filename = "Result_" + nome + "_" + request.getRemoteUser();
				
		response.setHeader("Content-Disposition", "attachment; filename="+filename+".xls");
		response.setContentType("application" + File.separator + "vnd.ms-excel");
		response.setHeader("Cache-Control", "public");
        response.setHeader("Pragma", "public");
        forward(request, response, redirect);
	}

}
