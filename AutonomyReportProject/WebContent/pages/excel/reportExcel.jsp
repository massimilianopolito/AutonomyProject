<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoQueryTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	List<DocumentoQueryTO> listaDocumenti = (ArrayList<DocumentoQueryTO>)request.getSession().getAttribute("listaRisultatiStruttura");
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
			<TH style="background-color: #F58233;">Codice Interazione</TH>
			<TH style="background-color: #F58233;">Titolo</TH>
			<TH style="background-color: #F58233;">Specifica</TH>
			<TH style="background-color: #F58233;">Motivo</TH>
			<TH style="background-color: #F58233;">Argomento</TH>
			<TH style="background-color: #F58233;">Service Team</TH>
			<TH style="background-color: #F58233;">Data Creazione</TH>
			<TH style="background-color: #F58233;">Tipo Canale</TH>
			<TH style="background-color: #F58233;">Direzione</TH>
			<TH style="background-color: #F58233;">Codice Cliente</TH>
			<TH style="background-color: #F58233;">CRM Nativo</TH>
			<TH style="background-color: #F58233;">Sub Conclusioni</TH>
			<TH style="background-color: #F58233;">Conclusioni</TH>
			<TH style="background-color: #F58233;">Segmento</TH>
			<TH style="background-color: #F58233;">Team Inbox Creazione</TH>
			<TH style="background-color: #F58233;">Codice Case</TH>
			<TH style="background-color: #F58233;">Flag WTT</TH>
			<TH style="background-color: #F58233;">Flag RATT</TH>
			<TH style="background-color: #F58233;">Team Inbox Chiusura</TH>
			<TH style="background-color: #F58233;">Data Chiusura</TH>
			<TH style="background-color: #F58233;">Team Inbox Destinazione</TH>
			<TH style="background-color: #F58233;">Summary</TH>
		</tr>
		<%
			int count=0;
			String classCss = "";
			for(DocumentoQueryTO documentoCorrente: listaDocumenti){	
				classCss = "style=\"background-color: #FFFFFF;\"";
				if(count%2==0)classCss = "style=\"background-color: #F9F9F9;\"";
		%>
			<tr>
				<td <%=classCss %>><%=documentoCorrente.getCodInterazione()!=null?documentoCorrente.getCodInterazione():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getTitleDoc()!=null?documentoCorrente.getTitleDoc():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getSpecifica()!=null?documentoCorrente.getSpecifica():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getMotivo()!=null?documentoCorrente.getMotivo():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getArgomento()!=null?documentoCorrente.getArgomento():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getServiceTeam()!=null?documentoCorrente.getServiceTeam():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getDataCreazione()!=null?documentoCorrente.getDataCreazione():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getTipoCanale()!=null?documentoCorrente.getTipoCanale():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getDirezione()!=null?documentoCorrente.getDirezione():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getCodCliente()!=null?documentoCorrente.getCodCliente():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getCrmNativo()!=null?documentoCorrente.getCrmNativo():""%></td>
				<td <%=classCss %>><%=documentoCorrente.getSubConclusioni()!=null?documentoCorrente.getSubConclusioni():""%></td>
				<td <%=classCss %>><%=documentoCorrente.getConclusioni()!=null?documentoCorrente.getConclusioni():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getSegmento()!=null?documentoCorrente.getSegmento():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getTeamInboxCreaz()!=null?documentoCorrente.getTeamInboxCreaz():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getCodCase()!=null?documentoCorrente.getCodCase():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getFlagWTT()!=null?documentoCorrente.getFlagWTT():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getFlagRATT()!=null?documentoCorrente.getFlagRATT():""%></td>
				<td <%=classCss %>><%=documentoCorrente.getTeamInboxChiusura()!=null?documentoCorrente.getTeamInboxChiusura():""%></td>
				<td <%=classCss %>><%=documentoCorrente.getDataChiusura()!=null?documentoCorrente.getDataChiusura():""%></td>
				<td <%=classCss %>><%=documentoCorrente.getTeamInboxDest()!=null?documentoCorrente.getTeamInboxDest():"" %></td>
				<td <%=classCss %>><%=documentoCorrente.getSummary()!=null?documentoCorrente.getSummary():"" %></td>
			</tr>
		<%
				count++;
			} 
		%>
	</table>
</body>
</html>