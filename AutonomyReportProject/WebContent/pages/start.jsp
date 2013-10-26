<%@page import="java.util.Collection"%>
<%@page import="java.util.Map"%>
<%@page import="utility.AppConstants"%>
<%@page import="utility.DateConverter"%>
<%@page import="Autonomy.D2Map"%>
<%@page import="model.Message"%>
<%@page import="Autonomy.ClusterData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.JobDataDescr"%>
<%
	JobDataDescr globalEnv = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
	if(globalEnv==null){
		globalEnv = new JobDataDescr();
	}
	String ambito = globalEnv.getAmbito();
	String radiceJob = globalEnv.getRadiceJob();
	String suffissoJob = globalEnv.getSuffissoJob();
	String classeReport = globalEnv.getClasseReport();
	
	Map<String, Collection<String>> authCombo = (Map<String, Collection<String>>)request.getSession().getAttribute("authCombo");
	Collection<String> area = authCombo.get(AppConstants.NomiCombo.AREA);
	Collection<String> report = authCombo.get(AppConstants.NomiCombo.REPORT);
	Collection<String> ticket = authCombo.get(AppConstants.NomiCombo.TICKET);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
	
		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
	  	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.10.2.custom.js" type="text/javascript"></script>

	
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
	</head>

	<script>
		var removed = [];
		
		function blockChoice(comboSourceId, comboSourceValue, comboTargetId){
			var sourceValue = document.getElementById(comboSourceId).options[document.getElementById(comboSourceId).selectedIndex].value;
			document.getElementById(comboTargetId).disabled = false;
			if("--" != sourceValue){
				if(comboSourceValue==sourceValue){
					document.getElementById(comboTargetId).selectedIndex = 0;
					document.getElementById(comboTargetId).disabled = true;
				}
				
				if("ambito"==comboSourceId){
					if(sourceValue==<%=AppConstants.Ambito.CORPORATE %>){
						removed = [];
						$("#classeReport option").each(function() {
						    if($(this).val()==<%=AppConstants.ClasseReport.SOCIAL%> ||
						    		$(this).val()==<%=AppConstants.ClasseReport.ONTOLOGYTRAINER%>){
						    	var clone = $(this).clone(); 
						    	removed.push(clone);
						        $(this).remove();
						    }
						});
					}else{
						var i, currentElem;
						for( i = 0, l = removed.length; i < l; i++ ) {
						  currentElem = removed[i];
						  $("#classeReport").append(currentElem);
						}
					}
				}
			}
		}
	
		function send(){
			var alertMsg = "Di seguito l'elenco dei campi obbligatori che non risultano correttamente valorizzati: \n";
			showMsg = false;
			$('select:enabled').find('option:selected').each(function() {
	    		if("--"==$(this).val()){
		    		alertMsg = alertMsg + "'" + $(this).attr('id') + "'\n";
		    		showMsg = true;
	    		}
	    	});
			
			if(showMsg){
				alert(alertMsg);
			}else{
				var report = $( "#classeReport" ).val();
				if(<%=AppConstants.ClasseReport.AUTONOMY%>==report){
					document.forms[0].action='getJobList';
				}else if (<%=AppConstants.ClasseReport.REAL_TIME%>==report){
					document.forms[0].action='ManageRealTime';
				}else if (<%=AppConstants.ClasseReport.STRUTTURA%>==report){
					document.forms[0].action='ManageStruttura';
				}else if (<%=AppConstants.ClasseReport.SOCIAL%>==report){
					document.forms[0].action='getJobList';
				}else if(<%=AppConstants.ClasseReport.ONTOLOGYTRAINER%>==report){
					document.forms[0].action='ManageAdmin';
				}
				document.forms[0].submit();
			}
	}
		
	</script>
	
	<body>
		<div id="wrapper">
			<%@ include file="header.jsp" %>
			
			<div class="box boxForm shadow">
				<div class="title">
					<p>Compilare la scheda che segue con i valori richiesti</p>
				</div>
				<div class="content">
					<form method="post">
						<input type="hidden" id="operation" name="operation" value="0"/>
						<p>
						<label>Area</label>
						<%if(AppConstants.isSingleChoice(area)){ %>
							<input type="hidden" id="ambito" name="ambito" value="<%=area.iterator().next() %>" />
							<label><%=AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, area.iterator().next()) %></label>						
						<%}else{ %>
							<select id="ambito" name="ambito" onchange="blockChoice('ambito','<%=AppConstants.Ambito.CORPORATE%>','radiceJob');">
								<option id="Area" value="--" label=" - Seleziona - " > - Seleziona - </option>
								<%for(String currentValue: area){ 
									String label = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, currentValue);
								%>
									<option id="<%=currentValue %>" value="<%=currentValue %>" <%if(currentValue.equalsIgnoreCase(ambito)){%> selected="selected" <%}%>  ><%=label %></option>
								<%} %>
							</select>
						<%} %>
						</p>
						<p>
						<label >Rete</label>
						<%if(AppConstants.isDisabled(ticket)){ %>
							<input type="hidden" id="radiceJob" name="radiceJob" />
							<label>--</label>						
						<%}else{ %>
							<select id="radiceJob" name="radiceJob">
								<option id='Rete' value="--" label=" - Seleziona - " > - Seleziona - </option>
								<%for(String currentValue: ticket){ 
									String label = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, currentValue);
								%>
									<option id="<%=currentValue %>" value="<%=currentValue %>" <%if(currentValue.equalsIgnoreCase(radiceJob)){%> selected="selected" <%}%>  ><%=label %></option>
								<%} %>
							</select>
						<%} %>
						</p>
						<p>
						<label >Tipologia Ticket</label>
						<select id="suffissoJob" name="suffissoJob">
							<option id="Tipologia Ticket" value="--" label=" - Seleziona - " > - Seleziona - </option>
							<option value="<%=AppConstants.categoriaTicket.CASE %>" <%if(AppConstants.categoriaTicket.CASE.equalsIgnoreCase(suffissoJob)){%> selected="selected" <%}%> ><%=AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, AppConstants.categoriaTicket.CASE) %></option>
							<option value="<%=AppConstants.categoriaTicket.INTERAZIONI %>" <%if(AppConstants.categoriaTicket.INTERAZIONI.equalsIgnoreCase(suffissoJob)){%> selected="selected" <%}%>><%=AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, AppConstants.categoriaTicket.INTERAZIONI) %></option>
						</select>
						</p>
						<p>
						<label >Tipo Report</label>
						<%if(AppConstants.isSingleChoice(report)){ %>
							<input type="hidden" id="classeReport" name="classeReport" value="<%=report.iterator().next() %>" />
							<label><%= AppConstants.getLabelFromIndex(AppConstants.classeReportLabel, report.iterator().next())%></label>						
						<%}else{ %>
							<select id="classeReport" name="classeReport" onchange="blockChoice('classeReport','<%=AppConstants.ClasseReport.SOCIAL%>','suffissoJob');">
								<option id="Tipo Report" value="--" label=" - Seleziona - " > - Seleziona - </option>
								<%for(String currentValue: report){ 
									String label = AppConstants.getLabelFromIndex(AppConstants.classeReportLabel, currentValue);
								%>
									<option id="<%=currentValue %>" value="<%=currentValue %>" <%if(currentValue.equalsIgnoreCase(classeReport)){%> selected="selected" <%}%>  ><%=label %></option>
								<%} %>
							</select>
						<%} %>
						</p>
											
						<input type="button" class="btnSubmit" value="Invia" onclick="javascript: send();"/>
						
					</form>
				</div>
			</div>	
		
		</div>
	
	</body>
	
	<%if(classeReport!=null && classeReport.trim().length()!=0){ %>
		<script>
			blockChoice('classeReport','<%=AppConstants.ClasseReport.SOCIAL%>','suffissoJob');
		</script>
	<%} %>
	
	<%if(ambito!=null && ambito.trim().length()!=0){ %>
		<script>
			blockChoice('ambito','<%=AppConstants.Ambito.CORPORATE%>','radiceJob');
		</script>
	<%} %>
	
</html>