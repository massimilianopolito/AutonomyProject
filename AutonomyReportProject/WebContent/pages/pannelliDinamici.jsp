<%@page import="java.util.Collection"%>
<%
	Collection<String> objLeft = (Collection<String>) request.getAttribute("objLeft");
	Collection<String> objRight = (Collection<String>) request.getAttribute("objRight");

%>
	
	<%if(objLeft!=null && !objLeft.isEmpty()){ %>
		<%for(String currentElement: objLeft){%>
			<p>
			<%=currentElement %>
		<%}%>
	<%} %>

	<%if(objRight!=null && !objRight.isEmpty()){ %>
			<%for(String currentElement: objRight){%>
				<%=currentElement %>
				</p>
			<%}%>
	<%} %>
	