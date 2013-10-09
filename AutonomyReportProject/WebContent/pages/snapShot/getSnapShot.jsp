<%@page import="utility.PropertiesManager"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URL"%>

	<%
		String sAddress = null;
		String sPort = PropertiesManager.getMyProperty("autonomy.port");		

		String sAction = request.getParameter("action");
		String sJobName = request.getParameter("sourceJobName");
		if(sJobName.contains("MOBILE_INTERAZIONI"))
			sAddress = PropertiesManager.getMyProperty("autonomy.url");
		else
			sAddress = PropertiesManager.getMyProperty("autonomy.query");
		
		String sStartDate = request.getParameter("dataFrom");
		String sEndDate = request.getParameter("dataTo");
		
		String sURL = null;
		
		if(sAction != null)
		{
			if(sAction.equalsIgnoreCase("clustersgpicserve"))
			{
				sURL = "http://" + sAddress + ":" + sPort + "/action=clustersgpicserve";
			}

			if(sAction.equalsIgnoreCase("clusterserve2dmap"))
			{
				sURL = "http://" + sAddress + ":" + sPort + "/action=clusterserve2dmap";
			}

			if(sURL != null)
			{

				if(sJobName != null)
				{
					sURL += "&sourcejobname=" + sJobName;
				}

				if(sStartDate != null)
				{
					sURL += "&startdate=" + sStartDate;
				}
				
				if(sEndDate != null || sEndDate.trim().length()!=0)
				{
					sURL += "&enddate=" + sEndDate;
				}

				try
				{
					URL url = new URL(sURL);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
										
					if (conn != null)
					{
						InputStream inputStream = conn.getInputStream();
				
						if (inputStream != null)
						{
							String sContentType = conn.getContentType();
				
							ServletOutputStream sos = response.getOutputStream();
				
							if (sos != null)
							{
								byte[] thisPart = new byte[4096];
								int nBytesRead = inputStream.read(thisPart);
				
								while (nBytesRead > -1)
								{
									sos.write(thisPart, 0, nBytesRead);
									nBytesRead = inputStream.read(thisPart);
								}
							}
				
							response.setContentType(sContentType);				
							sos.close();
						}						
						inputStream.close();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();	
	 			}
			}
		}	
	%>
