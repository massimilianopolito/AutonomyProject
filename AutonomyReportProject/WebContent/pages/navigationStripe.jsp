<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="model.JobDataDescr"%>
<%@page import="utility.AppConstants"%>
<%@page import="utility.PropertiesManager"%>
<%@page import="utility.NavigationMaker"%>
	<%
		NavigationMaker navigationMaker = NavigationMaker.getInstance();
		String navigationLink = navigationMaker.getNavigationLabel(request.getRequestURI());
	%> 
	
	<div id="contentNavigation">
		<p>
			<%=navigationLink %>
		</p>
		<div class='clr'></div>	
	</div> 

	