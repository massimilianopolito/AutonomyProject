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
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery-ui-1.10.2.custom.css" />
		<div id="header">
			<div class="left"><img alt="logo" src="<%=request.getContextPath()%>/img/logo-small.png" /></div>
			<div class="user">
				<p><i>Benvenuto</i> <b><%=request.getRemoteUser() %></b> | <i><a href="<%=request.getContextPath() %>/pages/user/changePwd.jsp">Password</a></i> | <i><a href="<%=request.getContextPath() %>/pages/LogOut">Logout</a></i></p>
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
		
		<%
			String serverName = request.getServerName();
			String serverPort = request.getServerPort()+"";
			String nomeFunzione = navigationMaker.getPage();
			String nomePaginaHelp = "http://" + serverName + ":" + serverPort +  "/help/" + nomeFunzione.replaceAll(" ", "").trim() + ".html";
		%>
		<div id="pageHeading">
		
			<h2><%= nomeFunzione%></h2>
			<%@ include file="infoBox.jsp" %>
			<a id="openDlg" href="#" class="help"><img src="<%=request.getContextPath()%>/img/icon/help-w.png" alt="Help" title="Help" /></a>
			<div class="clr"></div>
			
		</div>

		<div id="pageHeadingPlaceholder"></div>
	
		<script type="text/javascript">
		    $(document).ready(function () {
		        $("#dialog").load('<%=nomePaginaHelp%>').dialog({ autoOpen: false, 
		        					  minWidth: "600" , 
		        					  minHeight: "500" });

		        $("#openDlg").click(
		            function () {
		                $("#dialog").dialog('open');
		                return false;
		            }
		        );

		    });
		</script>
 
		
		<div id="dialog" title="<%=nomeFunzione%>">
		</div>

		<%@ include file="messageHeader.jsp" %>
		
	<%}%>

	
	
