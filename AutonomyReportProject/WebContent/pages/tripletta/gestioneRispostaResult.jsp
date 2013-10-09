<%@page import="utility.NumberGroupingSeparator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="utility.AppConstants"%>
<%@page import="model.Troika"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.Collection"%>
<%
	Collection<Troika> listaDocumenti = (Collection<Troika>) request.getAttribute("listResultGR");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<link href="<%=request.getContextPath() %>/css/global.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/jquery-collapsible-panel.css"/>
	<script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/js/highlight.pack.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/jquery.cookie.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/jquery.collapsible.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/global.js" type="text/javascript"></script>          
	<script language="javascript" type="text/javascript">
    $(document).ready(function() {
        //syntax highlighter
        hljs.tabReplace = '    ';
        hljs.initHighlightingOnLoad();

        $.fn.slideFadeToggle = function(speed, easing, callback) {
            return this.animate({opacity: 'toggle', height: 'toggle'}, speed, easing, callback);
        };

        $('.page_collapsible').collapsible({
        	cookieName: 'collapsibleGR',
        	speed: 'slow',
            animateOpen: function (elem, opts) { //replace the standard slideUp with custom function
                elem.next().slideFadeToggle(opts.speed);
            },
            animateClose: function (elem, opts) { //replace the standard slideDown with custom function
                elem.next().slideFadeToggle(opts.speed);
            },
            loadOpen: function (elem) { //replace the standard open state with custom function
                elem.next().show();
            },
            loadClose: function (elem, opts) { //replace the close state with custom function
                elem.next().hide();
            }

        });

    });
	</script>

</head>

<body>
	<%@ include file="../messageHeader.jsp" %>
	
	<%if(message==null || message.getType()!=Message.ERROR){ %>
		<%if(listaDocumenti!=null && !listaDocumenti.isEmpty()){
					String totDocGR = NumberGroupingSeparator.formatNumber(listaDocumenti.size());
		%>
					<div class='box boxForm shadow'>
							<div class='title'>
								<p>Sono stati individuati: <b><%=totDocGR %></b> risultati.</p>
							</div>
					<div class='content'>
				 	
					<form action="ManageAdmin" method="post" target="contentFrameGR" id="resultFormGR">
						<input type='hidden' id="typeOfField" name="typeOfField" value="<%=AppConstants.Tripletta.TROIKA_FIELD_MANAGE_R%>"/>
						<input type='hidden' id="classeReport" name="classeReport" value="<%=AppConstants.ClasseReport.ONTOLOGYTRAINER%>"/>
						<input type='hidden' id="operationGR" name="operation" value="9"/>
						<input type='hidden' id="triplettaCorrenteGR" name="triplettaCorrenteGR"/>
						<div style="text-align:center">
							<input type="submit" value="Salva tutto"/>
						</div>
						<div id="resultsTable">
	
				<%		
							Iterator iterDoc = listaDocumenti.iterator();
							String className = null;
							int count=0;
							Troika troika = null;
							String nomeRisposta = null;
							String risposta = null;
							String caratterizzazioneR = null;
							while(iterDoc.hasNext()){
								troika = (Troika)iterDoc.next();
								String title = null;
								title = troika.getCompleteName();
								risposta = troika.getRisposta();
								caratterizzazioneR = troika.getCaratterizzazione();
								nomeRisposta ="risposta_" + troika;
								if(count%2==0){
									className = "foo";
								}else{
									className = "alternateRow";
								}
				%>

								<input type='hidden' id="codice" name="codiceGR" value="<%=troika%>"/>
								<div class="<%=className%>">
									<div class="page_collapsible" id="sectionGR<%=count%>"><h3><%=title%></h3><span></span></div>
									<div class="container">
									    <div class="content">
											<div class="left">
												<textarea rows="8" cols="120" name="<%=nomeRisposta%>" style="resize:none;"><%=risposta%></textarea>
												<h3>Silos: <%=caratterizzazioneR==null?"":caratterizzazioneR%></h3>
											</div>
											<div class="right">
												<table>
													<tr>
														<td><img alt="Salva questa risposta" src="<%=request.getContextPath()%>/img/icon/diskette.png"  onclick="javascript: manage('<%=troika%>','8','resultFormGR','operationGR','triplettaCorrenteGR');"/></td>
													</tr>
												</table>
											</div>
											<div class="clr"></div>
									    </div>
									</div>
								</div>
				<%		
								count++;
							}
				%>
							<% 
								String classCustom = "foo";
								if((count+1)%2==0) classCustom = "alternateRow";
							%>

						</div>
						<div class="clr"></div>
						<!--  <div style="text-align:center">
							<input type="submit" value="Salva tutto"/>
						</div>-->
					</form>
				 </div>
				 </div>
		<%}else{%>
			<div class='box boxGrid shadow'>
				<div class='title'>
					<p class="label">Non ci sono risultati per i valori selezionati.</p>				
				</div>
			</div>	
			
		<%} %>
		
	<%} %>

</body>
</html>