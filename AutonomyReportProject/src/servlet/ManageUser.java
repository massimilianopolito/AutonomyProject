package servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jk.core.Msg;

import dao.UserDao;
import model.Message;
import utility.GenericServlet;

/**
 * Servlet implementation class ManageUser
 */
public class ManageUser extends GenericServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ManageUser() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void changePwd(HttpServletRequest request, String pwd) throws Exception{
		String currentUser = request.getRemoteUser();
		UserDao userDao = new UserDao();
		userDao.changePwd(currentUser, pwd);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		String operation = request.getParameter("operation");
		String redirect = "";
		Message responseMessage = new Message();
		responseMessage.setType(Message.INFO);
		//String base ="http://" + request.getServerName() +":" + request.getServerPort() + request.getContextPath() + "/pages/";

		try{
			if("2".equalsIgnoreCase(operation)){
				changePwd(request, newPwd);
				redirect = "changePwd.jsp";
				responseMessage.setText("La password è stata correttamente modificata.");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			responseMessage.setText("Si è verificato un errore in fase di modifica password.<br>" + e.getMessage());
			responseMessage.setType(Message.ERROR);
		}finally{
			request.setAttribute("message", responseMessage);
		}

		forward(request, response, redirect);

	}

}
