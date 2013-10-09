<%@page import="model.JobDataDescr"%>
<%@page import="java.util.Collection"%>
<%@page import="model.CategoryFrontEnd"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<% 
		JobDataDescr jobDataDescr = (JobDataDescr) request.getAttribute("jobDataDescr");
		Collection<CategoryFrontEnd> list = jobDataDescr.getCategoryList();
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery-ui.custom.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
  	<script src="<%=request.getContextPath()%>/js/jquery.dynatree-1.2.4.js" type="text/javascript"></script>
   	<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css">
	<link href="<%=request.getContextPath()%>/css/skin/ui.dynatree.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
	<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'>
	<title>D-CUBE | Digital Customer Behaviour</title>
	
	<script type="text/javascript">
/* 	  $(function(){
	    // Attach the dynatree widget to an existing <div id="tree"> element
	    // and pass the tree options as an argument to the dynatree() function:
	    $("#tree").dynatree({
 	      onActivate: function(node) {
	        // A DynaTreeNode object is passed to the activation handler
	        // Note: we also get this event, if persistence is on, and the page is reloaded.
	        alert("You activated " + node.data.title);
	      },
	    
	      children: [
	     	     	{title: "Root", isFolder: true, key: "root",
	     	     		children:[
	        			        {title: "Item 1", isFolder: true, key: "folder1"},
	        			        {title: "Folder 2", isFolder: true, key: "folder2",
	        			          children: [
	        			            {title: "Sub-item 2.1"},
	        			            {title: "Sub-item 2.2"}
	        			          ]
	        			        },
	        			        {title: "Item 3"}
	     	     		]	
	     	     	
	     	     	}
	     	      ]
 
	    });
	  });
 */	  
 
 
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
		  	for(CategoryFrontEnd currentCategory: list){
		  		String idParent = currentCategory.getParent();
		  		String idCategory = currentCategory.getId();
		  		String name = currentCategory.getNome();
		  		String shortName = currentCategory.getShortName();
		  		boolean isLinked = currentCategory.isLinked();
		  		if(idParent==null){%>
	  				var node = $("#tree").dynatree("getRoot");
		  		<%}else{%>
		  			var node = $("#tree").dynatree("getTree").selectKey("<%=idParent%>");
		  			var parentTitle = node.data.title;
		  		<%}%>
	  			 node.addChild({
	  				 <%if(isLinked){%>href: "ManageCategory?parentCategory=" + parentTitle + "&idCategory=<%=idCategory%>&nomeCategory=<%=name%>", <%}%>
	  				 title: "<%=name%>",
                     tooltip: "<%=name%>",
                     key: "<%=idCategory%>"
	             });
		  	<%}%>
	 });

	</script>
</head>
<body>
	
	<div id="wrapper">
		<%@ include file="../header.jsp" %>
		<table id='tbCategorie'>
					<tr>
						<td>
						<div class='box boxTree shadow'>
							<div class='title'>
								<p>Categorie</p>
							</div>
					<div class='content'>
						  <div id="tree"> </div>
						
						</div>
		</div>
		
		</td>
						<td>
			                <iframe name="contentFrame" scrolling="auto"
			                		width="100%" height="800px"
			                        marginheight="0" marginwidth="0" frameborder="0">
			                   <p>Your browser does not support iframes</p>
			                </iframe>
						</td>
					</tr>
				</table>	
				
				
					
			</div>
		
</body>
</html>