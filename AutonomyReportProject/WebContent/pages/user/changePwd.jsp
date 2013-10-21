<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>

<head>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
	
		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
	  	<script src="<%=request.getContextPath()%>/js/jquery-ui.custom.js" type="text/javascript"></script>

	
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
</head>

<body>
	<div id="wrapper">
		<%@ include file="../header.jsp" %>
			<div class="box boxForm shadow">
				<div class="title">
					<p>Compilare la scheda che segue con i valori richiesti</p>
				</div>
				<div class="content">
					<form action="ManageUser" method="post">
						<input type='hidden' id="operation" name="operation" value="2"/>
						<p>
						<label>Vecchia Password: </label>
						<input type="password" id="oldPwd" name="oldPwd"/>
						</p>
						<p>
						<label>Nuova Password: </label>
						<input type="password" id="newPwd" name="newPwd"/>
						</p>
			
						<input type="submit" class="btnSubmit" value="Cambia"/>
					</form>
				</div>
			</div>
	</div>
	
</body>