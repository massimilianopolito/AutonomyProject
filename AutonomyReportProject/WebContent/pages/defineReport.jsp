<%@page import="utility.AppConstants"%>
<%@page import="utility.DateConverter"%>
<%@page import="Autonomy.D2Map"%>
<%@page import="model.Message"%>
<%@page import="Autonomy.ClusterData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.JobDataDescr"%>
<%
	JobDataDescr jobDataDescr = (JobDataDescr) request.getAttribute("jobDataDescr");
	JobDataDescr global = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
	
	if(jobDataDescr==null){
		jobDataDescr = new JobDataDescr();
	}

	//String dataDa = jobDataDescr.getDataInizioSelected()!=null?jobDataDescr.getDataInizioSelected():"";
	//String dataA = jobDataDescr.getDataFineSelected()!=null?jobDataDescr.getDataFineSelected():"";

	String rappr = jobDataDescr.getRappresentazione();
	String actionName = jobDataDescr.getActionName();
	String nomeJob = jobDataDescr.getTipoTicket(); 
	String classeReportDR = jobDataDescr.getClasseReport();
	if(classeReportDR.trim().length()==0 && global!=null) classeReportDR = global.getClasseReport();
	if(nomeJob.trim().length()==0 && global!=null) nomeJob = global.getTipoTicket();
	
	ArrayList<String> startsDate = null;
	ArrayList<String> endDate = null;
	
	String maxDateInzio = null;
	String minDateInizio = null;
	String maxDateFine = null;
	String minDateFine = null;
