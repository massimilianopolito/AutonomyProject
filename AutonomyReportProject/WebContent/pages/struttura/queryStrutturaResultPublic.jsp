<%@page import="model.QueryObject"%>
<%@page import="Autonomy.DocumentoQueryTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.Collection"%>

<%
	Collection<QueryObject> listP = (Collection<QueryObject>) request.getAttribute("queryListPublic");
	if(listP == null) listP = new ArrayList<QueryObject>();
%>
<script>
	function send(operation, ID){
		document.getElementById("ID").value = ID;
		document.getElementById("operation").value = operation;
		document.forms[0].submit();
	}
</script>
<div class='box boxGrid shadow' style="height:475px;overflow:auto">
<div class='title'>
	<p>Sono presenti: <b><%=listP.size() %></b> query.</p>				
</div>
<form action="ManageStruttura" method="post" name="myformP">
	<input type='hidden' name="ID" id="ID">
	<input type='hidden' name="operation" id="operation">
	<div id="resultsTable" class="noH">

			<%		
		String classNameSaveQueryP = null;
		int countSaveQueryP=0;
		for(QueryObject currentQuery: listP){
			String ID = currentQuery.getID();
			String nome = currentQuery.getNomeQuery();
			String testo = currentQuery.getTesto();
			String riferimento = currentQuery.getRiferimento();
			String idDiv="public"+ID;
			if(countSaveQueryP%2==0){
				classNameSaveQueryP = "foo";
			}else{
				classNameSaveQueryP = "alternateRow";
			}
%>
			<div id="<%=idDiv %>" class="<%=classNameSaveQueryP%>">
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
							<td><a href="#" onclick="javascript: send('8','<%=ID%>')" title="Modifica"><img alt="Modifica" src="<%=request.getContextPath()%>/img/icon/pen.png" class="btn"></a></td>
							<td><a href="#" onclick="javascript: send('10','<%=ID%>')" title="Copia"><img alt="Copia" src="<%=request.getContextPath()%>/img/icon/copy.png" class="btn"></a></td>
							<td><a href="#" onclick="javascript: send('9','<%=ID%>')" title="Elimina"><img alt="Elimina" src="<%=request.getContextPath()%>/img/icon/trash_can.png"  class="btn"></a></td>
						</tr>
					</table>
				</div>
				<div class="clr"></div>
			</div>
<%		
		countSaveQueryP++;
		}
%>
	</div>
</form>
</div>