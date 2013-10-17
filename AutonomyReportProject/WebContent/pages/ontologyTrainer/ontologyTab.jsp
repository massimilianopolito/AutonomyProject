<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>

<head>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
<%-- 		<link href="<%=request.getContextPath() %>/css/jquery-ui-1.10.2.custom.css" rel="stylesheet" type="text/css" />
 --%>		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
		
		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/jquery-ui-1.10.2.custom.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/troika.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript" ></script>
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
	 <script>
	  $(function() {
	    $( "#tabs" ).tabs();
	  });
	  </script>

</head>

<body>
	<div id="wrapper">
		<%@ include file="../header.jsp" %>
		<div id="tabs">
			  <ul>
			    <li><a href="#tabs-1">Gestione Triplette</a></li>
			    <li><a href="#tabs-4">Gestione Risposte</a></li>
			    <li><a href="#tabs-2">Query testuale</a></li>
			    <li><a href="#tabs-3">Query per tripletta</a></li>
			  </ul>
			  <div id="tabs-1">
				<%@ include file="../tripletta/gestione.jsp" %>
			  </div>
			  <div id="tabs-4">
				<%@ include file="../tripletta/gestioneRisposta.jsp" %>
			  </div> 
			  <div id="tabs-2">
				<%@ include file="../tripletta/queryTraining.jsp" %>
			  </div>
			  <div id="tabs-3">
				<%@ include file="../tripletta/tripletta.jsp" %>
			  </div>
			</div>
		</div>
	
	
</body>