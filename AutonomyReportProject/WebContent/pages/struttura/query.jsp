<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="utility.NumberGroupingSeparator"%>
<%@page import="model.QueryObject"%>
<%@page import="model.DatiQuery"%>
<%@page import="java.util.Collection"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String IDH ="";
	String nomeQueryH="";
	String testoH ="";
	String relevance ="";
	String numRisultati ="";
	String dataDa = "";
	String dataA = "";
	String gap = "";
	String primo = "";
	String secondo = "";
	String terzo = "";
	String op ="";
	Collection<String> firstComboValues;
	List<DocumentoQueryTO> listaDoc;
	String pag = request.getSession().getAttribute("pagina").toString();
	System.out.println("pagina: "+pag);
	String penthaoUrl = (String) request.getAttribute("penthaoReportUrl");

/* 	Le uniche differenze tra pubblico e privato sono i nomi degli oggetti in REQUEST?
 */	
	String queryList = null;
	String listFieldvalue = null;
	String listaRisultatiStruttura = null;
	String queryObjectStruttura = null;

	Collection<QueryObject> queryLists = null;
	Collection<DatiQuery> datiQueryList =null;
	QueryObject queryObject = null;
			
	if(pag.equalsIgnoreCase("M"))
	{	
		queryList = "queryList";
		listFieldvalue = "listFieldvalue";
		listaRisultatiStruttura = "listaRisultatiStruttura";
		queryObjectStruttura = "queryObjectStruttura";
		

	}
	else
	{	
		
		queryList = "queryListPublic";
		listFieldvalue = "listFieldvaluePub";
		listaRisultatiStruttura = "listaRisultatiStruttura";
		queryObjectStruttura = "queryObjectStrutturaPub";

	}	
	
	queryLists = (Collection<QueryObject>) request.getAttribute(queryList);
	datiQueryList = (Collection<DatiQuery>) request.getSession().getAttribute(listFieldvalue);
	listaDoc = (ArrayList<DocumentoQueryTO>) request.getSession().getAttribute(listaRisultatiStruttura);
	queryObject = (QueryObject) request.getSession().getAttribute(queryObjectStruttura);

	JobDataDescr jobDataDescr = (JobDataDescr) request.getSession().getAttribute("globalEnvironment");
	firstComboValues = jobDataDescr.getComboValues();
	String area = jobDataDescr.getAmbito();

	op = (String)request.getAttribute("operation");
	if(op == null) op="";
	//QueryObject queryObject = (QueryObject) request.getAttribute("queryObject");
	if(queryObject == null) queryObject = new QueryObject();
	if(listaDoc == null) listaDoc = new ArrayList<DocumentoQueryTO>();
	IDH = queryObject.getID()==null?"":queryObject.getID();
	nomeQueryH = queryObject.getNomeQuery()==null?"":queryObject.getNomeQuery();
	testoH = queryObject.getTesto()==null?"":queryObject.getTesto();
	relevance = queryObject.getRelevance();
	numRisultati = queryObject.getNumRisultati()==null?"":queryObject.getNumRisultati();
	
	String myIdDiv = "";
	if(IDH!=null){
		if(pag.equalsIgnoreCase("M")){
			myIdDiv = "private" + IDH;
		}else{
			myIdDiv = "public" + IDH;
		}
	}

	if(datiQueryList!=null && !datiQueryList.isEmpty())
	{
		for(DatiQuery currentData: datiQueryList){
		
			if(currentData.getIdCampo().equalsIgnoreCase("DATA_CREAZIONE_DA"))
				dataDa = currentData.getValoreCampo();
			if(currentData.getIdCampo().equalsIgnoreCase("DATA_CREAZIONE_A"))
				dataA = currentData.getValoreCampo();
			if(currentData.getIdCampo().equalsIgnoreCase("GAP"))
				gap = currentData.getValoreCampo();
			if(currentData.getIdCampo().equalsIgnoreCase("first"))
				primo = currentData.getValoreCampo();
			if(currentData.getIdCampo().equalsIgnoreCase("second"))
				secondo = currentData.getValoreCampo();
			if(currentData.getIdCampo().equalsIgnoreCase("third"))
				terzo = currentData.getValoreCampo();
		}
	}	

	//JobDataDescr jobDataDescr = (JobDataDescr) request.getSession().getAttribute("globalEnvironment");
	//firstComboValues = new ArrayList<String>();

%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
	<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.10.2.custom.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script>
  	<script src="<%=request.getContextPath()%>/js/calendar.js" type="text/javascript"></script>
  	<script src="<%=request.getContextPath()%>/js/troika.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.datepick.js" type="text/javascript"></script>
 	<script src="<%=request.getContextPath()%>/js/jquery.datepick-it.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/datePick/jquery.datepick.css" />
