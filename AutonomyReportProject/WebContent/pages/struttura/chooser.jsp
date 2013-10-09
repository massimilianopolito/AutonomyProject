<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="utility.NumberGroupingSeparator"%>
<%
	String rappr = (String)request.getAttribute("rapprStruttura");
	String consolePath = (String)request.getAttribute("pathConsole");
	String totaleIn = (String) request.getSession().getAttribute("totaleInut");
	String docTot = ""; 
	if(totaleIn!=null && !totaleIn.isEmpty()) docTot = NumberGroupingSeparator.formatNumber(Integer.parseInt(totaleIn));

%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/jquery-ui.custom.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
		<script>
			function send(){
				var rappr = document.getElementById("rapprStruttura").options[document.getElementById("rapprStruttura").selectedIndex].value;
				if('<%=AppConstants.Struttura.QUERY %>'==rappr){
					document.forms[0].action="ManageStruttura";
					document.forms[0].submit();
				}else if('<%=AppConstants.Struttura.CONSOLE %>'==rappr){
					apri('Console','<%=consolePath%>');
				}else{
					alert('La selezione è obbligatoria.');
				}
			}
		</script>

	</head>

	
	<body>
		<div id="wrapper">
		<%@ include file="../header.jsp" %>
			
			<div class="box boxForm shadow">
				<div class="title">
					<p>Compilare la scheda che segue con i valori richiesti</p>
				</div>
				<div class="content">
			
					<form method="post">
						<input type="hidden" name="operation" value="5"/>
						<p>
						<label>Modalità di rappresentazione</label>
						<select id="rapprStruttura" name="rapprStruttura">
							<option value="--" >- Seleziona - </option>
							<option value="<%=AppConstants.Struttura.QUERY %>"  <%if(AppConstants.Struttura.QUERY.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Report Trainer</option>
							<option value="<%=AppConstants.Struttura.CONSOLE %>" <%if(AppConstants.Struttura.CONSOLE.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Console Pentaho</option>
						</select>
						</p>
						<p>
						<label>Totale Documenti: </label><label><%=docTot%></label> 
						</p>
					
							<input type="button" class="btnSubmit" value="Invia" onclick="javascript: send();"/>
						
					</form>
				</div>
			</div>	
		
		</div>
		
	</body>
</html>

