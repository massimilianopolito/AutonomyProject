<div id="tabs">

  <ul>

    <li><a href="#tabs-1">Le tue Query</a></li>

    <li><a id="public" href="#tabs-2">Query Pubbliche</a></li>

  </ul>

  <div id="tabs-1">

<%@ include file="queryStrutturaResult.jsp" %>

  </div>

  <div id="tabs-2">

<%@ include file="queryStrutturaResultPublic.jsp" %>

  </div>

</div>

<%

String pagina = (String)request.getSession().getAttribute("pagina");

if(pagina!=null && "P".equalsIgnoreCase(pagina)){

%>

<script>

window.location=document.getElementById('public').href;

</script>

<%

}

%>
		
	
	
