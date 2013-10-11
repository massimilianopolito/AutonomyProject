<%@page import="utility.NumberGroupingSeparator"%>
<%@page import="model.ClusterFronEnd"%>
<%@page import="model.JobDataDescr"%>
<%@page import="java.util.Collection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%

		JobDataDescr jobDataDescr = (JobDataDescr) request.getAttribute("jobDataDescr");
		Collection<ClusterFronEnd> list = jobDataDescr.getClusterList();
	
		String dataSelezionata = (String)request.getAttribute("dataElaborazioneScelta");
		String[] date = (String[])request.getSession().getAttribute("HT_date");
		String min = date[0];
		String max = date[date.length-1];
		if(dataSelezionata==null || dataSelezionata.isEmpty()) dataSelezionata = max;

		String nomeTabellaDoc = (String)request.getAttribute("nomeTabellaDoc");
		
		String rappresentazione = jobDataDescr.getRappresentazione(); 

	%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
	<title>Insert title here</title>
	<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>          
  	<script src="<%=request.getContextPath()%>/js/jquery-ui.custom.js" type="text/javascript"></script>
  	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
  	<script src="<%=request.getContextPath()%>/js/jquery.dynatree-1.2.4.js" type="text/javascript"></script>
 	<script src="<%=request.getContextPath()%>/js/jquery.datepick.js" type="text/javascript"></script>
 	<script src="<%=request.getContextPath()%>/js/jquery.datepick-it.js" type="text/javascript"></script>

	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>

  	<link href="<%=request.getContextPath()%>/css/skin/ui.dynatree.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath() %>/css/global.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/css/datePick/jquery.datepick.css" rel="stylesheet" type="text/css"  />

	<script type="text/javascript">
		$(function() {
			$('#dataElaborazione').datepick({
		        onSelect: function(date) {
		            document.getElementById("dataElaborazioneScelta").value=$("#dataElaborazione").datepicker().val();
		            document.getElementById("formTree").submit();
		        },
				dateFormat: 'dd/mm/yyyy',  
				maxDate: '<%=max%>', 
				minDate:'<%=min%>'});
		}); 		
		
		 $(function(){
			 $("#tree").dynatree({
				 selectMode: 1,
			     onClick: function(node, event) {
	 	         	if( node.getEventTargetType(event) == "title" ){
	 	         		if( node.data.href ){
	 	         			$("[name=contentFrame]").attr("src", node.data.href);
	 	         		}
	 	         	}
			     },
			     onKeydown: function(node, event) {
			          if( event.which == 32 ) {
			            node.toggleSelect();
			            return false;
			          }
			     }
			 });
			 <%
			  	for(ClusterFronEnd currentCluster: list){
			  		String id = currentCluster.getID();
			  		String name = currentCluster.getNome();
			  		String idQuery = currentCluster.getIdQuery();
			  		String size = currentCluster.getNumberOfDocs()!=null?NumberGroupingSeparator.formatNumber(currentCluster.getNumberOfDocs()):null;
			  		boolean isLinked = currentCluster.isLinked();
			  		if(idQuery==null){%>
		  				var node = $("#tree").dynatree("getRoot");
			  		<%}else{%>
			  			var node = $("#tree").dynatree("getTree").selectKey("-1");
			  			//var parentTitle = node.data.title;
			  		<%}%>
		  			 node.addChild({
		  				 <%if(isLinked){%>href: "ManageCluster?idCluster=<%=id%>&nomeCluster=<%=name%>&nomeTabella=<%=nomeTabellaDoc%>&dataSelezionata=<%=dataSelezionata%>", <%}%>
		  				 <%if(size!=null){%>
		  				 title:"<%=name%> (<%=size%>)",
		  				 <%}else{%>
		  				 title:"<%=name%>",
		  				 <%}%>
	                     tooltip: "<%=name%>",
	                     key: "<%=id%>"
		             });
			  	<%}%>
		 });

	</script>
</head>
<body>
	<div id="wrapper">
		<%@ include file="../header.jsp" %>
		<table id='tbHotTopics'>
			<tr>
				<td width="20%" style="vertical-align: top;">
					<form name="formTree" id="formTree" method="post" action="getJobList">
						<input type="hidden" id="dataElaborazioneScelta" name="dataElaborazioneScelta"/>
						<input type="hidden" id="operation" name="operation"/>
						<input type="hidden" id="rappresentazione" name="rappresentazione" value="<%=rappresentazione%>"/>
						<p>
							<label>Data Importazione:</label> 
							<input type="text" id="dataElaborazione" name="dataElaborazione" readonly="readonly" value="<%=dataSelezionata%>"/>
						</p>
						<div id="tree"> </div>
					</form>
				</td>
				<td width="80%">
	                <iframe name="contentFrame" scrolling="auto"
	                		width="100%" height="400px"
	                        marginheight="0" marginwidth="0" frameborder="0">
	                   <p>Your browser does not support iframes</p>
	                </iframe>
				</td>
			</tr>
		</table>	
	</div>

	<div style="text-align:center">
		<%@ include file="../back.jsp" %>
	</div>
	
</body>
</html>