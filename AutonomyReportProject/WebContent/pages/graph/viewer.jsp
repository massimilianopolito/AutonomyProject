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
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
 		<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script> 
	  	<script src="<%=request.getContextPath()%>/js/jquery-ui-1.10.2.custom.js" type="text/javascript"></script>
	  	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/jquery.vegas.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/script.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/d3/d3.v3.min.js" type="text/javascript"></script>
 		<script src="<%=request.getContextPath()%>/js/d3/sankey.js" type="text/javascript"></script>
 		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery.vegas.css"/>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/global.css"/>
		<link href='http://fonts.googleapis.com/css?family=Passion+One|Wallpoet|Vast+Shadow|Paytone+One|Jacques+Francois+Shadow|Syncopate|Audiowide' rel='stylesheet' type='text/css'/>
		<title>Login - D-CUBE | Digital Customer Behaviour</title>
 		
	</head>
	
	<body>
		<%@ include file="../header.jsp" %>
		<div id="wrapper">
			<div class="box boxGrid shadow">
				<div class="title">
					<p>Elaborazione relativa al periodo: <%=jobDataDescr.getDataInizioSelected()%> - <%=jobDataDescr.getDataFineSelected()%></p>
				</div>

				<table>
					<tr><td class="borderChartDate">
						<p id="listDate" class="chartClass" />
					</td></tr>
					
					<tr><td>
						<p id="chart" class="chartClass" />
					</td></tr>
					
				</table>
			</div>

			<iframe name="contentFrame" scrolling="no"
                	width="100%" height="800"
                    marginheight="0" marginwidth="0" frameborder="0">
                   <p>Your browser does not support iframes</p>
            </iframe>

		</div>
		 		
		 		<script>
		 		$("[name=contentFrame]").hide();
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
		 		  		 .append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
		 		
		 		var svgDate = d3.select("#listDate").append("svg")
	 		    			 .attr("width", width)
	 		    			 .attr("height", 10 + margin.top + margin.bottom)
	 		  				 .append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
		
		 		var escapeValueName = "fake";
		 		var sankey = d3.sankey()
		 		    .nodeWidth(15)
		 		    .nodePadding(10)
		 		    .size([width, height])
		 		    .escapeValueName(escapeValueName);
		
		 		var path = sankey.link();

		 		var url = "ManageGraph?dataDa=<%=jobDataDescr.getDataInizioSelected()%>&dataA=<%=jobDataDescr.getDataFineSelected()%>&nomeJob=<%=jobDataDescr.getTipoTicket()%>&operation=1";
		 		var urlDate = "ManageGraph?dataDa=<%=jobDataDescr.getDataInizioSelected()%>&dataA=<%=jobDataDescr.getDataFineSelected()%>&operation=2";

		 		d3.json(url, function(error, graph) {
		 			sankey.nodeWidth(1);
		 			
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
		 		    	  if(d.value==-1 || d.source.name.indexOf(escapeValueName)!=-1) className="linkInvalid";
		 		    	  return className;
		 		       })
		 		      .style("stroke-width", function(d) {
		 		    	  return  Math.max(1, d.dy); 
		 		    	  })
		 		      .sort(function(a, b) { return b.dy - a.dy; });
		 	
		 	
		 		
		 		// add the link titles
		 		  link.append("title")
		 		        .text(function(d) {
		 		      	return d.source.name + " -> " + 
		 		                d.target.name; });
		 		 
		 		// add in the nodes
		 		  var node = svg.append("g").selectAll(".node")
		 		      .data(graph.nodes)
		 		      .enter().append("g")
		 		      .attr("class", "node")
		 		      .attr("transform", function(d) { 
		 				  return "translate(" + d.x + "," + d.y + ")"; })
		 		      .call(d3.behavior.drag()
		 		      .origin(function(d) {return d; })
					  .on("dragstart", function() { 
		 				  this.parentNode.appendChild(this); })
		 		      .on("drag", dragmove))
		 		      .on("dblclick", function(d){
			 		    	if(d.name.indexOf(escapeValueName)!=-1) return;
		 			 		$("[name=contentFrame]").show();
		 		    	  	$("[name=contentFrame]").attr("src", d.url+"#result");
		 		    	 });

		 		      // add the rectangles for the nodes
		 		//   .on("click", function(){alert("Test")})
		 		    node.append("circle")
		 		      .on("dblclick", function(){
		 		    	 	circle = svg.selectAll("circle");
		 		    	 	circle.style("stroke-width", 1);
		 		    	 	d3.select(this).style("stroke-width", 5);
		 		    	 })
		 		      .style("fill", function(d) {
		 		    	  				fillcolor = color(d.date.replace(/ .*/, ""));
		 		    	  				if(d.name.indexOf(escapeValueName)!=-1) fillcolor = "transparent";
		 				  				return d.color = fillcolor; })
		 		      .style("stroke", function(d) { 
		 		    	  			   	stroke = d3.rgb(d.color).darker(2);
		 		    	  				if(d.name.indexOf(escapeValueName)!=-1) stroke = "";
		 				  				return stroke; })
      				  .attr("class", "node")
      				  .attr("cx", function(d) {
	    	  			  return d.dx;
  	 	   			       })
  	 	   			  .attr("cy", function(d) {
	    	  			  return d.dy/2;
  	 	   			       })
  	 	   			  .attr("r", function(d) {
  	 	   				  r = Math.max(10,d.dy/4) ;
  	 	   				  if(d.name.indexOf(escapeValueName)!=-1) r=0;
	    	  			  return r;
  	 	   			       })
		 		      .append("title")
		 		      .text(function(d) {
			 		      	try{
			 		      		title = ""
			 		      		if(d.name.indexOf(escapeValueName)==-1){
					 		    	title = d.name + "\n" + (d.numdoc).toLocaleString("it-IT");
				 		      	}
				 		     }catch(e){
								title = d.name + "\n" + d.numdoc;
						     }
		 					 return title; });

		 	/* 	   node.append("a")
	 		  	  	  .attr("xlink:href", function(d){return d.url="www.google.com";})
	 		  	  	  .append("circle")
	 		  	  	  .on("click",function(d) {d.areaName="link"})
    				  .attr("class", "nodelink")
    				  .attr("cx", function(d) {
	    	  			  return d.dx;;
	 	   			       })
	 	   			  .attr("cy", function(d) {
	    	  			  return d.dy/2;
	 	   			       })
	 	   			  .attr("r", function(d) {
	 	   				  r =10;
	    	  			  return r;
	 	   			       }); */

		 		 
		 		// add in the title for the nodes
		 		  node
	 		  	  	  .append("text")
		 		      .attr("x", -6)
		 		      .attr("y", function(d) { return d.dy / 2; })
		 		      .attr("dy", ".35em")
		 		      .attr("class", "legend")
		 		      .attr("text-anchor", "end")
		 		      .attr("transform", null)
		 		      .text(function(d) {
		 		    	  text = d.shortname; 
		 		    	  if(d.name.indexOf(escapeValueName)!=-1) text = ""
		 		    	  return text; 
		 		    	})
		 		      .filter(function(d) { return d.x < width / 2; })
		 		      .attr("x", 6 + sankey.nodeWidth())
		 		      .attr("text-anchor", "start");
		 		 
		 		// the function for moving the nodes
		 		  function dragmove(d) {
		 			if(d.name.indexOf(escapeValueName)!=-1) return;
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
		
		 		d3.json(urlDate, function(error, graphDate) {
		 			sankey.nodeWidth(15);
		 		    var nodeMap = {};
		 		    graphDate.nodes.forEach(function(x) { nodeMap[x.name] = x;});
		 		    graphDate.links = graphDate.links.map(function(x) {
		 		      return {
		 		        source: nodeMap[x.source],
		 		        target: nodeMap[x.target],
		 		        value: x.value
		 		      };
		 		    });
		 		 
			 		sankey.nodes(graphDate.nodes).links(graphDate.links).layout(32);

			 	    var link = svgDate.append("g").selectAll(".link")
		 		      				  .data(graphDate.links)
		 		    				  .enter().append("path")
		 		      				  .attr("d", path)
		 		      				  .attr("class","linkInvalid")
		 		      				  .style("stroke-width", function(d) {
		 		    	  			   							return  Math.max(1, d.dy); 
		 		    	  									 }
		 		      				  		)
		 		      				  .sort(function(a, b) { return b.dy - a.dy; });
			 	    
			 	   var node = svgDate.append("g").selectAll(".node")
		 		      				 .data(graphDate.nodes)
		 		    				 .enter().append("g")
		 		      				 .attr("class", "node")
		 		      				 .attr("transform", function(d) { 
		 				  								return "translate(" + d.x + "," + d.y + ")"; });
			 	   
			 		  node.append("rect")
		 		      	  .attr("height", function(d) {
		 		      		  				d.dy=10;
		 		    	  					return d.dy;
		 		    	 				  }
		 		      	  	   )
		 		      	  .attr("width", sankey.nodeWidth())
		 		      	  .style("fill", function(d) {
		 		    	  					fillcolor = color(d.date.replace(/ .*/, ""));
				 		 				    return d.color = fillcolor;
				 		 				 }
		 		      	  	   )
		 		      	  .style("stroke", function(d) { 
		 				  					return d3.rgb(d.color).darker(2); 
		 				  				   }
		 		      	  	   )
		 		    	  .append("title")
		 		      	  .text(function(d) {
		 		    	  			title = d.date;
		 				  			return title; 
		 				  		}
		 		      	  	   );

			 		  node.append("text")
			 		      .attr("x", -6)
			 		      .attr("y", function(d) { return d.dy / 2; })
		 		      	  .attr("dy", ".35em")
		 		      	  .attr("class", "legend")
		 		      	  .attr("text-anchor", "end")
		 		      	  .attr("transform", null)
		 		      	  .text(function(d) {
		 		    	  			text = d.date; 
		 		    	  			return text; 
		 		    			})
		 		    	  .filter(function(d) { 
		 		    		  		return d.x < width / 2; 
		 		    		  	  })
			 		      .attr("x", 6 + sankey.nodeWidth())
		 		      	  .attr("text-anchor", "start");
	
		 		});

		 		</script>
	</body>
</html>

