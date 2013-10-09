<%@page import="Autonomy.DocumentoQueryTO"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="Autonomy.D2Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
</head>
<body>

	<%
		String title = (String)request.getAttribute("title");
		String content = (String)request.getAttribute("content");
		
		Object doc = request.getAttribute("documento");
		DocumentoQueryTO documentoQueryTO = null;
		boolean isVisibleDetail = false;
		if(doc!=null && (doc instanceof DocumentoQueryTO)){
			isVisibleDetail = true;
			documentoQueryTO = (DocumentoQueryTO)doc;
		}
	 %>
	 <div class='box boxDettaglio shadow'>
		<div class='title'>
			<p>Dettaglio</p>
		</div>
		<div class='content'>
			<p><strong><%=title %></strong></p>
	 		<p><%=content %></p>
			<hr style="margin:30px 10px"/>
	 <%if(isVisibleDetail){ %>
				<%if(documentoQueryTO.getServiceTeam()!=null && documentoQueryTO.getServiceTeam().length() > 0){ %>
				<label>Service Team:</label>
				<span><%=documentoQueryTO.getServiceTeam() %></span>
				<br/>
				<%} %>

				<%if(documentoQueryTO.getMotivo()!=null && documentoQueryTO.getMotivo().length() > 0){ %>
				<label>Motivo:</label>
				<span><%=documentoQueryTO.getMotivo() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getArgomento()!=null && documentoQueryTO.getArgomento().length() > 0){ %>
				<label>Argomento:</label>
				<span><%=documentoQueryTO.getArgomento() %></span>
				<br/>
				<%} %>

				<%if(documentoQueryTO.getSpecifica()!=null && documentoQueryTO.getSpecifica().length() > 0){ %>
				<label>Specifica:</label>
				<span><%=documentoQueryTO.getSpecifica() %></span>
				<br/>
				<%} %>

				<%if(documentoQueryTO.getCodInterazione()!=null && documentoQueryTO.getCodInterazione().length() > 0){ %>
				<label>Codice Interazione:</label>
				<span><%=documentoQueryTO.getCodInterazione() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getDataCreazione()!=null && documentoQueryTO.getDataCreazione().length() > 0){ %>
				<label>Data Creazione:</label>
				<span><%=documentoQueryTO.getDataCreazione() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getTipoCanale()!=null && documentoQueryTO.getTipoCanale().length() > 0){ %>
				<label>Tipo Canale:</label>
				<span><%=documentoQueryTO.getTipoCanale() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getDirezione()!=null && documentoQueryTO.getDirezione().length() > 0){ %>
				<label>Direzione:</label>
				<span><%=documentoQueryTO.getDirezione() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getCodCliente()!=null && documentoQueryTO.getCodCliente().length() > 0){ %>
				<label>Codice Cliente:</label>
				<span><%=documentoQueryTO.getCodCliente() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getCrmNativo()!=null && documentoQueryTO.getCrmNativo().length() > 0){ %>
				<label>CRM Nativo:</label>
				<span><%=documentoQueryTO.getCrmNativo() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getSubConclusioni()!=null && documentoQueryTO.getSubConclusioni().length() > 0){ %>
				<label>Sub Conclusioni:</label>
				<span><%=documentoQueryTO.getSubConclusioni() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getConclusioni()!=null && documentoQueryTO.getConclusioni().length() > 0){ %>
				<label>Conclusioni:</label>
				<span><%=documentoQueryTO.getConclusioni() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getSegmento()!=null && documentoQueryTO.getSegmento().length() > 0){ %>
				<label>Segmento:</label>
				<span><%=documentoQueryTO.getSegmento() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getTeamInboxCreaz()!=null && documentoQueryTO.getTeamInboxCreaz().length() > 0){ %>
				<label>Team Inbox Creazione:</label>
				<span><%=documentoQueryTO.getTeamInboxCreaz() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getCodCase()!=null && documentoQueryTO.getCodCase().length() > 0){ %>
				<label>Codice Case:</label>
				<span><%=documentoQueryTO.getCodCase() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getFlagWTT()!=null && documentoQueryTO.getFlagWTT().length() > 0){ %>
				<label>Flag WTT:</label>
				<span><%=documentoQueryTO.getFlagWTT() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getFlagRATT()!=null && documentoQueryTO.getFlagRATT().length() > 0){ %>
				<label>Flag RATT:</label>
				<span><%=documentoQueryTO.getFlagRATT() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getTeamInboxChiusura()!=null && documentoQueryTO.getTeamInboxChiusura().length() > 0){ %>
				<label>Team Inbox Chiusura:</label>
				<span><%=documentoQueryTO.getTeamInboxChiusura() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getTeamInboxDest()!=null && documentoQueryTO.getTeamInboxDest().length() > 0){ %>
				<label>Team Inbox Destinazione:</label>
				<span><%=documentoQueryTO.getTeamInboxDest() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getDataChiusura()!=null && documentoQueryTO.getDataChiusura().length() > 0){ %>
				<label>Data Chiusura:</label>
				<span><%=documentoQueryTO.getDataChiusura() %></span>
				<br/>
				<%} %>
				
				<%if(documentoQueryTO.getRisposta()!=null && documentoQueryTO.getRisposta().length() > 0){ %>
				<label>Risposta:</label>
				<span><%=documentoQueryTO.getRisposta() %></span>
				<br/>
				<%} %>
	 <%} %>
	 	</div>
	</div>
</body>
</html>