<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="utility.NavigationMaker"%>
<%@page import="utility.AppConstants"%>
<%@page import="java.util.Iterator"%>
<%@page import="model.Troika"%>
<%@page import="model.JobDataDescr"%>
<%@page import="java.util.Collection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
		<div class='box boxForm shadow'>
							<div class='title'>
								<p>Compilare la scheda che segue con i valori richiesti</p>
							</div>
					<div class='content'>
		<form action="ManageAdmin" method="post" target="contentFrameQT">
			<input type='hidden' id="classeReport" name="classeReport" value="<%=AppConstants.ClasseReport.ONTOLOGYTRAINER%>">
			<input type='hidden' id="operation" name="operation" value="4">
							<p>
							<label>Testo di ricerca:</label><input type="text" name="testo" id="testo" style="width:500px"/>
							</p>
							<p>
							<label>Grado di rilevanza</label><select name="relevance" id="relevance">
								<%for(int i=10; i<=90; i=i+10){%>
								<option value="<%=i%>"><%=i %></option>
								<%}%>
							</select>
							</p>
							<p>
		
							<label>Numero di risultati:</label><select name="numRisultati" id="numRisultati">
								<option value="1">1</option>
								<option value="5">5</option>
								<option value="10">10</option>
								<option value="15">15</option>
								<option value="20">20</option>
							</select>
							</p>

							<p>
		
							<label>Silos:</label><select name="caratterizzazione" id="caratterizzazione">
								<option value="--" >- Seleziona - </option>
								<option value="<%=AppConstants.Tripletta.TROIKA_INFORMAZIONI %>"><%=AppConstants.Tripletta.TROIKA_INFORMAZIONI %></option>
								<option value="<%=AppConstants.Tripletta.TROIKA_ASSISTENZA %>"><%=AppConstants.Tripletta.TROIKA_ASSISTENZA %></option>
							</select>
							</p>
							<input type="submit" value="Avvia Ricerca"/>
					</form>
				</div>
			</div>

			

				<iframe name="contentFrameQT"
		              	width="100%" height="400px" scrolling="auto"
		                marginheight="0" marginwidth="0" frameborder="0">
		             <p>Your browser does not support iframes</p>
		    	</iframe>
	    	

</body>
</html>