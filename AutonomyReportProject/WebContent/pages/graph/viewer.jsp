<%@page import="model.SnapShot"%>
<%@page import="java.util.Collection"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Map"%>
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
	
	Map<Timestamp,  Map<Integer, Collection<SnapShot>>> snapByDate = jobDataDescr.getSnapByDate();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
 		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script> 
	  	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.10.2.custom.js" type="text/javascript"></script>
	  	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
 		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
 		
	</head>
	
	<body>
		<%@ include file="../header.jsp" %>
		<div id="wrapper">
			<div class="box boxForm shadow">
				<div class="title">
					<p>Elaborazione relativa al periodo: <%=jobDataDescr.getDataInizioSelected()%> - <%=jobDataDescr.getDataFineSelected()%></p>
				</div>

				<p id="chart" class="chartClass" />
		 		<script src="<%=request.getContextPath()%>/js/d3/d3.v3.min.js" type="text/javascript"></script>
		 		<script src="<%=request.getContextPath()%>/js/d3/sankey.js" type="text/javascript"></script>
		 		
		 		<script>
		 		var units = "Documenti";
				var margin = {top: 1, right: 1, bottom: 6, left: 1},
				    width = $(".box" ).width() -15,
				    height = 500;
		 		
		 		var formatNumber = d3.format(",.0f"),
		 		    format = function(d) { return formatNumber(d) + " "+ units; },
		 		    color = d3.scale.category20();
		
		 		var svg = d3.select("#chart").append("svg")
		 		    .attr("width", width)
		 		    .attr("height", height + margin.top + margin.bottom + 10)
		 		  .append("g")
		 		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
		
		 		var sankey = d3.sankey()
		 		    .nodeWidth(15)
		 		    .nodePadding(10)
		 		    .size([width, height]);
		
		 		var path = sankey.link();

		 		var url = "ManageGraph?dataDa=<%=jobDataDescr.getDataInizioSelected()%>&dataA=<%=jobDataDescr.getDataFineSelected()%>&nomeJob=<%=jobDataDescr.getTipoTicket()%>";
		 		d3.json(url, function(error, graph) {
		 			
		 		    var nodeMap = {};
		 		    graph.nodes.forEach(function(x) { nodeMap[x.name] = x;});
		 		    graph.links = graph.links.map(function(x) {
		 		      return {
		 		        source: nodeMap[x.source],
		 		        target: nodeMap[x.target],
		 		        value: x.value
		 		      };
		 		    });
		 		 
		 		  sankey
		 		      .nodes(graph.nodes)
		 		      .links(graph.links)
		 		      .layout(32);
		 		 
		 		// add in the links
		 		  var link = svg.append("g").selectAll(".link")
		 		      .data(graph.links)
		 		    .enter().append("path")
		 		      .attr("d", path)
		 		      .attr("class", function(d){
		 		    	  className = "linkValid";
		 		    	  if(d.value==-1 || d.source.name.indexOf("foo")!=-1) className="linkInvalid";
		 		    	  return className;
		 		       })
		 		      .style("stroke-width", function(d) {return Math.max(1, d.dy); })
		 		      .sort(function(a, b) { return b.dy - a.dy; });
		 	
		 	
		 		
		 		// add the link titles
		 		  link.append("title")
		 		        .text(function(d) {
		 		      	return d.source.name + " -> " + 
		 		                d.target.name + "\n" + format(d.value); });
		 		 
		 		// add in the nodes
		 		  var node = svg.append("g").selectAll(".node")
		 		      .data(graph.nodes)
		 		    .enter().append("g")
		 		      .attr("class", "node")
		 		      .attr("transform", function(d) { 
		 				  return "translate(" + d.x + "," + d.y + ")"; })
		 		    .call(d3.behavior.drag()
		 		      .origin(function(d) { return d; })
		 		      .on("dragstart", function() { 
		 				  this.parentNode.appendChild(this); })
		 		      .on("drag", dragmove));
		 		 
		 		// add the rectangles for the nodes
		 		//   .on("click", function(){alert("Test")})
		 		    
		 		  node.append("rect")
		 		      .attr("height", function(d) {
		 		    	  if(d.dy==0){
		 		    		  d.dy =5;
		 		    	  }
		 		    	  return d.dy;})
		 		      .attr("width", function(d) {
						  w = sankey.nodeWidth(); 	 	
						  if(d.name.indexOf("foo")!=-1) w = 0;
		 		    	  return w;}
		 		    	)
		 		      .style("fill", function(d) {
		 		    	  fillcolor = color(d.date.replace(/ .*/, ""));
		 		    	  if(d.name.indexOf("foo")!=-1) fillcolor = "";
		 				  return d.color = fillcolor; })
		 		      .style("stroke", function(d) { 
		 				  return d3.rgb(d.color).darker(2); })
		 		    .append("title")
		 		      .text(function(d) {
		 		    	  title = d.name + "\n" + format(d.numdoc);
		 		    	  if(d.name.indexOf("foo")!=-1) title = "";
		 				  return title; });
		 		 
		 		// add in the title for the nodes
		 		  node.append("text")
		 		      .attr("x", -6)
		 		      .attr("y", function(d) { return d.dy / 2; })
		 		      .attr("dy", ".35em")
		 		      .attr("class", "legend")
		 		      .attr("text-anchor", "end")
		 		      .attr("transform", null)
		 		      .text(function(d) {
		 		    	  text = d.shortname; 
		 		    	  if(d.name.indexOf("foo")!=-1) text = ""
		 		    	  return text; 
		 		    	})
		 		    .filter(function(d) { return d.x < width / 2; })
		 		      .attr("x", 6 + sankey.nodeWidth())
		 		      .attr("text-anchor", "start");
		 		 
		 		// the function for moving the nodes
		 		  function dragmove(d) {
		 			if(d.name.indexOf("foo")!=-1) return;
		 		    d3.select(this).attr("transform", 
		 		        "translate(" + (
		 		        	   d.x = Math.max(0, Math.min(width - d.dx, d3.event.x))
		 		        	) + "," + (
		 		                   d.y = Math.max(0, Math.min(height - d.dy, d3.event.y))
		 		            ) + ")");
		 		    sankey.relayout();
		 		    link.attr("d", path);
		 		  }
		 		});
		
		 		</script>
			</div>
		</div>
	</body>
</html>

