<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Collection"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  	<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery-ui.custom.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
	<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'>
	<title>Login - D-CUBE | Digital Customer Behaviour</title>
	
	
</head>
<body>
	<div id="wrapper">
		<%@ include file="../header.jsp" %>
		<table id='tbSocial'>
					<tr>
						<td>
						<div class='box boxForm shadow'>
							<div class='title'>
								<p>Compilare la scheda che segue con i valori richiesti</p>
							</div>
					<div class='content'>		
			<form action="ManageSocial" method="post" name="myform" target="contentFrame">
				<input type="hidden" name="operation" value="1"/>
				
		
<!-- 					<label>Testo di ricerca: </label>
					<input type="text" name="testo" id="testo" style="width:280px"/>
					<br/>
 -->
					<p>
					<label>Grado di rilevanza: </label>
					<select name="relevance" id="relevance">
						<option value="50">50</option>
						<option value="60">60</option>
						<option value="70">70</option>
						<option value="90">90</option>
					</select>
					</p>
					<p>

					<label>Numero di risultati: </label>
					<select name="numRisultati" id="numRisultati">
						<option value="200">200</option>
						<option value="500">500</option>
						<option value="1000">1000</option>
					</select>
					</p>
					<p>

					<label>Domanda: </label>
					<input type="text" name="domanda" id="domanda"/>
					</p>
					<p>

					<label>Risposta: </label>
					<input type="text" name="risposta" id="risposta"/>
					</p>
					<p>

					<label>Codice Ticket: </label>
					<input type="text" name="codiceTicket" id="codiceTicket"/>
					</p>
								
						<input type="submit" value="Invia"/>
						<%@ include file="../back.jsp" %>
					
				
				</form>
		</div>
		</div>
		
		</td>
						
						<td>
				<iframe name="contentFrame" 
		              	width="100%" height="400px" scrolling="auto"
		                marginheight="0" marginwidth="0" frameborder="0">
		             <p>Your browser does not support iframes</p>
		    	</iframe><div class='clr'></div>
						</td>
					</tr>
				</table>	
				
				
					
			</div>
</body>
</html>