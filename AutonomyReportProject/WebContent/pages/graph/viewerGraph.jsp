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
	
	<style>
		
		.node rect {
		  cursor: move;
		  fill-opacity: .9;
		  shape-rendering: crispEdges;
		}
		
		.node text {
		  pointer-events: none;
		  text-shadow: 0 1px 0 #fff;
		}
		
		.link {
		  fill: none;
		  stroke: #000;
		  stroke-opacity: .2;
		}
		
		.link:hover {
		  stroke-opacity: .5;
		}	
	
	</style>

	<body>
		<div id="wrapper">
		<%@ include file="../header.jsp" %>

		<div class="box boxForm shadow">
				<p id="chart" />
		 		<script src="<%=request.getContextPath()%>/js/d3/d3.v3.min.js" type="text/javascript"></script>
		 		<script src="<%=request.getContextPath()%>/js/d3/sankey.js" type="text/javascript"></script>
		 		
		 		<script>
		 		var units = "Documenti";
				var margin = {top: 1, right: 1, bottom: 6, left: 1},
				    width = $("#chart" ).width()- margin.top - margin.bottom,
				    height = 500 - margin.top - margin.bottom;
		 		
		 		var formatNumber = d3.format(",.0f"),
		 		    format = function(d) { return formatNumber(d) + units; },
		 		    color = d3.scale.category20();
		
		 		var svg = d3.select("#chart").append("svg")
		 		    .attr("width", width + margin.left + margin.right)
		 		    .attr("height", height + margin.top + margin.bottom)
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
		 		      .attr("class", "link")
		 		      .attr("d", path)
		 		      .style("stroke-width", function(d) { return Math.max(1, d.dy); })
		 		      .sort(function(a, b) { return b.dy - a.dy; });
		
		 		// add the link titles
		 		  link.append("title")
		 		        .text(function(d) {
		 		      	return d.source.name + " → " + 
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
		 		  node.append("rect")
		 		      .attr("height", function(d) { return d.dy; })
		 		      .attr("width", sankey.nodeWidth())
		 		      .style("fill", function(d) { 
		 				  return d.color = color(d.name.replace(/ .*/, "")); })
		 		      .style("stroke", function(d) { 
		 				  return d3.rgb(d.color).darker(2); })
		 		    .append("title")
		 		      .text(function(d) { 
		 				  return d.name + "\n" + format(d.value); });
		 		 
		 		// add in the title for the nodes
		 		  node.append("text")
		 		      .attr("x", -6)
		 		      .attr("y", function(d) { return d.dy / 2; })
		 		      .attr("dy", ".35em")
		 		      .attr("text-anchor", "end")
		 		      .attr("transform", null)
		 		      .text(function(d) { return d.name; })
		 		    .filter(function(d) { return d.x < width / 2; })
		 		      .attr("x", 6 + sankey.nodeWidth())
		 		      .attr("text-anchor", "start");
		 		 
		 		// the function for moving the nodes
		 		  function dragmove(d) {
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
			<div class="clr"></div>
		</div>
		
	</body>
</html>

