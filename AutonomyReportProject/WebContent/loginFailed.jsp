<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Version 20-03-2013" content="1.0">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css">
	
	<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/script-login.js" type="text/javascript"></script>
	
	<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'>
	<title>Login - D-CUBE | Digital Customer Behaviour</title>
</head>

<body>
	<div id="loginForm" class="roundBorder shadow">
		<form method="post" action="j_security_check">
			<%@ include file="pages/header.jsp" %>
			<div class="errore">
				<p><B><font style="color:#ff0000;">Credenziali non valide!</font></B></p>
			</div>

			<label>Utente: </label>
			<input type="text"  name="j_username" />
			<br />
			<label>Password: </label>
			<input type="password" name="j_password"/>
			<div class="clr"></div>
			
			<div style="text-align:center">
				<input type="submit" value="Invia"/>
			</div>
		</form>
	</div>
		
</body>
</html>

