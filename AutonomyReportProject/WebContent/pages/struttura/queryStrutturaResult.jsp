<%@page import="model.QueryObject"%>
<%@page import="Autonomy.DocumentoQueryTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.Collection"%>

<%
	Collection<QueryObject> list = (Collection<QueryObject>) request.getAttribute("queryList");
	if(list == null) list = new ArrayList<QueryObject>();
%>
<script>
/* 	function send(operation, ID){
		alert(operation);
		document.getElementById("ID").value = ID;
		document.getElementById("operation").value = operation;
		msg = "Si desidera procedere con l'eliminazione?";
		if(operation=="3" && !confirm(msg)) return;
		document.forms[0].submit();
	} */
</script>
<div class='box boxGrid shadow' style="height:475px;overflow:auto">
<div class='title'>
	<p>Sono presenti: <b><%=list.size() %></b> query.</p>				
</div>
<form action="ManageStruttura" method="post" name="myformM">
	<input type='hidden' name="ID" id="ID">
	<input type='hidden' name="operation" id="operation">
	<div id="resultsTable" class="noH">

			<%		
		String classNameSaveQuery = null;
		int countSaveQuery=0;
		for(QueryObject currentQuery: list){
			String ID = currentQuery.getID();
			String nome = currentQuery.getNomeQuery();
			String testo = currentQuery.getTesto();
			String riferimento = currentQuery.getRiferimento();
			String idDiv="private"+ID;
			if(countSaveQuery%2==0){
				classNameSaveQuery = "foo";
			}else{
				classNameSaveQuery = "alternateRow";
			}
%>
			<div id="<%=idDiv %>" class="<%=classNameSaveQuery%>">
				<div class="left">
					<div>
						<h3 class="titoloQuery"><%=nome%></h3>
						<h3 class="ambito"><%=riferimento%></h3>
						<p><%=testo%></p>
					</div>
				</div>
				<div class="right">
					<table>
						<tr>
							<td><a href="#" onclick="javascript: send('2','<%=ID%>')" title="Seleziona e Modifica"><img alt="Modifica" src="<%=request.getContextPath()%>/img/icon/pen.png" class="btn"></a></td>
							<td><a href="#" onclick="javascript: send('7','<%=ID%>')" title="Pubblica"><img alt="Pubblica" src="<%=request.getContextPath()%>/img/icon/hand_thumbsup.png" class="btn"></a></td>
							<td><a href="#" onclick="javascript: send('3','<%=ID%>')" title="Elimina"><img alt="Elimina" src="<%=request.getContextPath()%>/img/icon/trash_can.png"  class="btn"></a></td>
						</tr>
					</table>
				</div>
				<div class="clr"></div>
			</div>
<%		
			countSaveQuery++;
		}
%>
	</div>
</form>
</div>