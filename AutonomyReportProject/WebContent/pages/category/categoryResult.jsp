<%@page import="utility.NumberGroupingSeparator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.Collection"%>
<% 
	String nomeCategory = (String)request.getAttribute("nomeCategory");
	List<DocumentoTO> listaDocumenti = (ArrayList<DocumentoTO>)request.getAttribute("listaRisultati");
	if(listaDocumenti == null) listaDocumenti = new ArrayList<DocumentoTO>();
	String totDoc = NumberGroupingSeparator.formatNumber(listaDocumenti.size()); 
   
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<link href="<%=request.getContextPath() %>/css/global.css" rel="stylesheet" type="text/css" >
	<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script>          
</head>
<body>
	<%@ include file="../messageHeader.jsp" %>
	<%if(message==null || message.getType()!=Message.ERROR){ %>

		<%if(!listaDocumenti.isEmpty()){
			 	String msgTotaleDoc = "";
				DocumentoTO tmp = (DocumentoTO)listaDocumenti.get(0);
				if(tmp.getTotaleDocumenti()!=null) msgTotaleDoc = " su <b>" + tmp.getTotaleDocumenti() + "</b> documenti";
			 
			 %>
			 <div class='box boxGrid shadow'>
							<div class='title'>
								<p>Sono stati individuati: <b><%=totDoc %></b> risultati<%=msgTotaleDoc %> per <i><%=nomeCategory%></i>.</p>				
							</div>
			
				 <form action="GetContent" method="post" name="myform">
					<input type='hidden' name="reference" id="reference">
					<input type='hidden' name="idoldb" id="idoldb">
					<input type='hidden' name="redirect" value="../pages/popUp/viewContent.jsp">
					<div id="resultsTable">
			<%		
						Iterator iterDoc = listaDocumenti.iterator();
						String className = null;
						int count=0;
						while(iterDoc.hasNext()){
							DocumentoTO doc = (DocumentoTO)iterDoc.next();
							String reference = doc.getReferenceDoc();
							String title = doc.getTitleDoc();
							String summary = doc.getSummaryShort();
							String idoldb = doc.getDataBase();
							String score = doc.getScore() + "%";
							if(count%2==0){
								className = "foo";
							}else{
								className = "alternateRow";
							}
			%>
							<div class="<%=className%>">
								<div class="left">
									<div>
										<h3><%=title%> (<%=score %>)</h3>
										<p><%=summary%></p>
									</div>
									
								</div>
								<div class="right">
									<img alt="Visualizza contenuto" src="<%=request.getContextPath()%>/img/icon/zoom_in.png" 
										 onclick="viewContent('<%=reference%>','<%=idoldb%>');">
								</div>
								<div class="clr"></div>
							</div>
			<%		
							count++;
						}
			%>
					</div>
				 </form>

		<%}else{%>
		<div class='box boxGrid shadow'>
				<div class='title'>
					<p>Non ci sono risultati per: <i><%=nomeCategory%></i> </p>				
				</div>
			</div>
			
		<%} %>
	<%} %>

</body>
</html>