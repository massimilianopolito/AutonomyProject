<%@page import="utility.AppConstants"%>
<%@page import="model.JobDataDescr"%>
<%
	JobDataDescr globalEnvIB = (JobDataDescr)request.getSession().getAttribute("globalEnvironment");
	if(globalEnvIB!=null){
		String areaIB = AppConstants.getLabelFromIndex(AppConstants.ambitoLabel, globalEnvIB.getAmbito());
		String reteIB = AppConstants.getLabelFromIndex(AppConstants.tipoTicketLabel, globalEnvIB.getRadiceJob());
		String categoriaIB = AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, globalEnvIB.getSuffissoJob());
		if(categoriaIB.trim().length()==0) categoriaIB = "--";
		String classeReportIB = AppConstants.getLabelFromIndex(AppConstants.classeReportLabel, globalEnvIB.getClasseReport());
	%>

					
					<div class="infoBox">
						<label>Area:</label>
						<span><%=areaIB %></span>

						<label>Rete:</label>
						<span><%=reteIB %></span>

						<label>Tipologia Ticket:</label>
						<span><%=categoriaIB %></span>

						<label>Tipo Report:</label>
						<span><%=classeReportIB %></span>
					</div>
				
				
<% 	
	}
%>	