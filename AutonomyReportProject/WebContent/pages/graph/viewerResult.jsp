<%@page import="model.QueryObject"%>
<%@page import="Autonomy.DocumentoQueryTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.Collection"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
 		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script> 
	  	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.10.2.custom.js" type="text/javascript"></script>
	  	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/d3/d3.v3.min.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/d3/sankey.js" type="text/javascript"></script>
 		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
 		
	</head>
	<%
		String nome_Cluster = request.getParameter("nomeCluster");
	%>
	
	<body>
		<div id="wrapper">

			<div class="box boxForm shadow">
				<div class="title">
					<p>Risultati ottenuti per: <%=nome_Cluster %></p>
				</div>
			</div>
		</div>
	</body>
</html>
