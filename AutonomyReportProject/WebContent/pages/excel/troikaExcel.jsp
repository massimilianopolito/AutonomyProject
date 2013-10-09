<%@page import="model.Troika"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoQueryTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	List<Troika> listaDocumenti = (ArrayList<Troika>)request.getSession().getAttribute("listaRisultati");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>

<body>
	<table border="1">
		<tr >
			<TH style="background-color: #F58233;">Motivo</TH>
			<TH style="background-color: #F58233;">Specifica</TH>
			<TH style="background-color: #F58233;">Argomento</TH>
			<TH style="background-color: #F58233;">Campo Custom</TH>
			<TH style="background-color: #F58233;">Descrizione</TH>
			<TH style="background-color: #F58233;">Risposta</TH>
			<TH style="background-color: #F58233;">Codice Tripletta</TH>
		</tr>
		<%
			int count=0;
			String classCss = "";
			for(Troika documentoCorrente: listaDocumenti){	
				classCss = "style=\"background-color: #FFFFFF;\"";
				if(count%2==0)classCss = "style=\"background-color: #F9F9F9;\"";
		%>
			<tr>
				<td <%=classCss %>><%=documentoCorrente.getFirstValue()!=null?documentoCorrente.getFirstValue():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getSecondValue()!=null?documentoCorrente.getSecondValue():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getThirdValue()!=null?documentoCorrente.getThirdValue():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getCustom()!=null?documentoCorrente.getCustom():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getDescrizione()!=null?documentoCorrente.getDescrizione():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getRisposta()!=null?documentoCorrente.getRisposta():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getCodTripletta()!=null?documentoCorrente.getCodTripletta():"" %></td>
			</tr>
		<%
				count++;
			} 
		%>
	</table>
</body>
</html>