<%@page import="utility.NumberGroupingSeparator"%>
<%@page import="Autonomy.D2Map"%>
<%@page import="Autonomy.SpectroTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String path = request.getContextPath();

	String X1 = request.getParameter("x1");
	String Y1 = request.getParameter("y1");
	String X2 = request.getParameter("x2");
	String Y2 = request.getParameter("y2");

	String dataDa = request.getParameter("dataDa"); 
	String dataA = request.getParameter("dataA"); 
	String nomeJob = request.getParameter("nomeJob"); 
	
%>
<head>
	<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    	<script src="<%=request.getContextPath()%>/js/jquery-ui.custom.js" type="text/javascript"></script>
    	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.maphilight.js"></script>
		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
 		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
	<script type="text/javascript">
		$(function() {
			$('.map').maphilight({fade: false, strokeColor: 'F58233'});
		});
	</script>
	<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script>          

	<script>
		function makevisible(x1, y1, x2, y2){
			document.location.href = "<%=path%>" + "/pages/snapShot/viewSpectro.jsp?x1=" + x1 + "&y1=" + y1 + "&x2=" + x2 + "&y2=" + y2 +"&dataDa=<%=dataDa%>"+"&dataA=<%=dataA%>"+"&nomeJob=<%=nomeJob%>" ;
		}
	</script>
</head>
<%
	ArrayList<SpectroTO> jobDescription = (ArrayList<SpectroTO>) request.getSession().getAttribute("jobDescription");
	
	List result = null;
	String clusterName = null;
	boolean viewResult = false;
 	String msgTotaleDoc = "";
 	String totDocSG = "";
 	if(X1!=null && Y1!=null && X2!=null && Y2!= null){
		viewResult = true;
		Iterator<SpectroTO> iterJobDescription = jobDescription.iterator();
		SpectroTO currentBean = null;
		while(iterJobDescription.hasNext()){
			currentBean = iterJobDescription.next();
			if(currentBean.getX1().equalsIgnoreCase(X1) && currentBean.getY1().equalsIgnoreCase(Y1) &&
					currentBean.getX2().equalsIgnoreCase(X2) && currentBean.getY2().equalsIgnoreCase(Y2) 	) break;
		}
		
		D2Map d2Map = new D2Map();
		currentBean = d2Map.SGDocs(currentBean);
		clusterName = currentBean.getClusterTitle();
		result = currentBean.getDocList();

		if(!result.isEmpty()){
			DocumentoTO doc = (DocumentoTO)result.get(0);
			if(doc.getTotaleDocumenti()!=null) msgTotaleDoc = " su <b>" + doc.getTotaleDocumenti() + "</b> documenti";
			totDocSG = NumberGroupingSeparator.formatNumber(result.size());
		}
	}


 %>
<body>
	<div id="wrapper">
		<%@ include file="../header.jsp" %>
		<table id='tb2DMap'>
					<tr>
						<td>
						<div class='box boxMap shadow'>
							<div class='title'>	
				<%if(viewResult){ %>
					<p>CLUSTER: "<%=clusterName %>"</p>
					
				<%} %>
				</div>
							<map name="imagemap">
					 		<%
									Iterator<SpectroTO> iter = jobDescription.iterator();
									while(iter.hasNext()){	
										SpectroTO currentBean = (SpectroTO)iter.next();
										// Coordinate per la creazione dell'AreaShape
										int y2Extended = Integer.parseInt(currentBean.getY2());
										if(currentBean.getY2().equals(currentBean.getY1())) y2Extended = y2Extended + 5;
										String coord = currentBean.getX1() + "," + currentBean.getY1() + "," +currentBean.getX2() + "," + y2Extended;
										// Valore dell'Alt del link
										String alt = "NOME: " + currentBean.getClusterTitle() + "\n" + 
													 "NUMERO RISULTATI: " + currentBean.getNumDocs() + "\n" ;
										// Parameter per ottenere l'elenco dei docs del Cluster
										String dateFrom = currentBean.getFromDate();
										String dateTo = currentBean.getToDate();
										String id = currentBean.getClusterId();
										String setName = currentBean.getDataSetName();
										%>
											<area shape="rect" coords="<%=coord%>" href="javascript: makevisible('<%=currentBean.getX1()%>','<%=currentBean.getY1()%>','<%=currentBean.getX2()%>','<%=currentBean.getY2()%>')" title="<%=alt%>" style=""/>
									<%}%>			
					
							</map>
					
							<img id="spectro" src="<%=path%>/pages/snapShot/getSnapShot.jsp?action=ClusterSGPicServe&dataFrom=<%=dataDa%>&dataTo=<%=dataA%>&sourceJobName=<%=nomeJob.toUpperCase()%>SG" class="map" usemap="#imagemap"/>
						<table id="legendaMappa">
									<tr>
										<th colspan="2">Legenda</th>
									<tr>
								<td class="verde"></td>
								<td>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac arcu vitae neque varius iaculis.</td>
							</tr>
							<tr>
								<td class="giallo"></td>
								<td>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac arcu vitae neque varius iaculis.</td>
							</tr>
							<tr>
								<td class="arancione"></td>
								<td>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac arcu vitae neque varius iaculis.</td>
							</tr>
							<tr>
								<td class="nero"></td>
								<td>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac arcu vitae neque varius iaculis.</td>
							</tr>
						</table>
						<div style="text-align:center">
					<%@ include file="../back.jsp" %>
				</div>
 								</div>
 								
 							</td>
						
						<td>
						<div class='box boxGrid shadow'>
							<div class='title'>
							<%if(viewResult){ %>
					
									<p>Nel cluster sono stati individuati: <b><%=totDocSG %></b> risultati<%=msgTotaleDoc %>. </p>
								<%} %>
							</div>
						<%
							if(viewResult){
								if(result!=null){
						%>
							
								 <form action="GetContent" method="post" name="myform">
									<input type='hidden' name="reference" id="reference"/>
									<input type='hidden' name="idoldb" id="idoldb"/>
									<input type='hidden' name="redirect" value="../popUp/viewContent.jsp"/>
			
									<div id="resultsTable">
								 
							<%		
										Iterator iterDoc = result.iterator();
										String className = null;
										int count=0;
										while(iterDoc.hasNext()){
											DocumentoTO doc = (DocumentoTO)iterDoc.next();
											int score = Integer.parseInt(doc.getScore()) ;
											//String reference = StringParser.findReplace(doc.getReferenceDoc(), " ", "%20");
											String reference = doc.getReferenceDoc();
											String title = doc.getTitleDoc();
											String summary = doc.getSummaryShort();
											String idoldb = doc.getDataBase();
											if(count%2==0){
												className = "foo";
											}else{
												className = "alternateRow";
											}
							%>
											<div  class="<%=className%>">
												<div class="left">
													<div>
														<h3><%=title%></h3>
														<p><%=summary%></p>
													</div>
													
												</div>
												<div class="right">
													<img alt="Visualizza contenuto" src="<%=request.getContextPath()%>/img/icon/zoom_in.png" 
														 onclick="viewContent('<%=reference%>','<%=idoldb%>');"/>
												</div>
												<div class="clr"></div>
											</div>
							<%		
											count++;
										}%>
									</div>
								 </form>
							<%}
							}
						%></div>
						<div class='clr'></div>
						</td>
					</tr>
				</table>
				</div>
</body>
</html>