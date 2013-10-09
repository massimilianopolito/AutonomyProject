<%@page import="utility.NavigationMaker"%>
<%
	NavigationMaker nmBack = NavigationMaker.getInstance();
	String back = request.getContextPath() + nmBack.getBack();

%>
<input type="button" value="Indietro" onclick="document.location.href='<%=back %>';">