/* 	ArrayList<String> tipologiaTicket = null;
	
	try{
		tipologiaTicket = (new D2Map()).getNomeSnapshots();
	}catch(Exception e){
		tipologiaTicket = new ArrayList<String>();
		tipologiaTicket.add("FISSO");
		tipologiaTicket.add("MOBILE");
	}
 */	
 	String dataInizio = "Data elaborazione";
 	String dataFine = "Data fine elaborazione";
	boolean isVisibleDate = (jobDataDescr.getList()!=null && !jobDataDescr.getList().isEmpty()) || 
							(jobDataDescr.getExtremeDate()!=null && !jobDataDescr.getExtremeDate().isEmpty());
	boolean isVisibleEndDate = false;
	if(isVisibleDate){
		if(rappr.startsWith(AppConstants.Rappresentazione.DMAP)){
			startsDate = ((ClusterData)jobDataDescr.getList().get(0)).getDataInizioMap();
			if(startsDate!=null && !startsDate.isEmpty()){
				maxDateInzio = DateConverter.getDate(startsDate.get(0));
				minDateInizio = DateConverter.getDate(startsDate.get(startsDate.size()-1));
			}
		}else if(rappr.startsWith(AppConstants.Rappresentazione.SPECTRO)){
			dataInizio = "Data inizio elaborazione";
			
			startsDate = ((ClusterData)jobDataDescr.getList().get(0)).getDataInizioSpectro();
			if(startsDate!=null && !startsDate.isEmpty()){
				maxDateInzio = DateConverter.getDate(startsDate.get(0));
				minDateInizio = DateConverter.getDate(startsDate.get(startsDate.size()-1));
			}

			endDate =  ((ClusterData)jobDataDescr.getList().get(0)).getDataFineSpectro();
			if(endDate!=null && !endDate.isEmpty()){
				maxDateFine = DateConverter.getDate(endDate.get(0));;
				minDateFine =  DateConverter.getDate(endDate.get(endDate.size()-1));;
			}

			isVisibleEndDate = true;
		}else if(rappr.equalsIgnoreCase(AppConstants.Rappresentazione.GRAPH)){
			minDateInizio = minDateFine  = jobDataDescr.getExtremeDate().get(0);
			maxDateInzio = maxDateFine = jobDataDescr.getExtremeDate().get(1);
			isVisibleEndDate = true;
		}
	}
	
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
 		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>          
	  	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.10.2.custom.js" type="text/javascript"></script>
	  	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
	 	<script src="<%=request.getContextPath()%>/js/jquery.datepick.js" type="text/javascript"></script>
	 	<script src="<%=request.getContextPath()%>/js/jquery.datepick-it.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
 		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
 		
 		<script>
	 		$(function() {
	 			$('#dataDa').datepick({dateFormat: 'dd/mm/yyyy',  
	 								   onSelect: customRange,
	 								   maxDate: '<%=maxDateInzio%>', 
	 								   minDate:'<%=minDateInizio%>'});
	 			$('#dataA').datepick({dateFormat: 'dd/mm/yyyy',  
									  onSelect: customRange,
	 								  maxDate: '<%=maxDateFine%>', 
	 								  minDate:'<%=minDateFine%>'});
	 		}); 		

	 		function customRange(dates) {
	 			var data = dates[0];
	 			if(typeof data === "undefined") data = null;
	 		    if (this.id == 'dataDa') {
	 		    	if(data==null){
	 			    	$('#dataA').datepick('option', 'minDate', "<%=minDateFine%>");
	 		    	}else{
	 			    	$('#dataA').datepick('option', 'minDate', data);
	 		    	}
	 		    } 
	 		    else { 
	 		    	if(data==null){
	 			    	$('#dataDa').datepick('option', 'maxDate', '<%=maxDateInzio%>'); 
	 		    	}else{
	 			    	$('#dataDa').datepick('option', 'maxDate', data); 
	 		    	}
	 		    } 
	 		}

 		</script>
 	
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/datePick/jquery.datepick.css" />

		<script>
			function enabled(){
				document.getElementById("operation").value = "2";
				//document.getElementById("nomeJob").disabled = false;
			}
			
			function send(){
				$('#dataA').val(null);
				$('#dataDa').val(null);
				var rappr = document.getElementById("rappresentazione").options[document.getElementById("rappresentazione").selectedIndex].value;
				//var type = "--";
				var isSubmittable = false;
				if(<%=AppConstants.Rappresentazione.CATEGORY%>==rappr || 
				   <%=AppConstants.Rappresentazione.CATEGORY_DOMANDE%>==rappr || 
				   <%=AppConstants.Rappresentazione.CATEGORY_RISPOSTE%>==rappr || 
				   <%=AppConstants.Rappresentazione.QUERYSOCIAL%>==rappr){
					//document.getElementById("nomeJob").disabled = true;
					document.getElementById("operation").value = "1";
					isSubmittable = true;
				}else{
					enabled();
					//type = document.getElementById("nomeJob").options[document.getElementById("nomeJob").selectedIndex].value;
				}
	
				if( "--"!=rappr || isSubmittable){
					if(<%=AppConstants.Rappresentazione.DMAP%>==rappr || <%=AppConstants.Rappresentazione.DMAP_DOMANDE%>==rappr || <%=AppConstants.Rappresentazione.DMAP_RISPOSTE%>==rappr) document.getElementById("servletName").value = 'get2DMapList';
					if(<%=AppConstants.Rappresentazione.SPECTRO%>==rappr || <%=AppConstants.Rappresentazione.SPECTRO_DOMANDE%>==rappr || <%=AppConstants.Rappresentazione.SPECTRO_RISPOSTE%>==rappr) document.getElementById("servletName").value = 'getSpectroList';
					if(<%=AppConstants.Rappresentazione.QUERYSOCIAL%>==rappr) document.getElementById("servletName").value = 'ManageSocial';
					if(<%=AppConstants.Rappresentazione.GRAPH%>==rappr) document.getElementById("servletName").value = 'ManageGraph';
					document.forms[0].action="getJobList";
					document.forms[0].submit();
				}
			}
			
			function goToImage(){
				document.forms[0].action='<%=actionName%>';
				document.forms[0].submit();		
			}
		
		</script>

	</head>

	
	<body>
		<div id="wrapper">
		<%@ include file="header.jsp" %>

		<div class="box boxForm shadow">
				<div class="title">
					<p>Compilare la scheda che segue con i valori richiesti</p>
				</div>
				<div class="content">	
					<form method="post">
						<input type="hidden" id="servletName" name="servletName" />
						<input type="hidden" id="operation" name="operation"/>
						<input type="hidden" id="nomeJob" name="nomeJob" value="<%=nomeJob%>"/>
						<input type="hidden" id="classeReport" name="classeReport" value="<%=classeReportDR%>"/>
						<p>	
						<label>Modalità di rappresentazione</label>
						<select id="rappresentazione" name="rappresentazione" onchange="javascript: send();">
							<option value="--" >- Seleziona - </option>
							<%if(AppConstants.ClasseReport.SOCIAL.equalsIgnoreCase(classeReportDR)){ %>
							<option value="<%=AppConstants.Rappresentazione.DMAP_DOMANDE %>" label="2DMap Domande" <%if(AppConstants.Rappresentazione.DMAP_DOMANDE.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >2DMap Domande</option>
							<option value="<%=AppConstants.Rappresentazione.DMAP_RISPOSTE %>" label="2DMap Risposte" <%if(AppConstants.Rappresentazione.DMAP_RISPOSTE.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >2DMap Risposte</option>
							<option value="<%=AppConstants.Rappresentazione.SPECTRO_DOMANDE %>" label="Spettrografo Domande" <%if(AppConstants.Rappresentazione.SPECTRO_DOMANDE.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Spettrografo Domande</option>
							<option value="<%=AppConstants.Rappresentazione.SPECTRO_RISPOSTE %>" label="Spettrografo Risposte" <%if(AppConstants.Rappresentazione.SPECTRO_RISPOSTE.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Spettrografo Risposte</option>
							<option value="<%=AppConstants.Rappresentazione.CATEGORY_DOMANDE %>" label="Categorie Domande" <%if(AppConstants.Rappresentazione.CATEGORY_DOMANDE.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Categorie Domande</option>
							<option value="<%=AppConstants.Rappresentazione.CATEGORY_RISPOSTE %>" label="Categorie Risposte" <%if(AppConstants.Rappresentazione.CATEGORY_RISPOSTE.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Categorie Risposte</option>
							<option value="<%=AppConstants.Rappresentazione.QUERYSOCIAL %>" label="Query" <%if(AppConstants.Rappresentazione.QUERYSOCIAL.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Query</option>
							<%}else{%>
							<option value="<%=AppConstants.Rappresentazione.DMAP %>" label="2DMap" <%if(AppConstants.Rappresentazione.DMAP.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >2DMap</option>
							<option value="<%=AppConstants.Rappresentazione.SPECTRO %>" label="Spettrografo" <%if(AppConstants.Rappresentazione.SPECTRO.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Spettrografo</option>
							<option value="<%=AppConstants.Rappresentazione.HOTTOPICS %>" label="Hot Topics" <%if(AppConstants.Rappresentazione.HOTTOPICS.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Hot Topics</option>
								<%if("max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){ %>
									<option value="<%=AppConstants.Rappresentazione.GRAPH %>" label="Pallografo" <%if(AppConstants.Rappresentazione.GRAPH.equalsIgnoreCase(rappr)){%> selected="selected" <%}%> >Pallografo</option>
								<%} %>
							<%}%>
						</select>
						</p>
	
<%-- 						<label>Tipo di ticket:</label>
						<select id="nomeJob" name="nomeJob" onchange="javascript: send();" disabled="disabled">
							<option value="--" >- Seleziona - </option>
								<%
									for(String nomeCorrente: tipologiaTicket){
								%>
									<option value="<%=nomeCorrente.toUpperCase()%>" label="<%=nomeCorrente %>" <%if(nomeCorrente.equalsIgnoreCase(type)){%> selected="selected" <%}%>><%=nomeCorrente %></option>
								<%
									}
								%>
						</select>
						<br />
 --%>							
							<%
								if(isVisibleDate){
							%>
								<p>
								<label><%=dataInizio %></label>
								<input type="text" id="dataDa" name="dataDa" readonly="readonly"/>
								</p>
								
								<%if(isVisibleEndDate){ %>
									<p>
									<label><%=dataFine %></label>
									<input type="text" id="dataA" name="dataA" readonly="readonly"/>
									</p>
								<%}%>
							
								<div class="clr"></div>
								
									<input class='btnSubmit' type="button" value="Invia" onclick="javascript: goToImage()"/>
								
							<%
								}
							%>
					</form>
					<!--  <div style="text-align:center">
						//include file="back.jsp" %>
					</div>-->
				
				</div>
			</div>
			
			<div class="clr"></div>
			
								
			
			
		</div>
		
		<%
			if(AppConstants.Rappresentazione.DMAP.equalsIgnoreCase(rappr) || AppConstants.Rappresentazione.SPECTRO.equalsIgnoreCase(rappr)){
		%>
		<script>
			enabled();
		</script>
		<%
			}
		%>
	</body>
</html>