<%-- 	<link href="<%=request.getContextPath() %>/css/jquery-ui-1.10.2.custom.css" rel="stylesheet" type="text/css" />
 --%>	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
	<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
	<title>Login - D-CUBE | Digital Customer Behaviour</title>
	
</head>
	<script>
		function reset(){
			document.getElementsByName("operation").value = "4";
			document.forms[0].submit();
		}
		
		function sendAndResetButton(operationValue){
			
			/*if('4'==operationValue){
				document.formStruct.reset();
				try{
					document.contentRes.document.body.innerHTML = "";
				}catch(e){
					window.contentRes.document.body.innerHTML = "";
				}
			}*/
			
			
			document.formStruct.operation.value=operationValue;
			document.formStruct.submit();
			//document.forms[1].operation.value=operationValue;
			//document.forms[1].submit();
		}
		
		function manageField(tabIndex, deleteValue){
        	$('#queryFormData input, #queryFormData select, #queryFormData textarea, #queryFormData div').each(
        		    function(index){ 
        		    	var input = $(this);
        		    	type = input.attr('type') ;
        		    	tag = input.prop("tagName");
        		    	id = input.attr('id');
        		    	if(type=='hidden' || type=='button') return;
        		    	if(tabIndex==1){
            		    	input.attr('disabled','disabled');
            		    	if(id.indexOf('pvt')!=-1 && tag=="DIV") $('#' + id).hide(); 
            		    	if(id.indexOf('pbl')!=-1 && tag=="DIV") $('#' + id).show(); 
            		    	$('#title').html("<p>Dettaglio della Query Pubblica selezionata</p>");
             		    	<%-- $('#private<%=IDH%>').css( "border", "" ); --%>
        		    	}else{
        		    		input.removeAttr('disabled');
            		    	if(id.indexOf('pvt')!=-1 && tag=="DIV") $('#' + id).show(); 
            		    	if(id.indexOf('pbl')!=-1 && tag=="DIV") $('#' + id).hide(); 
            		    	$('#title').html("<p>Compilare la scheda che segue con i valori richiesti</p>");
            		    	<%-- $('#public<%=IDH%>').css( "border", "" ); --%>
        		    	}
        		    	if(deleteValue){
							if(id=='second' || id=='third'){
	            			    $('#' + id + ' option').each(function(){$('#' + id + ' option').remove();});
	            			    $('#' + id).append('<option value="--" selected="selected">- Selezionare -</option>');
							}
        		    		input.val(null);
            		        $('#resultDiv').hide();
            		        $('#pblBtnDetail').attr("disabled","disabled");
        		    	}

        		    	//console.log("ID: " + input.attr('id') + " TYPE: " + input.attr('type') + " DISABLED: " + input.prop("disabled") );
        		    }
        		);
		}

		$(function() {
			<%if(pag.equalsIgnoreCase("P")){ %>
				manageField(1, false);
			<%}%>
		
		    $( "#tabs" ).tabs();
		    
		    $('#tabs').on('tabsactivate', function(event, ui) {
		        var selectedTab = ui.newTab.index();
	        	manageField(selectedTab, true);
		    });
		});
		
		$(function() {
			$( "#<%=myIdDiv%>" ).css( "border", "2px solid #ffaa1b" );
		  });
		
	</script>

