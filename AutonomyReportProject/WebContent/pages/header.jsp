<%@page import="utility.NavigationMaker"%>
<%@page import="utility.PropertiesManager"%>


	<%
		String env = PropertiesManager.getMyProperty("environment");
	
	%>
<%-- 	<div id="header">
		<table>
			<tr>
				<%if(!"max".equalsIgnoreCase(env)){ %>
					<td width="20%"><img alt="logo" src="<%=request.getContextPath()%>/img/logo-small.jpg" onclick="document.location.href='<%=request.getContextPath() %>/pages/start.jsp'"> </td>
				<%}%>
				<%if(request.getRemoteUser()!=null){ %>
					<td class="headerMsg"><p class="headerWelcome"><i>Benvenuto</i> <b><%=request.getRemoteUser() %></b></p></td>
				<%}%>
			</tr>
		</table>
	</div>

	<%@ include file="errorHeader.jsp" %>
 --%>
	<%if(request.getRemoteUser()==null){ %>
		<div id="header">
			<img alt="logo" src="<%=request.getContextPath()%>/img/logo-small.png" />
			<h1>D-CUBE | <span class="subTitle">Digital Customer Behaviour</span></h1>
		</div>
	<%}else{%>
		<div id="header">
			<div class="left"><img alt="logo" src="<%=request.getContextPath()%>/img/logo-small.png" /></div>
			<div class="user">
				<p><i>Benvenuto</i> <b><%=request.getRemoteUser() %></b> | <i><a href="<%=request.getContextPath() %>/pages/LogOut">Logout</a></i></p>
		    </div>
			<div class="clr"></div>
		</div>
		<div id="menu">
				<h1>D-CUBE | <span class="subTitle">Digital Customer Behaviour</span></h1>
				<%@ include file="navigationStripe.jsp" %>
				
				<div class="clock">
					<div id="Date"></div>
					<ul>
						<li id="hours"> </li>
						<li id="point">:</li>
						<li id="min"> </li>
						<li id="point">:</li>
						<li id="sec"> </li>
					</ul>
					<div class="clr"></div>
				</div>
				<div class="clr"></div>
			</div>
		
		<div id="pageHeading">
		
			<h2><%= navigationMaker.getPage()%></h2>
			<%@ include file="infoBox.jsp" %>
			<a href="#" class="help"><img src="<%=request.getContextPath()%>/img/icon/help-w.png" alt="Help" title="Help" /></a>
			<div class="clr"></div>
			
		</div>
		<div id="pageHeadingPlaceholder"></div>
		
		
		<%@ include file="messageHeader.jsp" %>
		
	<%}%>

	