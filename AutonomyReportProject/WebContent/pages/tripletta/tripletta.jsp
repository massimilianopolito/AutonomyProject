<%@page import="utility.NavigationMaker"%>
<%@page import="model.JobDataDescr"%>
<%@page import="java.util.Collection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%
		JobDataDescr jobDataDescrSP = (JobDataDescr) request.getAttribute("jobDataDescr");
		Collection<String> firstComboValuesSP = jobDataDescrSP.getComboValues();
	%>
</head>
<body>
	<div id="wrapper">
	<div class='box boxForm shadow'>
							<div class='title'>
								<p>Compilare la scheda che segue con i valori richiesti</p>
							</div>
					<div class='content'>
		<form action="ManageTroika" method="post" target="contentFrameSP" id="SP">
			<p>
			<label>Motivo: </label><select name="first" id="firstSP">
								<option value="--">- Selezionare - </option>
								<%
									for(String value: firstComboValuesSP){
								%>
									<option value="<%=value%>"><%=value%></option>
								<%		
									}
								%>
							</select>
							</p>
							<p>
							<label>Argomento: </label><select name="second" id="secondSP">
								<option value="--">- Selezionare - </option>
							</select>
							</p>
							<p>
		
							<label>Specifica: </label><select name="third" id="thirdSP">
								<option value="--">- Selezionare - </option>
							</select>
							</p>
							<p>

							<label>Grado di rilevanza</label><select name="relevance" id="relevance">
								<%for(int i=40; i<=90; i=i+10){%>
								<option value="<%=i%>"><%=i %></option>
								<%}%>
							</select>
							</p>
							<input type="submit" value="Avvia Ricerca"/>
				</form>
				</div>
			</div>

					

		
		
		<iframe name="contentFrameSP" 
		              	width="100%" height="400px" scrolling="auto"
		                marginheight="0" marginwidth="0" frameborder="0">
		             <p>Your browser does not support iframes</p>
		    	</iframe>
	    	
						
		
	</div>
</body>
</html>