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
		JobDataDescr jobDataDescrGR = (JobDataDescr) request.getAttribute("jobDataDescr");
		Collection<String> firstComboValuesGR = jobDataDescrGR.getComboValues();
		Collection<String> customComboValuesGR = jobDataDescrGR.getComboCustomValues();

	%>
	<script>
		function sendGR(operation){
			document.getElementById("operationGR").value = operation;
			first = document.getElementById("firstGR").value;
			second = document.getElementById("secondGR").value;
			third = document.getElementById("thirdGR").value;

			if("7"==operation){
				custom = document.getElementById("customGR").value;
				if("--"==custom && "--"==first && "--"== second && "--"==third){
					alert('E\'necessario selezionare una terna o il Campo Aggiuntivo.');
				}else if("--"!=first && ("--"== second || "--"==third)){
					alert('Tutti gli elementi della terna devono essere valorizzati.');
				}else if(("--"!=custom && "--"==first && "--"== second && "--"==third) || 
						 ("--"!=first && "--"!= second && "--"!=third)){
					$.cookie('collapsibleGR', null, { path: '/' });
					document.formGR.submit();
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
		
		<form action="ManageAdmin" method="post" target="contentFrameGR" id="gestioneGR" name="formGR">
			<input type='hidden' id="typeOfField" name="typeOfField" value="<%=AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R%>"/>
			<input type='hidden' id="classeReport" name="classeReport" value="<%=AppConstants.ClasseReport.ONTOLOGYTRAINER%>"/>
			<input type='hidden' id="operationGR" name="operation"/>
						<p>
					<label>Motivo: </label><select name="first" id="firstGR">
								<option value="--">- Selezionare - </option>
								<%
									for(String value: firstComboValuesGR){
								%>
									<option value="<%=value%>"><%=value%></option>
								<%		
									}
								%>
							</select>
							</p>
							<p>
							<label>Argomento: </label><select name="second" id="secondGR">
								<option value="--">- Selezionare - </option>
							</select>
							</p>
							<p>
		
							<label>Specifica: </label><select name="third" id="thirdGR">
								<option value="--">- Selezionare - </option>
							</select>
							</p>
									
							<label>Campo Aggiuntivo: </label><select name="custom" id="customGR">
								<option value="--">- Selezionare - </option>
								<%
									for(String value: customComboValuesGR){
								%>
									<option value="<%=value%>"><%=value%></option>
								<%		
									}
								%>
							</select>
							</p>
				

			
				<input type="button" value="Avvia Ricerca" onclick="sendGR('7')"/>
				<input type="button" value="Esporta in Excel" onclick="send('6')"/>
			
		</form>
		</div>
			</div>
		
		
				<iframe name="contentFrameGR"
		              	width="100%" height="400px" scrolling="auto"
		                marginheight="0" marginwidth="0" frameborder="0">
		             <p>Your browser does not support iframes</p>
		    	</iframe>
	    	

</body>
</html>