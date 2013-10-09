<%@page import="utility.NumberGroupingSeparator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="utility.AppConstants"%>
<%@page import="model.Troika"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Autonomy.DocumentoTO"%>
<%@page import="java.util.Collection"%>
<%
	Collection<Troika> listaDocumenti = (Collection<Troika>) request.getAttribute("listResult");

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
        	cookieName: 'collapsible',
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
				 	
					<form action="ManageAdmin" method="post" target="contentFrame" id="resultForm">
						<input type='hidden' id="typeOfField" name="typeOfField" value="<%=AppConstants.Tripletta.TROIKA_FIELD_MANAGE_D%>"/>
						<input type='hidden' id="classeReport" name="classeReport" value="<%=AppConstants.ClasseReport.ONTOLOGYTRAINER%>"/>
						<input type='hidden' id="operation" name="operation" value="2"/>
						<input type='hidden' id="triplettaCorrente" name="triplettaCorrente"/>
						<div style="text-align:center">
							<input type="button" value="Salva Tutto" onclick="manageAndCheckGlobal('resultForm')"/>
						</div>
						
						<div id="resultsTable">
	
				<%		
							Iterator iterDoc = listaDocumenti.iterator();
							String className = null;
							int count=0;
							Troika troika = null;
							String nomeDescrizione = null;
							String nomeCaratterizzazione = null;
							String descrizione = null;
							String caratterizzazione = null;
							boolean isFather = false;
							while(iterDoc.hasNext()){
								String jsFunctionName = "manage";
								troika = (Troika)iterDoc.next();
								isFather = troika.isFather();// troika.getCustom()==null || troika.getCustom().trim().length()==0;
								String title = null;
								title = troika.getCompleteName();
								descrizione = troika.getDescrizione();
								nomeDescrizione ="descrizione_" + troika;
								nomeCaratterizzazione = "caratterizzazione_" + troika;
								if(count%2==0){
									className = "foo";
								}else{
									className = "alternateRow";
								}
								caratterizzazione = troika.getCaratterizzazione();
				%>

								<input type='hidden' id="codice" name="codice" value="<%=troika%>"/>
								<%if(isFather){ %>
									<input type='hidden' id="uid" name="uid" value="<%=troika.getID()%>"/>
									<input type='hidden' id="codiceF" name="codiceF" value="<%=troika%>"/>
								<%} %>
								<div class="<%=className%>">
									<div class="page_collapsible" id="section<%=count%>"><h3><%=title%></h3><span></span></div>
									<div class="container">
									    <div class="content">
											<div class="left">
												<textarea rows="8" cols="120" name="<%=nomeDescrizione%>" style="resize:none;"><%=descrizione%></textarea>

												<%if(isFather){ 
													jsFunctionName = "manageAndCheck";
												%>
													<h3>Silos: 
														<select id="<%=nomeCaratterizzazione %>" name="<%=nomeCaratterizzazione %>">
															<option value="--" >- Seleziona - </option>
															<option value="<%=AppConstants.Tripletta.TROIKA_INFORMAZIONI %>" label="<%=AppConstants.Tripletta.TROIKA_INFORMAZIONI %>" <%if(AppConstants.Tripletta.TROIKA_INFORMAZIONI.equalsIgnoreCase(caratterizzazione)){%> selected="selected" <%}%> ><%=AppConstants.Tripletta.TROIKA_INFORMAZIONI %></option>
															<option value="<%=AppConstants.Tripletta.TROIKA_ASSISTENZA %>" label="<%=AppConstants.Tripletta.TROIKA_ASSISTENZA %>" <%if(AppConstants.Tripletta.TROIKA_ASSISTENZA.equalsIgnoreCase(caratterizzazione)){%> selected="selected" <%}%> ><%=AppConstants.Tripletta.TROIKA_ASSISTENZA %></option>
														</select>
													</h3>
												<%}else{%>
													<input type='hidden' name="<%=nomeCaratterizzazione %>" value="<%=caratterizzazione%>"/>
													<h3>Silos: <%=caratterizzazione==null?"":caratterizzazione%></h3>
												<%} %>
											</div>
											<div class="right">
												<table>
													<tr>
														<%if(!isFather){ %>
														<td><img alt="Elimina" src="<%=request.getContextPath()%>/img/icon/trash_can.png"  onclick="javascript: manage('<%=troika%>','5','resultForm','operation','triplettaCorrente');"/></td>
														<%} %>
														<td><img alt="Salva questa descrizione" src="<%=request.getContextPath()%>/img/icon/diskette.png"  onclick="javascript: <%=jsFunctionName%>('<%=troika%>','3','resultForm','operation','triplettaCorrente');"/></td>
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
							<div class="<%=classCustom%>">
								<div class="left">
									<h3>Nome Campo Aggiuntivo: </h3><input type="text" name="customName" id="customName" style="width:200px"/></p>
									<h3>Descrizione Campo Aggiuntivo: </h3>
									<textarea rows="5" cols="130" name="customDescr" style="resize:none;"></textarea>
								</div>
								<div class="right">
									<img alt="Salva questa descrizione" src="<%=request.getContextPath()%>/img/icon/diskette.png" 
										 onclick="javascript: manage('custom', '3','resultForm','operation','triplettaCorrente');"/>
								</div>
								<div class="clr"></div>
							</div>

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