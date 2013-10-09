<%@page import="utility.AppConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Iterator"%>
<%@page import="model.Troika"%>
<%@page import="model.JobDataDescr"%>
<%@page import="java.util.Collection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%
		JobDataDescr jobDataDescr = (JobDataDescr) request.getAttribute("jobDataDescr");
		Collection<String> firstComboValues = jobDataDescr.getComboValues();
		Collection<String> customComboValues = jobDataDescr.getComboCustomValues();

	%>

	<script>
	function send(operation){
		document.getElementById("operation").value = operation;
		first = document.getElementById("first").value;
		second = document.getElementById("second").value;
		third = document.getElementById("third").value;

		if("1"==operation){
		custom = document.getElementById("custom").value;
		if("--"==custom && "--"==first && "--"== second && "--"==third){
		alert('E\'necessario selezionare una terna o il Campo Aggiuntivo.');
		}else if("--"!=first && ("--"== second || "--"==third)){
		alert('Tutti gli elementi della terna devono essere valorizzati.');
		}else if(("--"!=custom && "--"==first && "--"== second && "--"==third) || 
		("--"!=first && "--"!= second && "--"!=third)){
		$.cookie('collapsible', null, { path: '/' });
		document.forms[0].submit();
		}
		}else if("6"==operation){
		document.forms[0].submit();
		}
		}
	</script>
</head>
<body>
		<div class='box boxForm shadow'>
							<div class='title'>
								<p>Compilare la scheda che segue con i valori richiesti</p>
							</div>
					<div class='content'>
		
		<form action="ManageAdmin" method="post" target="contentFrame" id="gestione">
			<input type='hidden' id="typeOfField" name="typeOfField" value="<%=AppConstants.Tripletta.TROIKA_FIELD_MANAGE_D%>"/>
			<input type='hidden' id="classeReport" name="classeReport" value="<%=AppConstants.ClasseReport.ONTOLOGYTRAINER%>"/>
			<input type='hidden' id="operation" name="operation"/>
						<p>
					<label>Motivo: </label><select name="first" id="first">
								<option value="--">- Selezionare - </option>
								<%
									for(String value: firstComboValues){
								%>
									<option value="<%=value%>"><%=value%></option>
								<%		
									}
								%>
							</select>
							</p>
							<p>
							<label>Argomento: </label><select name="second" id="second">
								<option value="--">- Selezionare - </option>
							</select>
							</p>
							<p>
		
							<label>Specifica: </label><select name="third" id="third">
								<option value="--">- Selezionare - </option>
							</select>
							</p>
									
							<label>Campo Aggiuntivo: </label><select name="custom" id="custom">
								<option value="--">- Selezionare - </option>
								<%
									for(String value: customComboValues){
								%>
									<option value="<%=value%>"><%=value%></option>
								<%		
									}
								%>
							</select>
							</p>
			
				<input type="button" value="Avvia Ricerca" onclick="send('1')"/>
				<input type="button" value="Esporta in Excel" onclick="send('6')"/>
			
		</form>
		</div>
			</div>
		
		
				<iframe name="contentFrame"
		              	width="100%" height="400px" scrolling="auto"
		                marginheight="0" marginwidth="0" frameborder="0">
		             <p>Your browser does not support iframes</p>
		    	</iframe>
	    	

</body>
</html>