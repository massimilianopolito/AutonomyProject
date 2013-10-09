<%@page import="utility.NumberGroupingSeparator"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="Autonomy.Bean2DMapTO"%>
<%@page import="java.util.ArrayList"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String path = request.getContextPath();
	String dataDa = request.getParameter("dataDa"); 
	String nomeJob = request.getParameter("nomeJob");
	//F58233
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
			$('.map').maphilight({fade: false, strokeColor: 'FF0000', alwaysOn: true});
		});
	</script>
	<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script> 
	       
	<script>
		function makevisible(x,y){
			document.location.href = "<%=path%>" + "/pages/snapShot/view2DMap.jsp?rappresentazione=" + <%=AppConstants.Rappresentazione.DMAP%> + "&x="+x+"&y="+y +"&dataDa=<%=dataDa%>"+"&nomeJob=<%=nomeJob%>" ;
		}
	
	</script>
</head>
<%
	String xValue = request.getParameter("x"); 
	String yValue = request.getParameter("y");

	String dataA = dataDa; 
	
	ArrayList<Bean2DMapTO> jobDescription = (ArrayList<Bean2DMapTO>) request.getSession().getAttribute("jobDescription");
	
	List result = null;
	String clusterName = null;
	boolean viewResult = false;
	
/* 	if("max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){
		xValue = "0";
		yValue = "0";
	}
 */	
 	String msgTotaleDoc = "";
 	String totDocMap = "";
	if(xValue!=null&&yValue!=null){
		viewResult = true;
		Iterator<Bean2DMapTO> iterJobDescription = jobDescription.iterator();
		Bean2DMapTO currentBean = null;
		while(iterJobDescription.hasNext()){
			currentBean = iterJobDescription.next();
			if(currentBean.getX() == Integer.parseInt(xValue) && currentBean.getY() == Integer.parseInt(yValue) ) break;
		}
		
		clusterName = currentBean.getClusterName();
		result = currentBean.getResultList();

		if(!result.isEmpty()){
			DocumentoTO doc = (DocumentoTO)result.get(0);
			if(doc.getTotaleDocumenti()!=null) msgTotaleDoc = " su <b>" + doc.getTotaleDocumenti() + "</b> documenti";
			totDocMap = NumberGroupingSeparator.formatNumber(result.size());
		}
	}


 %>
<script>
	function makevisible(x,y){
		document.location.href = "<%=path%>" + "/pages/snapShot/view2DMap.jsp?rappresentazione=" + <%=AppConstants.Rappresentazione.DMAP%> + "&x="+x+"&y="+y +"&dataDa=<%=dataDa%>"+"&nomeJob=<%=nomeJob%>" ;
	}

</script>
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
<%-- 							<%if(!"max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){ %>
 --%>							<map name="imagemap">
					 		<%
									Iterator<Bean2DMapTO> iter = jobDescription.iterator();
									while(iter.hasNext()){
										Bean2DMapTO currentObj = (Bean2DMapTO)iter.next();
										String coord = currentObj.getX() + "," + currentObj.getY() + ",5";
										String x = currentObj.getX() + "";
										String y = currentObj.getY() + "";
										String alt = "NOME: " + currentObj.getClusterName() + "\n" + 
													 "NUMERO RISULTATI: " + currentObj.getNumDocs() + "\n" ;
										%>
											<area shape="circle" coords="<%=coord%>" href="javascript: makevisible('<%=x%>','<%=y%>')" title="<%=alt%>" style=""/>
										<% 
									}
								 %>			
					
								</map>
								<img id="dueDMap" src="<%=path%>/pages/snapShot/getSnapShot.jsp?action=ClusterServe2DMap&dataFrom=<%=dataDa%>&dataTo=<%=dataA%>&sourceJobName=<%=nomeJob.toUpperCase()%>_CLUSTERS" class="map" usemap="#imagemap"/>
<%-- 							<%} %>
							
 --%>							<table id="legendaMappa">
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
					
									<p>Nel cluster sono stati individuati: <b><%=totDocMap %></b> risultati<%=msgTotaleDoc %>. </p>
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
									<input type='hidden' name="rappresentazione" value="<%=AppConstants.Rappresentazione.DMAP%>"/>
									
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
						%>
						</div>
						<div class='clr'></div>
						</td>
					</tr>
				</table>	
				
				
					
			</div>
		
</body>
</html>