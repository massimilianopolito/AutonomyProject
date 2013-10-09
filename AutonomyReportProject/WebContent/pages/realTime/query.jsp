<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Collection"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery-ui.custom.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script>
  	<script src="<%=request.getContextPath()%>/js/calendar.js" type="text/javascript"></script>
  	<script src="<%=request.getContextPath()%>/js/troika.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.datepick.js" type="text/javascript"></script>
 	<script src="<%=request.getContextPath()%>/js/jquery.datepick-it.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/datePick/jquery.datepick.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
	<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'>
	<title>Login - D-CUBE | Digital Customer Behaviour</title>
	<script>
		function sendAndResetButton(operationValue){
			if('1'==operationValue){
				try{
					document.contentFrame.document.body.innerHTML = "";
				}catch(e){
					window.contentFrame.document.body.innerHTML = "";
				}
				if(document.forms[0].grafico.style.visibility=="visible"){
					document.forms[0].grafico.style.visibility = "hidden";
				}
			}
			document.forms[0].operation.value=operationValue;
			document.forms[0].submit();
		}
	</script>
	
</head>
<body>
	<div id="wrapper">
		<%@ include file="../header.jsp" %>
		
		<table id='tbRealTime'>
					<tr>
						<td>
						<div class='box boxForm shadow'>
							<div class='title'>
								<p>Compilare la scheda che segue con i valori richiesti</p>
							</div>
					<div class='content'>				
			<form action="ManageRealTime" method="post" name="myform" id="myform" target="contentFrame">
				<input type="hidden" id="operation" name="operation"/>
				<input type="hidden" id="penthaoUrl" name="penthaoUrl" />
				
					<p>
					<label>Nome Query</label><input type="text" name="nomeQuery" id="nomeQuery" maxlength="200"/>
					</p>
					<p>
					<p>
					<label>Testo di ricerca:</label><input type="text" name="testo" id="testo"/>
					</p>
					<p>
					<label>Grado di rilevanza:</label><select name="relevance" id="relevance">
						<%for(int i=40; i<=90; i++){%>
						<option value="<%=i%>"><%=i %></option>
						<%}%>
					</select>
					</p>
					<p>
					<label>Numero di risultati:</label><select name="numRisultati" id="numRisultati">
						<option value="200">200</option>
						<option value="500">500</option>
						<%for(int i=1000; i<=10000; i=i+1000){%>
						<option value="<%=i%>"><%=i %></option>
						<%}%>
					</select>
					</p>
					<!-- Pannelli dinamici -->
					<%@ include file="../pannelliDinamici.jsp" %>
					<div class="clr"></div>
						<input type="button" value="Esegui" onclick="sendAndResetButton('1');"/>
						<%@ include file="../back.jsp" %>
						<input type="button" name ="Salva" value="Salva" style="visibility:hidden" onclick="sendAndResetButton('3');"/>
							
						<div id="grafico" style="visibility:hidden">
							<input type="button" name="grafico" value="Rappresentazione grafica" onclick="apri('Report',document.getElementById('penthaoUrl').value);" />
						
					</div>
				</form>
		</div>
		</div>
		
		</td>
		</tr>				
		<tr>
						<td>
						
				<iframe name="contentFrame" id="contentFrame"
		              	width="100%" height="750px" scrolling="auto"
		                marginheight="0" marginwidth="0" frameborder="0">
		             <p>Your browser does not support iframes</p>
		    	</iframe>
	    	
						<div class='clr'></div>
						</td>
					</tr>
				</table>	
				
				
					
			</div>
</body>
</html>