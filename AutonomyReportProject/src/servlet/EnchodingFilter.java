package servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import dao.UserDao;
import thread.ManageThread;
import utility.AppConstants;

/**
 * Servlet Filter implementation class EnchodingFilter
 */
public class EnchodingFilter implements Filter {
	ManageThread gestisciThread = new ManageThread();

    /**
     * Default constructor. 
     */
    public EnchodingFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF8");
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String userName = httpServletRequest.getRemoteUser();
		String profile = (String)httpServletRequest.getSession().getAttribute("profile");
		
		if(profile == null){
			try{
				UserDao userDao = new UserDao();
				profile = userDao.getMyProfile(userName);
				if(profile==null) profile = "admin";
				httpServletRequest.getSession().setAttribute("profile", profile);
				
				Map<String, Collection<String>> authCombo = userDao.getAuthComboByProfile(profile);
				httpServletRequest.getSession().setAttribute("authCombo", authCombo);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {}

}
