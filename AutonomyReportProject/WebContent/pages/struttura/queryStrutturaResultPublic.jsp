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
<div class='box boxGrid shadow' style="height:400px;overflow:auto">
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
			if(countSaveQueryP%2==0){
				classNameSaveQueryP = "foo";
			}else{
				classNameSaveQueryP = "alternateRow";
			}
%>
			<div class="<%=classNameSaveQueryP%>">
				<div class="left">
					<div>
						<h3><%=nome%></h3>
						<p><%=testo%></p>
					</div>
				</div>
				<div class="right">
					<table>
						<tr>
							<td><img alt="Modifica" src="<%=request.getContextPath()%>/img/icon/pen.png" onclick="javascript: send('8','<%=ID%>')" class="btn"></td>
							<td><img alt="Copia" src="<%=request.getContextPath()%>/img/icon/copy.png" onclick="javascript: send('10','<%=ID%>')" class="btn"></td>
							<td><img alt="Elimina" src="<%=request.getContextPath()%>/img/icon/trash_can.png"  onclick="javascript: send('9','<%=ID%>')" class="btn"></td>
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