<%@page import="model.Message"%>
<%
	Message message = (Message) request.getAttribute("message");
	if(message!=null){
		String css = "info";
		int type = message.getType();
		String msg = message.getText();
		
		switch (type) {
			case 0 :{ css = "info"; break;}
			case 1 :{ css = "errore"; break;} 
			case 2 :{ css = "info"; msg = "<B><font style=\"color:#ff0000;\">ATTENZIONE!</font></B> " + msg;  break;}
		}
		
	%>
		<div class="<%=css%>">
			<p><%=msg%> </p>
		</div>
	<%		
	}
%>