<body>
	<div id="wrapper">
		<%@ include file="../header.jsp" %>
		<table id='tbStruttura'>
		<tr>
			<td>
			
			</td>
		</tr>
		<tr>
			<td>
				<%@ include file="tabStrutturaQuery.jsp" %>
		    	<div class="clr"></div>
								
			</td>
			<td>
						<div class='box boxForm shadow' style="height:500px;overflow:auto">
							<div class='title' id="title">
								<p>Compilare la scheda che segue con i valori richiesti</p>
							</div>
					<div class='content'>
					 <!--  id="formStruct"  target="contentRes"-->
			<form action="ManageStruttura" method="post" name="formStruct" id="queryFormData">
				<input type="hidden" name="operation" name="operation"/>
				<input type='hidden' name="ID" value="<%=IDH%>"/>
				<input type="hidden" id="penthaoUrl" name="penthaoUrl" />
					<p>
					<label>Nome Query: </label><input type="text" name="nomeQuery" id="nomeQuery" maxlength="250" value="<%=nomeQueryH%>"/>
					</p>
					<p>
					<label>Testo di ricerca: </label><textarea name="testo" id="testo" style="width:400px" rows="3"><%=testoH%></textarea>
					</p>
					<p>
					<label>Grado di rilevanza: </label><select <%if(pag.equalsIgnoreCase("P")){ %> disabled <%}%> name="relevance" id="relevance">
						<%for(int i=40; i<=90; i++){
							String comparingValue = i + "";
						%>
						<option value="<%=i%>" <%if(comparingValue.equalsIgnoreCase(relevance)){ %> selected="selected" <%}%>><%=i %></option>
						<%}%>
					</select>
					</p>

					<div id="pvtNumRisDiv">
						<p>
						<label>Numero di risultati:</label><select name="numRisultati" id="numRisultati">
							<option value="200" <%if("200".equalsIgnoreCase(numRisultati)){ %> selected="selected" <%}%>>200</option>
							<option value="500" <%if("500".equalsIgnoreCase(numRisultati)){ %> selected="selected" <%}%>>500</option>
							<%for(int i=1000; i<=10000; i=i+1000){
								String comparingValue = i + "";
							%>
							<option value="<%=i%>" <%if(comparingValue.equalsIgnoreCase(numRisultati)){ %> selected="selected" <%}%>><%=i %></option>
							
							<%}%>			
						</select>
						</p>
					</div>

					<p>
					<label>Data Creazione DA: </label><input type="text" name="DATA_CREAZIONE_DA" id="DATA_CREAZIONE_DA" maxlength="200" value="<%=dataDa%>" readonly="readonly" />
					</p>
					<p>
					<label>Data Creazione A: </label><input type="text" name="DATA_CREAZIONE_A" id="DATA_CREAZIONE_A" maxlength="200" value="<%=dataA%>" readonly="readonly" />
					</p>
					<p>
					<label>Rolling date: </label><select name="GAP" id="GAP">
								<option value="--">- Selezionare - </option>
								<%
									for(int i=1; i<=60; i++){
										String comparingValue = i + "";
								%>
									<option value="<%=i%>" <%if(comparingValue.equalsIgnoreCase(gap)){ %> selected="selected" <%}%>><%=i %></option> 
								<%		
									}
									%>
							</select>
					</p>
						<p>
						<label>Motivo: </label><select  name="first" id="first">
							<option value="--">- Selezionare - </option>
							<%
								for(String value: firstComboValues){
									String selected="";
									if(value.equalsIgnoreCase(primo)) selected="selected";
							%>
									<option value="<%=value%>" <%if(!selected.isEmpty()){ %> selected="<%=selected%>" <%}%>><%=value%></option> 
							<%		
								}
							%>
<%-- 							<%
								if(primo!=""){
							%>		
								<option value="<%=primo%>" selected="selected"><%=primo%></option>
							<%}else{%>
							<option value="--">- Selezionare - </option>
							<%} %>
 --%>						</select>
						</p>
						<p>
						<label>Argomento: </label><select  name="second" id="second">
							<%		
							if(secondo!=""){
							%>
							<option value="<%=secondo%>" selected="selected"><%=secondo%></option>
							<%}else{%>
							<option value="--">- Selezionare - </option>
							<%} %>
						</select>
						</p>
						<p>
	
						<label>Specifica: </label><select  name="third" id="third">
							
							<%		
							if(terzo!=""){
							%>
							<option value="<%=terzo%>" selected="selected"><%=terzo%></option>
							<%}else{%>
							<option value="--">- Selezionare - </option>
							<%} %>
						</select>
						</p>
				 		
						<div id="pvtBtnDiv">
							<input type="button" value="Nuovo" onclick="sendAndResetButton('4');"/>
							<input type="button" value="Esegui" onclick="sendAndResetButton('6');"/>
							<%if(!listaDoc.isEmpty()){ %>
								<input type="button" name="grafico" value="Summary Grafico" onclick="apri('Report','<%=penthaoUrl%>');"/>	
							
							<%} %>
							<input type="button" value="Salva" onclick="sendAndResetButton('1');"/>
						</div>

						<div id="pblBtnDiv" style="display:none;">
							<input id="pblBtnDetail" type="button" value="Visualizza Dettagli" onclick="sendAndResetButton('11');" />
						</div>
				
			</form>
		</div>
		</div>
	</td>
					</tr>
		<tr>
		<td colspan="2">
			<%if(op.equals("6")||op.equals("11")){%>
			<div class='box boxGrid noH shadow' id="resultDiv"> 
							
				 	<%@ include file="queryTestStruttura.jsp" %>
		    	</div>
		    	
			<%}%>			
		</td>
		</tr>			
				</table>
				</div>
</body>
</html>
