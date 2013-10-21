package servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jk.core.Msg;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
	
	
	private JSONObject makeChange(HttpServletRequest request) throws Exception{
		JSONObject json = new JSONObject();
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		String currentUser = request.getRemoteUser();

		if(oldPwd==null || oldPwd.isEmpty() || newPwd==null || newPwd.isEmpty()){
			json.put("type", "2");
			json.put("msg", "I campi devono essere entrambi correttamente valorizzati.");
		}else if(oldPwd.equalsIgnoreCase(newPwd)){
			json.put("type", "2");
			json.put("msg", "Le due password non possono essere coincidenti.");
		}else{
			UserDao userDao = new UserDao();
			if(userDao.isPwdCorrect(currentUser, oldPwd)){
				userDao.changePwd(currentUser, newPwd);
				json.put("type", "0");
				json.put("msg", "La password è stata correttamente modificata.");
			}else{
				json.put("type", "2");
				json.put("msg", "La vecchia password non coincide con quella indicata nel sistema.");
			}
		}
		return json;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operation = request.getParameter("operation");
		String redirect = "";
		Message responseMessage = new Message();
		responseMessage.setType(Message.INFO);
		
		JSONObject json = new JSONObject();

		try{
			if("2".equalsIgnoreCase(operation)){
				json = makeChange(request);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			responseMessage.setText("Si è verificato un errore <br>" + e.getMessage());
			responseMessage.setType(Message.ERROR);
			if("2".equalsIgnoreCase(operation)){
				json.put("type", "1");
				json.put("msg", "Si è verificato un errore in fase di modifica password.<br>" + e.getMessage());
			}
		}finally{
			request.setAttribute("message", responseMessage);
		}

		if("2".equalsIgnoreCase(operation)){
			request.setCharacterEncoding("UTF8");
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write(json.toString());
		}else{
			forward(request, response, redirect);
		}

	}

}
