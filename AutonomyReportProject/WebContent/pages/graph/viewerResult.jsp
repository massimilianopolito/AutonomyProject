<%@page import="utility.AppConstants"%>
<%@page import="utility.NumberGroupingSeparator"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
 		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script> 
	  	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.10.2.custom.js" type="text/javascript"></script>
	  	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script> 
	 	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
 		
	</head>
	<%
		String nome_Cluster = (String)request.getAttribute("nomeCluster");
		String data = (String)request.getAttribute("data");
		String totDocs = (String)request.getAttribute("numDocInRange");
	
		List<DocumentoTO> result = (ArrayList<DocumentoTO>) request.getAttribute("result");
	 	String msgTotaleDoc = "";
	 	String totDocMap = "";
	 	String msg = "Risultati ottenuti per: \""+ nome_Cluster + "\" in data: " + data;

		if(result!=null && !result.isEmpty()){
			msgTotaleDoc = " su <b>" + NumberGroupingSeparator.formatNumber(totDocs) + "</b> documenti";
			totDocMap = NumberGroupingSeparator.formatNumber(result.size());
			msg = msg + ". Nel cluster sono stati individuati: <b>"+ totDocMap + "</b> risultati" + msgTotaleDoc;
		}


	%>
	
	<body>
		<div id="wrapper">
			<a id="result"></a>
			<div class="box boxGrid shadow">
				<div class="title">
					<p><%=msg %></p>
				</div>
			<%
					if(result!=null){
			%>
				
					 <form action="GetContent" method="post" name="myform">
						<input type='hidden' name="reference" id="reference"/>
						<input type='hidden' name="idoldb" id="idoldb"/>
						<input type='hidden' name="redirect" value="../pages/popUp/viewContent.jsp"/>
						<input type='hidden' name="rappresentazione" value="<%=AppConstants.Rappresentazione.GRAPH%>"/>
						
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
										<a href="#">
										<img alt="Visualizza contenuto" src="<%=request.getContextPath()%>/img/icon/zoom_in.png" 
											 onclick="viewContent('<%=reference%>','<%=idoldb%>');"/></a>
									</div>
									<div class="clr"></div>
								</div>
				<%		
								count++;
							}%>
						</div>
					 </form>
				<%}
			%>
			</div>
		</div>
	</body>
</html>
