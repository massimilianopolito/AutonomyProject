package Autonomy;

import java.io.File;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.swing.text.DateFormatter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import sun.awt.CharsetString;
import utility.PropertiesManager;

import com.autonomy.aci.AciAction;
import com.autonomy.aci.AciConnection;
import com.autonomy.aci.AciConnectionDetails;
import com.autonomy.aci.AciResponse;
import com.autonomy.aci.ActionParameter;
import com.autonomy.aci.constants.IDOLEncodings;
import com.autonomy.aci.exceptions.AciException;
import com.autonomy.aci.services.ConceptRetrievalFunctionality;
import com.autonomy.aci.services.IDOLService;
import com.autonomy.aci.services.QuerySummaryManagerService;


public class D2Map {
	private String yesterday;
	
	
	public D2Map() {
		super();
		Calendar yesterdayDate = GregorianCalendar.getInstance();
		yesterdayDate.roll(Calendar.DATE, -1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
		yesterday = dateFormat.format(yesterdayDate.getTime());
	}

	public String getNumTotaleDocumenti(String db)throws AutonomyException, Exception{
		String num = null;
		String env = PropertiesManager.getMyProperty("environment");
		if(!"max".equalsIgnoreCase(env)){
			try{
				
				AciConnection connection = null;
				
				if(db.equals("CaseMobileConsumer")||db.equals("CaseFissoConsumer")||db.equals("IntFissoConsumer"))
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				else
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				connection.setCharacterEncoding(IDOLEncodings.UTF8);
				
				AciAction getStatus = new AciAction("getstatus");
				AciResponse resp = connection.aciActionExecute(getStatus);

				if(resp.checkForSuccess()){
					AciResponse databases = resp.findFirstOccurrence("databases");
					if(databases!=null){
						AciResponse database = databases.findFirstOccurrence("database");
						while(database!=null){
							String dbName = database.getTagValue("name");
							if(db.equalsIgnoreCase(dbName)){
								num = database.getTagValue("documents");
								break;
							}
							database = database.next("database");
						}
					}
				}
				

			}catch (AciException e) {
				e.printStackTrace();
				throw new AutonomyException(e.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
				throw new AutonomyException(e.getMessage());
			}
		}
		
		return num;
	}
	
	private String testoList(String text) throws Exception
	{
		
		
		text = text.replace("?", " ");
		text = text.replace(":", " ");
		text = text.replace(".", " ");
		text = text.replace(",", " ");
		text = text.replace("!", " ");
				
		String Wind = PropertiesManager.getMyProperty("wind.words");
				
				
		String[] windArr = Wind.split(";");
		String textlower = text.toLowerCase();
		
		
		
		for(int i = 0; i<windArr.length; i++)
		{
			String t = windArr[i].toLowerCase();
			
			//System.out.println("valore white list: " + t);
			
			if(textlower.contains(t))
			{
				textlower = textlower.replaceFirst(t, "\"" + t + "\"");
			}	
		}
	
		System.out.println("Testo cambiato: " + textlower);
			
		textlower = textlower.trim();
		
	
		if(textlower.contains("\"\""))
		{
			System.out.println("dentro if doppio apice");
			int primo = textlower.indexOf("\"\"");
			String sub = textlower.substring(primo);
			
			String subUp = sub.replace("\"\"", "");
					
			subUp = subUp.replaceFirst("\"", "");
			
			String up ="\"" + subUp;
			
			textlower = textlower.replace(sub, up);
			
			System.out.println("substring: " + textlower);
		}
		
		String[] textRetArr = textlower.split(" ");
		System.out.println("lunghezza array testo: " +textRetArr.length);
		if(textRetArr.length==1)
		{
			if(!textlower.contains("\""))
				textlower = "pippoplutotopolino";
		}	
			
		if(text.equalsIgnoreCase(""))
			textlower = "pippoplutotopolino";
		
		return textlower;
	}
	
	/*private String testoList(String text) throws Exception
	{
		
		
		text = text.replace("?", " ");
		text = text.replace(":", " ");
		text = text.replace(".", " ");
		text = text.replace(",", " ");
		text = text.replace("!", " ");
				
		String Wind = PropertiesManager.getMyProperty("wind.words");
		
		String[] windArr = Wind.split(",");
		String textlower = text.toLowerCase();
		for(int i = 0; i<windArr.length; i++)
		{
			String t = windArr[i].toLowerCase();
			
			System.out.println("valore white list: " + t);
			
			if(textlower.contains(t))
			{
				if(!textlower.contains("\""))
					textlower = textlower.replaceFirst(t, "\"" + t + "\"");
			}	
		}
	
		System.out.println("Testo cambiato: " + textlower);
			
		textlower = textlower.trim();
		String[] textRetArr = textlower.split(" ");
		System.out.println("lunghezza array testo: " +textRetArr.length);
		if(textRetArr.length==1)
			textlower = "pippoplutotopolino";
		if(text.equalsIgnoreCase(""))
			textlower = "pippoplutotopolino";
		
		return textlower;
	}*/
	
	/*private String testo(String text) throws Exception
	{
		
		String textRet = "";
		
		System.out.println("Testo cambiato: " + text);
		
		String[] textArr = text.split(" ");
		
		
		String Wind = PropertiesManager.getMyProperty("wind.words");
		
		String[] windArr = Wind.split(",");
		
		for(int i = 0; i<textArr.length; i++)
		{
			String t = textArr[i];
			
			for(int z = 0;z<windArr.length;z++)
			{
				if(t.equalsIgnoreCase(windArr[z]))
				{	
					if(!textRet.contains(t))
					textRet=textRet + " " + t;
				}	
			}	
		}
		
		if(textRet.contains("verso") && textRet.contains("wind"))
		{
			textRet = "verso DNEAR1 wind";
		}
		
		
			
		textRet = textRet.trim();
		String[] textRetArr = textRet.split(" ");
		System.out.println("lunghezza array testo: " +textRetArr.length);
		if(textRetArr.length==1)
			textRet = "pippoplutotopolino";
		if(textRet.equalsIgnoreCase(""))
			textRet = "pippoplutotopolino";
		
		return textRet;
	}*/
	
	public ArrayList<String> getNomeSnapshots() throws AutonomyException{
	
		ArrayList<String> listSnapName = null;
		try{
			
			AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			
			AciAction getCluster = new AciAction("clustershowjobs");
			AciResponse resp = connection.aciActionExecute(getCluster);
		
			boolean esito = resp.checkForSuccess();
			if(esito){
				listSnapName = new ArrayList<String>();
				AciResponse snap = resp.findFirstOccurrence("autn:snapshot");
				while(snap != null){
					listSnapName.add(snap.getTagValue("autn:name", ""));
					snap = snap.next("autn:snapshot");
				}
		
			}
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		return listSnapName;
	}

	public ArrayList<ClusterData> getCuster(String idol) throws AutonomyException,Exception
	{
		ArrayList<ClusterData> listCluster = null;
		
		try{
			ClusterData cd = null;
			AciConnection connection = null;
			if(idol.equalsIgnoreCase("a"))
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			else
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			AciAction getCluster = new AciAction("clustershowjobs");
			AciResponse resp = connection.aciActionExecute(getCluster);
			boolean esito = resp.checkForSuccess();
			if(esito)
			{
				listCluster = new ArrayList<ClusterData>(); 
				String appoName = "";
				AciResponse snap = resp.findFirstOccurrence("autn:snapshot");
				ArrayList<String> appo = null;
				while(snap != null)
				{
					int i = 0;
					cd = new ClusterData();
					cd.setSpectroAcro(snap.getTagValue("autn:name", "") + "SG");
					
					//spectroPath = KMSProperties.getInstance().getSPECTRO_PATH() + "/SGPIC_";
					//mapPath = KMSProperties.getInstance().getMAP_PATH() + "/";
					
					//spectroPath = spectroPath + kmsClusterRetTo.getSpectroAcro() + "_";
					AciResponse timespaSpe = snap.findFirstOccurrence("autn:timestamp");
					
					String dataInizio = null;
					while(timespaSpe != null)
					{
						if(i==0){
							dataInizio = timespaSpe.getValue();
							cd.getDataInizioSpectro().add(dataInizio);
						}
						
						if(i==1){
							cd.getDataFineSpectro().add(timespaSpe.getValue());
							dataInizio = timespaSpe.getValue();
						}

						if(i>1){
							cd.getDataInizioSpectro().add(dataInizio);
							cd.getDataFineSpectro().add(timespaSpe.getValue());
							dataInizio = timespaSpe.getValue();
						}
		/*				if(i == 0)
							kmsClusterRetTo.getDataInizioSpectro().add(timespaSpe.getValue());
						else
						{	
							int z = i%2;
						if(z == 0)
							kmsClusterRetTo.getDataInizioSpectro().add(timespaSpe.getValue());
						else
							kmsClusterRetTo.getDataFineSpectro().add(timespaSpe.getValue());
						}*/
						//appoSpectro = timespaSpe.getValue();
						
						//kmsClusterRetTo.getDataFineSpectro().add(timespaSpe.getValue());
						//kmsClusterRetTo.getDataFineSpectro().add("1200265260");
						//1199833260
						timespaSpe = timespaSpe.next("autn:timestamp");
						
						i++;
						//if(i==1)
						
					}
					if(cd.getDataInizioSpectro().size() != cd.getDataFineSpectro().size())
					{
						int iniz = cd.getDataInizioSpectro().size();
						int fine = cd.getDataFineSpectro().size();
						while(iniz != fine)
						{	
							cd.getDataInizioSpectro().remove(iniz-1);
							iniz = cd.getDataInizioSpectro().size();
						}
									
						//int s = kmsClusterRetTo.getDataInizioSpectro().size();
						//kmsClusterRetTo.getDataInizioSpectro().remove(s-1);
					}	
					
//					kmsClusterRetTo.getDataInizioSpectro().add("1199833260");
//					kmsClusterRetTo.getDataFineSpectro().add("1201042860");
					//if(kmsClusterRetTo.getDataInizioSpectro().size() > 0)
					//{
					//	int k = kmsClusterRetTo.getDataFineSpectro().size() - 1;
					//	spectroPath = spectroPath + kmsClusterRetTo.getDataInizioSpectro().get(0) + "-1200265260_512x512.jpeg"; 
					//}
						
					appo = new ArrayList<String>();
					
					AciResponse clust = resp.findFirstOccurrence("autn:cluster");
					
					while(clust != null)
					{
						
						if(clust.getTagValue("autn:name", "").contains(snap.getTagValue("autn:name", "")))
						{
							appoName = clust.getTagValue("autn:name", "");
							if(appoName.length() != "_clusters".length())
							{
								cd.setMapAcro(clust.getTagValue("autn:name", ""));
						
								AciResponse timespa = clust.findFirstOccurrence("autn:timestamp");
						
								while(timespa != null)
								{
									cd.getDataInizioMap().add(timespa.getValue());
									cd.getDataFineMap().add(timespa.getValue());
									//kmsClusterRetTo.getPathMap().add(mapPath + clust.getTagValue("autn:name", "") + "-" + timespa.getValue()+ ".jpeg");
								
									timespa = timespa.next("autn:timestamp");
								}
							}
						}
						
						clust = clust.next("autn:cluster");
					}	
					
					AciResponse spectr = resp.findFirstOccurrence("autn:spectrograph");
					
					while(spectr != null)
					{
						if(spectr.getTagValue("autn:name", "").equalsIgnoreCase(cd.getSpectroAcro()))
						{
							appo.add(spectr.getTagValue("autn:name", ""));
							//kmsClusterRetTo.getPathSpectro().add(spectroPath);
						}
						spectr = spectr.next("autn:spectrograph");
					}	
					
					if(appo.size() == 0)
					{	
						cd.setSpectroAcro(null);
						cd.getPathSpectro().clear();
						cd.getDataInizioSpectro().clear();
						cd.getDataFineSpectro().clear();
					}
					listCluster.add(cd);
					
					snap = snap.next("autn:snapshot");
				}

			}
			
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		
		
		return listCluster;
	}
	
	public ArrayList<Bean2DMapTO> view2DMap(String cluster, String startDate, String endDate) throws Exception
	{
		ArrayList<Bean2DMapTO> listCluster = null;
		String Db = null;
		//LOCALE
		/*if(cluster.equals("MOBILE"))
			Db = "WindT";
		if(cluster.equals("FISSO"))
			Db = "WindF";*/
		//HotTopicsIntMobile();
		//PRODUZIONE
		if(cluster.equals("FISSO_INTERAZIONI_CONSUMER"))
			Db = "IntFissoConsumer";
		if(cluster.equals("FISSO_CASE_CONSUMER"))
			Db = "CaseFissoConsumer";
		if(cluster.equals("MOBILE_CASE_CONSUMER"))
			Db = "CaseMobileConsumer";
		if(cluster.equals("MOBILE_INTERAZIONI_CONSUMER"))
			Db = "IntMobileConsumer";
		if(cluster.equals("MOBILE_SOCIAL_CONSUMER_DOMANDE"))
			Db = "ConsumerSocialMobile";
		if(cluster.equals("MOBILE_SOCIAL_CONSUMER_RISPOSTE"))
			Db = "ConsumerSocialMobile";
		if(cluster.equals("FISSO_SOCIAL_CONSUMER_DOMANDE"))
			Db = "ConsumerSocialFisso";
		if(cluster.equals("FISSO_SOCIAL_CONSUMER_RISPOSTE"))
			Db = "ConsumerSocialFisso";
		if(cluster.equals("FISSO_INTERAZIONI_CORPORATE"))
			Db = "IntFissoCorporate";
		if(cluster.equals("FISSO_CASE_CORPORATE"))
			Db = "CaseFissoCorporate";
		if(cluster.equals("MOBILE_CASE_CORPORATE"))
			Db = "CaseMobileCorporate";
		if(cluster.equals("MOBILE_INTERAZIONI_CORPORATE"))
			Db = "IntMobileCorporate";
		
		/*
		 * 		if(spectroTo.equals("FISSO_INTERAZIONI_CONSUMER"))
			Db = "IntFissoConsumer";
		if(spectroTo.equals("FISSO_CASE_CONSUMER"))
			Db = "CaseFissoConsumer";
		if(spectroTo.equals("MOBILE_CASE_CONSUMER"))
			Db = "CaseMobileConsumer";
		if(spectroTo.equals("MOBILE_INTERAZIONI_CONSUMER"))
			Db = "IntMobileConsumer";
		if(spectroTo.equals("FISSO_INTERAZIONI_CORPORATE"))
			Db = "IntFissoCorporate";
		if(spectroTo.equals("FISSO_CASE_CORPORATE"))
			Db = "CaseFissoCorporate";
		if(spectroTo.equals("MOBILE_CASE_CORPORATE"))
			Db = "CaseMobileCorporate";
		if(spectroTo.equals("MOBILE_INTERAZIONI_CORPORATE"))
			Db = "IntMobileCorporate";

		 */
				
		try{
			String numDoc = getNumTotaleDocumenti(Db);
			AciConnection connection = null;
			
			if(Db.equals("CaseMobileConsumer")||Db.equals("CaseFissoConsumer")||Db.equals("IntFissoConsumer"))
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			else
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			
			AciAction aciAction = new AciAction("ClusterResults");
			aciAction.setParameter(new ActionParameter("SourceJobname", cluster + "_clusters"));
			aciAction.setParameter(new ActionParameter("NumClusters", "25"));
			aciAction.setParameter(new ActionParameter("NumResults", "5000"));
			if(startDate != null)
				aciAction.setParameter(new ActionParameter("StartDate", startDate));
			if(endDate != null)
				aciAction.setParameter(new ActionParameter("EndDate", endDate));
			
			AciResponse response = connection.aciActionExecute(aciAction);
			
			
			if(response.checkForSuccess())
			{	
				//KMS2DMapTO[] arrKms2DMapTo = new KMS2DMapTO[aciResponse.getTagValue("autn:numclusters".trim(), 0)];
				
				listCluster = new ArrayList<Bean2DMapTO>();
				AciResponse responseClust = response.findFirstOccurrence("autn:cluster".trim());
																							
				Bean2DMapTO bean2DMapTo = null;
				DocumentoTO documentoTo = null;
				//KMS2DMapTO current2D = (KMS2DMapTO) currentTo;
				String nomeMap = cluster + "_clusters" + "-" + startDate + ".jpeg";
				String path = "C:\\Program Files (x86)\\Autonomy\\IDOLServer\\IDOL\\category\\cluster\\2DMAPS\\" + nomeMap.toUpperCase();
			
				BigInteger bi = null;
				BigInteger div = new BigInteger("2");
				
			
				while(responseClust != null)
				{
					ArrayList<DocumentoTO> docList = new ArrayList<DocumentoTO>();
					bean2DMapTo = new Bean2DMapTO();
					//Kms2DMapTo.setAction("ClusterResults");
					bean2DMapTo.setClusterName(responseClust.getTagValue("autn:title", ""));
					bean2DMapTo.setClusterScore(responseClust.getTagValue("autn:score", ""));
					bean2DMapTo.setX(responseClust.getTagValue("autn:x_coord", 0));
					bean2DMapTo.setY(responseClust.getTagValue("autn:y_coord", 0));
					bean2DMapTo.setNumDocs(responseClust.getTagValue("autn:numdocs", 0));
				
					AciResponse responseDoc = responseClust.findFirstEnclosedOccurrence("autn:doc");
					
				
					while(responseDoc != null)
					{
						documentoTo = new DocumentoTO();
					
						documentoTo.setTitleDoc(responseDoc.getTagValue("autn:title", ""));
						documentoTo.setReferenceDoc(responseDoc.getTagValue("autn:ref", ""));
						documentoTo.setTotaleDocumenti(numDoc);
						String summ = "";//responseDoc.findFirstEnclosedOccurrence("autn:summary").getValue();
						
						AciResponse summary = responseDoc.findFirstEnclosedOccurrence("autn:summary"); 
						if(summary != null)
							summ = summary.getValue();
//							documentoTo.setSummary);
						else
							summ = documentoTo.getTitleDoc();
						//.setSummary(responseDoc.getTagValue("autn:title", ""));
						
						documentoTo.setSummary(summ);
						documentoTo.setDataBase(Db);
						
						if(!responseDoc.getTagValue("autn:score", "1").equalsIgnoreCase("1"))
						{	
							bi = new BigInteger(responseDoc.getTagValue("autn:score", "1"));
							documentoTo.setScore(bi.divideAndRemainder(div)[0].toString());
						}
						else
							documentoTo.setScore("1");
						
						docList.add(documentoTo);
						responseDoc = responseDoc.next("autn:doc");
					}	
				
					bean2DMapTo.setResultList(docList);
					bean2DMapTo.setPath(path);
					listCluster.add(bean2DMapTo);
					
					responseClust = responseClust.next("autn:cluster");
				}
				
			}
			
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		
		//return arrKms2DMapTo;
		return listCluster;
	}
	
	public ArrayList<SpectroTO> SGData(String cluster, String startDate, String endDate) throws AutonomyException
	{
		ArrayList<SpectroTO> toList = null;
		AciConnection connection = null;
		try{
			
			
			if(cluster.equalsIgnoreCase("MOBILE_INTERAZIONI_CONSUMER"))
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			else
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			
			AciAction aciAction = new AciAction("ClusterSGDataServe");
			aciAction.setParameter(new ActionParameter("SourceJobname", cluster + "SG"));
			aciAction.setParameter(new ActionParameter("StartDate", startDate));
			if(endDate != null)
				aciAction.setParameter(new ActionParameter("EndDate", endDate));
			aciAction.setParameter(new ActionParameter("StructuredXML", "true"));
			AciResponse response = connection.aciActionExecute(aciAction);
			
			if(response.checkForSuccess())
			{
				toList = new ArrayList<SpectroTO>();
				SpectroTO spectroTo = null;
				//KMSSpectroTO currentSpectro = (KMSSpectroTO) currentTo;
				AciResponse responseClust = response.findFirstOccurrence("autn:cluster");
				String datasetName = cluster + "SG";
				String spectroName = cluster + "SG" + "_" + startDate + "-" + startDate + "_512x512.jpeg";
				String path = "C:\\Program Files (x86)\\Autonomy\\IDOLServer\\IDOL\\category\\cluster\\SGPICCACHE\\" + spectroName.toUpperCase();
				while(responseClust != null)
				{
					spectroTo = new SpectroTO();
					spectroTo.setClusterId(responseClust.getTagValue("autn:id", ""));
					spectroTo.setJobName(responseClust.getTagValue("autn:jobname", ""));
					spectroTo.setClusterTitle(responseClust.getTagValue("autn:title", ""));
					spectroTo.setFromDate(responseClust.getTagValue("autn:fromdate", ""));
					spectroTo.setToDate(responseClust.getTagValue("autn:todate", ""));
					spectroTo.setNumDocs(responseClust.getTagValue("autn:numdocs", ""));
					spectroTo.setX1(responseClust.getTagValue("autn:x1", ""));
					spectroTo.setX2(responseClust.getTagValue("autn:x2", ""));
					spectroTo.setY1(responseClust.getTagValue("autn:y1", ""));
					spectroTo.setY2(responseClust.getTagValue("autn:y2", ""));
					spectroTo.setRadiusFrom(responseClust.getTagValue("autn:radius_from", ""));
					spectroTo.setRadiusTo(responseClust.getTagValue("autn:radius_to", ""));
					spectroTo.setPath(path);
					spectroTo.setDataSetName(datasetName);

					toList.add(spectroTo);

					responseClust = responseClust.next("autn:cluster");
				}

				/*arrKmsSpectroTo = new KMSSpectroTO[toList.size()];
				for(int i = 0; i < toList.size(); i++)
				{
					arrKmsSpectroTo[i] = toList.get(i);
				}

				return arrKmsSpectroTo;*/
			}
			
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
	
		return toList;
	}
	
	public SpectroTO SGDocs(SpectroTO spectroTo) throws AutonomyException
	{
		SpectroTO pectroTo = null;
		
		String Db = null;
		//locale
/*		if(spectroTo.getDataSetName().equals("MOBILESG"))
			Db="WindT";
		if(spectroTo.getDataSetName().equals("FISSOSG"))
			Db="WindF";
*/		
		//PRODUZIONE
		if(spectroTo.getJobName().equals("FISSO_INTERAZIONI_CONSUMER"))
			Db = "IntFissoConsumer";
		if(spectroTo.getJobName().equals("FISSO_CASE_CONSUMER"))
			Db = "CaseFissoConsumer";
		if(spectroTo.getJobName().equals("MOBILE_CASE_CONSUMER"))
			Db = "CaseMobileConsumer";
		if(spectroTo.getJobName().equals("MOBILE_INTERAZIONI_CONSUMER"))
			Db = "IntMobileConsumer";
		if(spectroTo.getJobName().equals("FISSO_INTERAZIONI_CORPORATE"))
			Db = "IntFissoCorporate";
		if(spectroTo.getJobName().equals("FISSO_CASE_CORPORATE"))
			Db = "CaseFissoCorporate";
		if(spectroTo.getJobName().equals("MOBILE_CASE_CORPORATE"))
			Db = "CaseMobileCorporate";
		if(spectroTo.getJobName().equals("MOBILE_INTERAZIONI_CORPORATE"))
			Db = "IntMobileCorporate";
		if(spectroTo.getJobName().equals("MOBILE_SOCIAL_CONSUMER_DOMANDE"))
			Db = "ConsumerSocialMobile";
		if(spectroTo.getJobName().equals("MOBILE_SOCIAL_CONSUMER_RISPOSTE"))
			Db = "ConsumerSocialMobile";
		if(spectroTo.getJobName().equals("FISSO_SOCIAL_CONSUMER_DOMANDE"))
			Db = "ConsumerSocialFisso";
		if(spectroTo.getJobName().equals("FISSO_SOCIAL_CONSUMER_RISPOSTE"))
			Db = "ConsumerSocialFisso";		

		try{
			String numDoc = getNumTotaleDocumenti(Db);
			
			AciConnection connection = null;
			
			if(Db.equals("CaseMobileConsumer")||Db.equals("CaseFissoConsumer")||Db.equals("IntFissoConsumer"))
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			else
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			
			AciAction aciAction = new AciAction("ClusterSGDocsServe");
			aciAction.setParameter(new ActionParameter("SourceJobname", spectroTo.getDataSetName()));
			aciAction.setParameter(new ActionParameter("StartDate", spectroTo.getFromDate()));
			aciAction.setParameter(new ActionParameter("EndDate", spectroTo.getToDate()));
			aciAction.setParameter(new ActionParameter("Cluster", spectroTo.getClusterId()));
			aciAction.setParameter(new ActionParameter("NumResults", "1000"));
			AciResponse response = connection.aciActionExecute(aciAction);
			
			if(response.checkForSuccess())
			{
				
				ArrayList<DocumentoTO> docList = new ArrayList<DocumentoTO>();
				DocumentoTO DocumentTo = null;
				BigInteger bi = null;
				BigInteger div = new BigInteger("2");

				AciResponse responseClust = response.findFirstOccurrence("autn:cluster");

				if(responseClust != null)
				{
					pectroTo = new SpectroTO();

					pectroTo.setClusterTitle(responseClust.getTagValue("autn:title", ""));
					pectroTo.setClusterScore(responseClust.getTagValue("autn:score", ""));
					pectroTo.setNumDocs(responseClust.getTagValue("autn:numdocs", ""));
					AciResponse responseDocs = responseClust.findFirstOccurrence("autn:doc");

					while(responseDocs != null)
					{
						DocumentTo = new DocumentoTO();

						DocumentTo.setTotaleDocumenti(numDoc);
						DocumentTo.setTitleDoc(responseDocs.getTagValue("autn:title", ""));
						DocumentTo.setReferenceDoc(responseDocs.getTagValue("autn:ref", ""));
						String summ = "";
						AciResponse summary = responseDocs.findFirstEnclosedOccurrence("autn:summary"); 
						if(summary != null)
							summ = summary.getValue();
//							documentoTo.setSummary);
						else
							summ = DocumentTo.getTitleDoc();
						//.setSummary(responseDoc.getTagValue("autn:title", ""));
						
						DocumentTo.setSummary(summ);
						DocumentTo.setDataBase(Db);
						if(!responseDocs.getTagValue("autn:score", "1").equalsIgnoreCase("1"))
						{
							bi = new BigInteger(responseDocs.getTagValue("autn:score", "1"));
							DocumentTo.setScore(bi.divideAndRemainder(div)[0].toString());
						}
						else
							DocumentTo.setScore("1");

						docList.add(DocumentTo);
						responseDocs = responseDocs.next("autn:doc");
					}
					pectroTo.setDocList(docList);
				}

			}
			
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		
		return pectroTo;
		
	}
	
	public ArrayList<DocumentoTO> SuggestOnText(String ambito, String contesto, String relevance, String numRes, String text, String silos) throws Exception
	{
		ArrayList<DocumentoTO> suggestList = null;
		DocumentoTO suggest = null;
		String dbQ = null;
		
		if(ambito.equals("Consumer"))
		{
			if(contesto.equals("FISSO"))
			{
				if(silos.equalsIgnoreCase("INFORMAZIONI"))
					dbQ = "ConsumerTIntFisso";
				else if(silos.equalsIgnoreCase("ASSISTENZA"))
					dbQ = "ConsumerTIntFissoAss";
				else
					dbQ = "ConsumerTIntFisso,ConsumerTIntFissoAss";
					
			}
			else
			{
				if(silos.equalsIgnoreCase("INFORMAZIONI"))
					dbQ = "ConsumerTIntMobile";
				else if(silos.equalsIgnoreCase("ASSISTENZA"))
					dbQ = "ConsumerTIntMobileAss";
				else
					dbQ = "ConsumerTIntMobile,ConsumerTIntMobileAss";
			}
			
		}
		else
		{
			if(contesto.equals("FISSO"))
			{
				dbQ = "CorporateTIntFisso";
			}
			else
			{
				dbQ = "CorporateTIntMobile";
			}
		}
		
		text = text.replace("?", " ");
		text = text.replace(":", " ");
		text = text.replace(".", " ");
		text = text.replace(",", " ");
		text = text.replace("!", " ");
		text = text.replace("'", " ");
		
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		AciAction aciSuggest = new AciAction("query");
		String textUp = testoList(text);
		String textUpTrim = textUp.trim();
		//System.out.println("testo query: " + textUpTrim);
		//aciSuggest.setParameter(new ActionParameter("text", text));
		aciSuggest.setParameter(new ActionParameter("text", textUpTrim));
		aciSuggest.setParameter(new ActionParameter("databasematch", dbQ));
		//aciSuggest.setParameter(new ActionParameter("databasematch", "news"));
		aciSuggest.setParameter(new ActionParameter("Synonym", "true"));
		/*String minScore = "60";
		if(dbQ.equals("ConsumerTIntFisso")){
			minScore = "40";
		}*/
		aciSuggest.setParameter(new ActionParameter("minscore", relevance));

		aciSuggest.setParameter(new ActionParameter("maxresults", numRes));
		aciSuggest.setParameter(new ActionParameter("print", "all"));
		aciSuggest.setParameter(new ActionParameter("OutputEncoding", "UTF8"));
		//aciSuggest.setParameter(new ActionParameter("MatchAllTerms", "true"));
		//if(textUpTrim.contains("\""))
			aciSuggest.setParameter(new ActionParameter("MinLinks", "2"));
			aciSuggest.setParameter(new ActionParameter("Highlight", "terms"));
		/*else
			aciSuggest.setParameter(new ActionParameter("MinLinks", "3"));*/
		
		AciResponse responseSuggest = connection.aciActionExecute(aciSuggest);
		if(responseSuggest.checkForSuccess())
		{
			suggestList = new ArrayList<DocumentoTO>();
			AciResponse hitsSuggest = responseSuggest.findFirstOccurrence("autn:hit");
			while(hitsSuggest != null)
			{
				suggest = new DocumentoTO();
				
				suggest.setDataBase(hitsSuggest.getTagValue("autn:database"));
				suggest.setReferenceDoc(hitsSuggest.getTagValue("autn:reference"));
				suggest.setScore(hitsSuggest.getTagValue("autn:weight"));
				//suggest.setSummary(hitsSuggest.getTagValue("autn:summary"));
				suggest.setSummary(hitsSuggest.getTagValue("DRECONTENT"));
				suggest.setTitleDoc(hitsSuggest.getTagValue("DRETITLE"));
				//suggest.setTitleDoc(hitsSuggest.getTagValue("autn:title"));
				suggest.setRisposta(hitsSuggest.getTagValue("RISPOSTE",""));
				suggest.setQuery(text);
				suggestList.add(suggest);
				
				hitsSuggest = hitsSuggest.next("autn:hit");
			}
		}

		
		return suggestList;
	}
	
	/*public ArrayList<DocumentoTO> SuggestOnText(String ambito, String contesto, String relevance, String numRes, String text) throws Exception
	{
		ArrayList<DocumentoTO> suggestList = null;
		DocumentoTO suggest = null;
		String dbQ = null;
		
		if(ambito.equals("Consumer"))
		{
			if(contesto.equals("FISSO"))
			{
				dbQ = "ConsumerTIntFisso";
			}
			else
			{
				dbQ = "ConsumerTIntMobile";
			}
			
		}
		else
		{
			if(contesto.equals("FISSO"))
			{
				dbQ = "CorporateTIntFisso";
			}
			else
			{
				dbQ = "CorporateTIntMobile";
			}
		}
		
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		AciAction aciSuggest = new AciAction("SuggestOnText");
		aciSuggest.setParameter(new ActionParameter("text", text));
		aciSuggest.setParameter(new ActionParameter("databasematch", dbQ));
		
		//String minScore = "60";
		//if(dbQ.equals("ConsumerTIntFisso")){
		//	minScore = "40";
		//}
		aciSuggest.setParameter(new ActionParameter("minscore", relevance));

		aciSuggest.setParameter(new ActionParameter("maxresults", numRes));
		aciSuggest.setParameter(new ActionParameter("print", "all"));
		aciSuggest.setParameter(new ActionParameter("OutputEncoding", "UTF8"));
		AciResponse responseSuggest = connection.aciActionExecute(aciSuggest);
		if(responseSuggest.checkForSuccess())
		{
			suggestList = new ArrayList<DocumentoTO>();
			AciResponse hitsSuggest = responseSuggest.findFirstOccurrence("autn:hit");
			while(hitsSuggest != null)
			{
				suggest = new DocumentoTO();
				
				suggest.setDataBase(hitsSuggest.getTagValue("autn:database"));
				suggest.setReferenceDoc(hitsSuggest.getTagValue("autn:reference"));
				suggest.setScore(hitsSuggest.getTagValue("autn:weight"));
				//suggest.setSummary(hitsSuggest.getTagValue("autn:summary"));
				suggest.setSummary(hitsSuggest.getTagValue("DRECONTENT"));
				suggest.setTitleDoc(hitsSuggest.getTagValue("autn:title"));
				
				suggestList.add(suggest);
				
				hitsSuggest = hitsSuggest.next("autn:hit");
			}
		}

		
		return suggestList;
	}*/
	
	public ArrayList<DocumentoTO> TripletteSuggestAutonomy(String ambito, String contesto, String specifica, String tripletta, String minScore) throws Exception
	{
		DocumentoTO primaQuery = null;
		DocumentoTO suggest = null;
		ArrayList<DocumentoTO> suggestList = null;
		String dbQ = null;
		String dbS = null;
		if(ambito.equals("Consumer"))
		{
			if(contesto.equals("FISSO"))
			{
				if(specifica.equals("INTERAZIONI"))
				{
					dbQ = "ConsumerTIntFisso,ConsumerTIntFissoAss";
					dbS = "IntFissoConsumer";
				}
				else
				{
					dbS = "CaseFissoConsumer";
					dbQ = "ConsumerTIntFisso,ConsumerTIntFissoAss";
				}
			}
			else
			{
				if(specifica.equals("INTERAZIONI"))
				{
					dbS = "IntMobileConsumer";
					dbQ = "ConsumerTIntMobile,ConsumerTIntMobileAss";
				}
				else
				{
					dbS = "CaseMobileConsumer";
					dbQ = "ConsumerTIntMobile,ConsumerTIntMobileAss";
				}
			}
		}
		else
		{
			if(contesto.equals("FISSO"))
			{
				if(specifica.equals("INTERAZIONI"))
				{
					dbQ = "CorporateTIntFisso";
					dbS = "IntFissoCorporate";
				}
				else
				{
					dbS = "CaseFissoCorporate";
					dbQ = "CorporateTIntFisso";
				}
			}
			else
			{
				if(specifica.equals("INTERAZIONI"))
				{
					dbS = "IntMobileCorporate";
					dbQ = "CorporateTIntMobile";
				}
				else
				{
					dbS = "CaseMobileCorporate";
					dbQ = "CorporateTIntMobile";
				}
			}
		}
			AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			
			
			AciAction aciAction = new AciAction("Query");
			String param = "MATCH{"+tripletta+"}:DRETITLE";
			aciAction.setParameter(new ActionParameter("FieldText", param));
			aciAction.setParameter(new ActionParameter("databasematch", dbQ));
			aciAction.setParameter(new ActionParameter("print", "all"));
			AciResponse response = connection.aciActionExecute(aciAction);
			
			if(response.checkForSuccess())
			{
				AciResponse hit = response.findFirstOccurrence("autn:hit");
				if(hit != null)
				{
					primaQuery = new DocumentoTO();
					primaQuery.setId(hit.getTagValue("autn:id",""));
					primaQuery.setSummary(hit.getTagValue("DRECONTENT",""));
					
				}
				
			}
			if(primaQuery != null)
			{
				AciAction aciSuggest = new AciAction("SuggestOnText");
				aciSuggest.setParameter(new ActionParameter("text", primaQuery.getSummary()));
				aciSuggest.setParameter(new ActionParameter("databasematch", dbS));
				
/*				String minScore = "60";
				if(dbQ.equals("ConsumerTIntFisso")){
					minScore = "40";
				}
*/				aciSuggest.setParameter(new ActionParameter("minscore", minScore));

				aciSuggest.setParameter(new ActionParameter("maxresults", "1000"));
				aciSuggest.setParameter(new ActionParameter("print", "all"));
				AciResponse responseSuggest = null;
				if(dbS.equalsIgnoreCase("CaseMobileConsumer")||dbS.equalsIgnoreCase("CaseFissoConsumer")||dbS.equalsIgnoreCase("IntFissoConsumer"))
					responseSuggest = connection.aciActionExecute(aciSuggest);
				else
				{	
					AciConnection connectionSuggest = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
					connectionSuggest.setCharacterEncoding(IDOLEncodings.UTF8);
					responseSuggest = connectionSuggest.aciActionExecute(aciSuggest);
				}
				if(responseSuggest.checkForSuccess())
				{
					suggestList = new ArrayList<DocumentoTO>();
					AciResponse hitsSuggest = responseSuggest.findFirstOccurrence("autn:hit");
					while(hitsSuggest != null)
					{
						suggest = new DocumentoTO();
						
						suggest.setDataBase(hitsSuggest.getTagValue("autn:database"));
						suggest.setReferenceDoc(hitsSuggest.getTagValue("autn:reference"));
						suggest.setScore(hitsSuggest.getTagValue("autn:weight"));
						//suggest.setSummary(hitsSuggest.getTagValue("autn:summary"));
						suggest.setSummary(hitsSuggest.getTagValue("DRECONTENT"));
						suggest.setTitleDoc(hitsSuggest.getTagValue("autn:title"));
						
						suggestList.add(suggest);
						
						hitsSuggest = hitsSuggest.next("autn:hit");
					}
				}
			}
					
		return suggestList;
	}
	
	public DocumentoQueryTO GetContentTriplette(String reference, String dbS, String text) throws AutonomyException
	{
		DocumentoQueryTO document = null;
		try{
			AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			
			//String textUp = testoList(text);
			//String textUpTrim = textUp.trim();
			
			text = text.replace("?", " ");
			text = text.replace(":", " ");
			text = text.replace(".", " ");
			text = text.replace(",", " ");
			text = text.replace("!", " ");
			
			AciAction aciAction = new AciAction("Query");
			aciAction.setParameter(new ActionParameter("MatchReference", reference));
			aciAction.setParameter(new ActionParameter("text", text));
			aciAction.setParameter(new ActionParameter("databasematch", dbS));
			aciAction.setParameter(new ActionParameter("print", "all"));
			aciAction.setParameter(new ActionParameter("Highlight", "terms"));
			aciAction.setParameter(new ActionParameter("Synonym", "true"));
			AciResponse response = connection.aciActionExecute(aciAction);
			
			if(response.checkForSuccess())
			{
				AciResponse hits = response.findFirstOccurrence("autn:hit");
				if(hits != null)
				{
					document = new DocumentoQueryTO();
					
					document.setContent(hits.getTagValue("DRECONTENT",""));
					document.setTitleDoc(hits.getTagValue("DRETITLE",""));
					//document.setTitleDoc(hits.getTagValue("autn:title",""));
					document.setReferenceDoc(hits.getTagValue("autn:reference",""));
					document.setDataBase(hits.getTagValue("DREDBNAME",""));
					document.setRisposta(hits.getTagValue("RISPOSTE",""));
					
				}
				
			}
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		return document;
	}
	public DocumentoQueryTO GetContentSottolineato(String reference, String dbS, String text) throws AutonomyException
	{
		DocumentoQueryTO document = null;
		try{
			AciConnection connection = null;
			if(dbS.equalsIgnoreCase("IntMobileConsumer"))
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			else
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			
			//String textUp = testoList(text);
			//String textUpTrim = textUp.trim();
			
			text = text.replace("?", " ");
			text = text.replace(":", " ");
			text = text.replace(".", " ");
			text = text.replace(",", " ");
			text = text.replace("!", " ");
			
			AciAction aciAction = new AciAction("Query");
			aciAction.setParameter(new ActionParameter("MatchReference", reference));
			aciAction.setParameter(new ActionParameter("text", text));
			aciAction.setParameter(new ActionParameter("databasematch", dbS));
			aciAction.setParameter(new ActionParameter("print", "all"));
			if(!text.equalsIgnoreCase("*"))
				aciAction.setParameter(new ActionParameter("Highlight", "terms"));
			//aciAction.setParameter(new ActionParameter("Synonym", "true"));
			AciResponse response = connection.aciActionExecute(aciAction);
			
			if(response.checkForSuccess())
			{
				AciResponse hits = response.findFirstOccurrence("autn:hit");
				if(hits != null)
				{
					document = new DocumentoQueryTO();
					
					document.setContent(hits.getTagValue("DRECONTENT",""));
					//document.setTitleDoc(hits.getTagValue("autn:title",""));
					document.setTitleDoc(hits.getTagValue("DRETITLE",""));
					document.setReferenceDoc(hits.getTagValue("autn:reference",""));
					document.setDataBase(hits.getTagValue("DREDBNAME",""));
					
					if(dbS.equals("ConsumerTIntMobile")) //ConsumerTIntMobile
						document.setRisposta(hits.getTagValue("RISPOSTE",""));
					
					if(dbS.equals("IntFissoConsumer"))
					{
						//document.setDataBase(hits.getTagValue("autn:database",""));
						document.setScore(hits.getTagValue("autn:weight",""));
						//document.setContent(hits.getTagValue("DRECONTENT",""));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title",""));
						document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
						document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
						document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
						document.setDirezione(hits.getTagValue("DIREZIONE",""));
						document.setMotivo(hits.getTagValue("MOTIVO",""));
						document.setArgomento(hits.getTagValue("ARGOMENTO",""));
						document.setSpecifica(hits.getTagValue("SPECIFICA",""));
						document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
						document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
						document.setStato(hits.getTagValue("STATO",""));
						document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
						document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
						document.setCodCase(hits.getTagValue("COD_CASE",""));
						document.setSegmento(hits.getTagValue("SEGMENTO",""));
						document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
						document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
						
					}
					if(dbS.equals("CaseFissoConsumer"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setContent(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
						document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
						document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
						document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
						document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
						document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
						document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
						document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
						document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
						document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
						document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
						document.setStato(hits.getTagValue("STATO",""));
						document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
						document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
						document.setCodCase(hits.getTagValue("COD_CASE",""));
						document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
						document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
						document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
					}
					if(dbS.equals("IntMobileConsumer"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setContent(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
						document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
						document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
						document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
						document.setDirezione(hits.getTagValue("DIREZIONE",""));
						document.setMotivo(hits.getTagValue("MOTIVO",""));
						document.setArgomento(hits.getTagValue("ARGOMENTO",""));
						document.setSpecifica(hits.getTagValue("SPECIFICA",""));
						document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
						document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
						document.setStato(hits.getTagValue("STATO",""));
						document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
						document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
						document.setCodCase(hits.getTagValue("COD_CASE",""));
						document.setSegmento(hits.getTagValue("SEGMENTO",""));
						document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
						document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
					}
					if(dbS.equals("CaseMobileConsumer"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setContent(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
						document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
						document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
						document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
						document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
						document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
						document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
						document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
						document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
						document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
						document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
						document.setStato(hits.getTagValue("STATO",""));
						document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
						document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
						document.setCodCase(hits.getTagValue("COD_CASE",""));
						document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
						document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
						document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
					}
					if(dbS.equals("IntFissoCorporate"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setSummary(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
					}
					if(dbS.equals("CaseFissoCorporate"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setSummary(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
					}
					if(dbS.equals("IntMobileCorporate"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setSummary(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
					}
					if(dbS.equals("CaseMobileCorporate"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setSummary(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
					}
				
			}
		}
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		return document;
	}
	
	public DocumentoQueryTO GetContentQuery(String reference, String dbS) throws AutonomyException
	{
		DocumentoQueryTO document = null;
		try{
			AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			AciAction aciAction = new AciAction("GetContent");
			aciAction.setParameter(new ActionParameter("reference", reference));
			aciAction.setParameter(new ActionParameter("databasematch", dbS));
			aciAction.setParameter(new ActionParameter("print", "all"));
			AciResponse response = connection.aciActionExecute(aciAction);
			
			if(response.checkForSuccess())
			{
				AciResponse hits = response.findFirstOccurrence("autn:hit");
				if(hits != null)
				{
					document = new DocumentoQueryTO();
					
					document.setContent(hits.getTagValue("DRECONTENT",""));
					document.setTitleDoc(hits.getTagValue("autn:title",""));
					document.setReferenceDoc(hits.getTagValue("autn:reference",""));
					document.setDataBase(hits.getTagValue("DREDBNAME",""));
					
					if(dbS.equals("ConsumerTIntMobile")) //ConsumerTIntMobile
						document.setRisposta(hits.getTagValue("RISPOSTE",""));
					
					if(dbS.equals("IntFissoConsumer"))
					{
						//document.setDataBase(hits.getTagValue("autn:database",""));
						document.setScore(hits.getTagValue("autn:weight",""));
						//document.setContent(hits.getTagValue("DRECONTENT",""));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title",""));
						document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
						document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
						document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
						document.setDirezione(hits.getTagValue("DIREZIONE",""));
						document.setMotivo(hits.getTagValue("MOTIVO",""));
						document.setArgomento(hits.getTagValue("ARGOMENTO",""));
						document.setSpecifica(hits.getTagValue("SPECIFICA",""));
						document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
						document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
						document.setStato(hits.getTagValue("STATO",""));
						document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
						document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
						document.setCodCase(hits.getTagValue("COD_CASE",""));
						document.setSegmento(hits.getTagValue("SEGMENTO",""));
						document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
						document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
						
					}
					if(dbS.equals("CaseFissoConsumer"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setContent(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
						document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
						document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
						document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
						document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
						document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
						document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
						document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
						document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
						document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
						document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
						document.setStato(hits.getTagValue("STATO",""));
						document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
						document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
						document.setCodCase(hits.getTagValue("COD_CASE",""));
						document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
						document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
						document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
					}
					if(dbS.equals("IntMobileConsumer"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setContent(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
						document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
						document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
						document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
						document.setDirezione(hits.getTagValue("DIREZIONE",""));
						document.setMotivo(hits.getTagValue("MOTIVO",""));
						document.setArgomento(hits.getTagValue("ARGOMENTO",""));
						document.setSpecifica(hits.getTagValue("SPECIFICA",""));
						document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
						document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
						document.setStato(hits.getTagValue("STATO",""));
						document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
						document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
						document.setCodCase(hits.getTagValue("COD_CASE",""));
						document.setSegmento(hits.getTagValue("SEGMENTO",""));
						document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
						document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
					}
					if(dbS.equals("CaseMobileConsumer"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setContent(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
						document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
						document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
						document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
						document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
						document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
						document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
						document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
						document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
						document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
						document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
						document.setStato(hits.getTagValue("STATO",""));
						document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
						document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
						document.setCodCase(hits.getTagValue("COD_CASE",""));
						document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
						document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
						document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
					}
					if(dbS.equals("IntFissoCorporate"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setSummary(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
					}
					if(dbS.equals("CaseFissoCorporate"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setSummary(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
					}
					if(dbS.equals("IntMobileCorporate"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setSummary(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
					}
					if(dbS.equals("CaseMobileCorporate"))
					{
						//document.setDataBase(hits.getTagValue("autn:database"));
						//document.setReferenceDoc(hits.getTagValue("autn:reference"));
						document.setScore(hits.getTagValue("autn:weight"));
						//document.setSummary(hits.getTagValue("DRECONTENT"));
						//document.setSummary(hits.getTagValue("autn:summary"));
						//document.setTitleDoc(hits.getTagValue("autn:title"));
					}

					
				}
				
			}
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		return document;
	}
	public DocumentoTO GetContentSocial(String reference, String db) throws AutonomyException
	{
		DocumentoTO doc = null;
		try{
			AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			AciAction aciAction = new AciAction("GetContent");
			aciAction.setParameter(new ActionParameter("reference", reference));
			aciAction.setParameter(new ActionParameter("databasematch", db));
			AciResponse response = connection.aciActionExecute(aciAction);
			
			if(response.checkForSuccess())
			{
				AciResponse document = response.findFirstOccurrence("DOCUMENT");
				if(document != null)
				{
					doc = new DocumentoTO();
					doc.setTitleDoc(document.getTagValue("DRETITLE",""));
					doc.setContent(document.getTagValue("DRECONTENT",""));
				}
				
			}
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		return doc;
	}
	
	public AciResponse HotTopicsIntMobile() throws Exception
	{
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(0);
		
		AciAction aciAction = new AciAction("Query");
		aciAction.setParameter(new ActionParameter("text", "*"));
		//aciAction.setParameter(new ActionParameter("text", "promozioni"));
		
		aciAction.setParameter(new ActionParameter("minscore", "70"));
		//aciAction.setParameter(new ActionParameter("minscore", "80"));
		aciAction.setParameter(new ActionParameter("maxresults", "50000"));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", "IntMobileConsumer"));
		aciAction.setParameter(new ActionParameter("Print", "all"));
		
		aciAction.setParameter(new ActionParameter("FieldText", "STRING{"+yesterday+"}:DATA_IMPORT"));
		//aciAction.setParameter(new ActionParameter("FieldText", "STRING{"+yesterday+"}:DATA_IMPORT"));
		//aciAction.setParameter(new ActionParameter("FieldText", "MATCH{2013-09-14}:DATA_IMPORT"));
		aciAction.setParameter(new ActionParameter("Cluster", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		if(response.checkForSuccess())	
			return response;
		else
			return null;
	}
	
	public AciResponse HotTopicsIntFisso() throws Exception
	{
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(0);
		AciAction aciAction = new AciAction("Query");
		aciAction.setParameter(new ActionParameter("text", "*"));
		
		aciAction.setParameter(new ActionParameter("minscore", "70"));
		aciAction.setParameter(new ActionParameter("maxresults", "50000"));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", "IntFissoConsumer"));
		aciAction.setParameter(new ActionParameter("Print", "all"));
		//aciAction.setParameter(new ActionParameter("FieldText", "MATCH{2013-09-11}:DATA_IMPORT"));
		aciAction.setParameter(new ActionParameter("FieldText", "STRING{"+yesterday+"}:DATA_IMPORT"));
		aciAction.setParameter(new ActionParameter("Cluster", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		if(response.checkForSuccess())	
			return response;
		else
			return null;
	}
	
	public AciResponse HotTopicsCaseMobile() throws Exception
	{
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(0);
		AciAction aciAction = new AciAction("Query");
		//aciAction.setParameter(new ActionParameter("text", "reclami assistenza all inclusive"));
		aciAction.setParameter(new ActionParameter("text", "*"));
		
		aciAction.setParameter(new ActionParameter("minscore", "70"));
		aciAction.setParameter(new ActionParameter("maxresults", "50000"));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", "CaseMobileConsumer"));
		aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("FieldText", "STRING{"+yesterday+"}:DATA_IMPORT"));
		//aciAction.setParameter(new ActionParameter("FieldText", "MATCH{2013-09-11}:DATA_IMPORT"));
		aciAction.setParameter(new ActionParameter("Cluster", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		if(response.checkForSuccess())	
			return response;
		else
			return null;
	}
	
	public AciResponse HotTopicsCaseFisso() throws Exception
	{
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(0);
		AciAction aciAction = new AciAction("Query");
		//aciAction.setParameter(new ActionParameter("text", "reclami assistenza fisso"));
		aciAction.setParameter(new ActionParameter("text", "*"));
		
		aciAction.setParameter(new ActionParameter("minscore", "70"));
		aciAction.setParameter(new ActionParameter("maxresults", "50000"));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", "CaseFissoConsumer"));
		aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("FieldText", "STRING{"+yesterday+"}:DATA_IMPORT"));
		//aciAction.setParameter(new ActionParameter("FieldText", "MATCH{2013-09-11}:DATA_IMPORT"));
		aciAction.setParameter(new ActionParameter("Cluster", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		if(response.checkForSuccess())	
			return response;
		else
			return null;
	}
	
	public ArrayList<DocumentoQueryTO> getHotTopicsCaseResults(Object response, String queryIndex) throws AutonomyException, Exception
	{
		String dbS = "";
		if(queryIndex.equals("1")){
			dbS = "CaseFissoConsumer";//Nome DB CaseFisso
		}else if(queryIndex.equals("3")){
			dbS = "CaseMobileConsumer"; //Nome DB CaseMobile
		}
		
		ArrayList<DocumentoQueryTO> documents = null;
		String numDoc = getNumTotaleDocumenti(dbS);
		AciResponse resp = (AciResponse) response;
		documents = new ArrayList<DocumentoQueryTO>();
		DocumentoQueryTO document = null;
		AciResponse hits = resp.findFirstOccurrence("autn:hit");
		while(hits != null)
		{
			document = new DocumentoQueryTO();
			
			document.setDataBase(hits.getTagValue("autn:database"));
			document.setReferenceDoc(hits.getTagValue("autn:reference"));
			document.setScore(hits.getTagValue("autn:weight"));
			document.setSummary(hits.getTagValue("DRECONTENT"));
			//document.setSummary(hits.getTagValue("autn:summary"));
			document.setTitleDoc(hits.getTagValue("autn:title"));
			document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
			document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
			document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
			document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
			document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
			document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
			document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
			document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
			document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
			document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
			document.setStato(hits.getTagValue("STATO",""));
			document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
			document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
			document.setCodCase(hits.getTagValue("COD_CASE",""));
			document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
			document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
			document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
			document.setTotaleDocumenti(numDoc);
			document.setNomeCluster(hits.getTagValue("autn:clustertitle",""));
			documents.add(document);
			
			hits = hits.next("autn:hit");
		}
		
		return documents;
	}
	public ArrayList<DocumentoQueryTO> getHotTopicsIntResults(Object response, String queryIndex) throws AutonomyException, Exception
	{
		String dbS = "";
		if(queryIndex.equals("2")){
			dbS = "IntFissoConsumer";//Nome DB IntFisso
		}else if(queryIndex.equals("4")){
			dbS = "IntMobileConsumer"; //Nome DB IntMobile
		}

		ArrayList<DocumentoQueryTO> documents = null;
		String numDoc = getNumTotaleDocumenti(dbS);
		AciResponse resp = (AciResponse) response;
		documents = new ArrayList<DocumentoQueryTO>();
		DocumentoQueryTO document = null;
		System.out.println("dentro a getHotTopicsIntResults");
		AciResponse hits = resp.findFirstOccurrence("autn:hit");
		while(hits != null)
		{
			document = new DocumentoQueryTO();
			
			document.setDataBase(hits.getTagValue("autn:database",""));
			document.setReferenceDoc(hits.getTagValue("autn:reference",""));
			document.setScore(hits.getTagValue("autn:weight",""));
			document.setSummary(hits.getTagValue("DRECONTENT",""));
			//document.setSummary(hits.getTagValue("autn:summary"));
			document.setTitleDoc(hits.getTagValue("autn:title",""));
			document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
			document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
			document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
			document.setDirezione(hits.getTagValue("DIREZIONE",""));
			document.setMotivo(hits.getTagValue("MOTIVO",""));
			document.setArgomento(hits.getTagValue("ARGOMENTO",""));
			document.setSpecifica(hits.getTagValue("SPECIFICA",""));
			document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
			document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
			document.setStato(hits.getTagValue("STATO",""));
			document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
			document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
			document.setCodCase(hits.getTagValue("COD_CASE",""));
			document.setSegmento(hits.getTagValue("SEGMENTO",""));
			document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
			document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
			document.setNomeCluster(hits.getTagValue("autn:clustertitle",""));	
			document.setTotaleDocumenti(numDoc);
			documents.add(document);
			
			hits = hits.next("autn:hit");
		}
		return documents;
	}
	
	private String changeRel(String rel)
	{
		String rank = null;
		
		rel = rel.replace(".", "");
		String scor1 = rel.substring(0, 2);
		String scor2 = rel.substring(2, 4);
		rank = scor1+"."+scor2;
		
		return rank;
	}
	
	private String getNumTotHits(String db, String val) throws Exception
	{
		AciConnection connection = null;
		String numTothits=null;
		if(db.equals("CaseMobileConsumer")||db.equals("CaseFissoConsumer")||db.equals("IntFissoConsumer"))
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		else
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		aciAction.setParameter(new ActionParameter("text", "*"));
		aciAction.setParameter(new ActionParameter("minscore", "10"));
		aciAction.setParameter(new ActionParameter("maxresults", "1"));
		aciAction.setParameter(new ActionParameter("FieldText", val.trim()));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", db));
		aciAction.setParameter(new ActionParameter("Print", "none"));
		aciAction.setParameter(new ActionParameter("TotalResults", "true"));
		//aciAction.setParameter(new ActionParameter("Highlight", "terms"));
		//aciAction.setParameter(new ActionParameter("Synonym", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		
		if(response.checkForSuccess())
		{
			numTothits = response.getTagValue("autn:totalhits", "");
		}
		
		return numTothits;
	}
	
	public ArrayList<DocumentoQueryTO> Query(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username) throws Exception
	{
		
		String dbS = null;
		ArrayList<DocumentoQueryTO> documents = null;
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoConsumer";
				else
					dbS = "CaseFissoConsumer";
					
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileConsumer";
				else
					dbS = "CaseMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		String numDoc = getNumTotaleDocumenti(dbS);
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
//		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			Object valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(valoreCorrente instanceof String){
				if(!valoreCorrente.equals("--"))
				{
/*					if(i > 0)
						valori = valori + "+AND+";
*/					
					valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//					i++;
				}
			}else if(valoreCorrente instanceof String[]){
				String[] values = (String[])valoreCorrente;
				String testNull = values[0] + values[1];
				if(testNull.trim().length()>2){
					valori = valori + "RANGE{"+values[0]+","+values[1]+ "}:" + chiaveCorrente + "+AND+";
				}
			}
		}
		if(valori.endsWith("+AND+")){
			int i = valori.lastIndexOf("+AND+");
			valori = valori.substring(0,i);
		}
		System.out.println("Valori: " + valori);
		
		if(!valori.equals(""))
			numDoc= getNumTotHits(dbS, valori);
		
		AciConnection connection = null;
		
		if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer")||dbS.equals("IntFissoConsumer"))
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		else
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		boolean relChange=false;
		if(testo.contains("(NOT)"))
		{	
			testo = testo.replace("(NOT)", "NOT");
			relChange = true;
		}
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		//aciAction.setParameter(new ActionParameter("maxresults", "20"));
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		if(relChange)
			aciAction.setParameter(new ActionParameter("absweight", "true"));
		
		if(!valori.equals(""))
			aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("Highlight", "terms"));
		//aciAction.setParameter(new ActionParameter("Synonym", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		
		if(response.checkForSuccess())
		{
			documents = new ArrayList<DocumentoQueryTO>();
			DocumentoQueryTO document = null;
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			while(hits != null)
			{
				document = new DocumentoQueryTO();
				if(dbS.equals("IntFissoConsumer"))
				{
					document.setDataBase(hits.getTagValue("autn:database",""));
					document.setReferenceDoc(hits.getTagValue("autn:reference",""));
					if(relChange)
						document.setScore(changeRel(hits.getTagValue("autn:weight","")));
					else
						document.setScore(hits.getTagValue("autn:weight",""));
					document.setQuery(testo);
					document.setSummary(hits.getTagValue("DRECONTENT",""));
					//document.setSummary(hits.getTagValue("autn:summary"));
					//document.setTitleDoc(hits.getTagValue("autn:title",""));
					document.setTitleDoc(hits.getTagValue("DRETITLE",""));
					document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
					document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
					document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
					document.setDirezione(hits.getTagValue("DIREZIONE",""));
					document.setMotivo(hits.getTagValue("MOTIVO",""));
					document.setArgomento(hits.getTagValue("ARGOMENTO",""));
					document.setSpecifica(hits.getTagValue("SPECIFICA",""));
					document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
					document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
					document.setStato(hits.getTagValue("STATO",""));
					document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
					document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
					document.setCodCase(hits.getTagValue("COD_CASE",""));
					document.setSegmento(hits.getTagValue("SEGMENTO",""));
					document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
					document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
					
				}
				if(dbS.equals("CaseFissoConsumer"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					if(relChange)
						document.setScore(changeRel(hits.getTagValue("autn:weight","")));
					else
						document.setScore(hits.getTagValue("autn:weight",""));
					document.setQuery(testo);
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					//document.setTitleDoc(hits.getTagValue("autn:title"));
					document.setTitleDoc(hits.getTagValue("DRETITLE",""));
					document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
					document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
					document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
					document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
					document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
					document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
					document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
					document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
					document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
					document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
					document.setStato(hits.getTagValue("STATO",""));
					document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
					document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
					document.setCodCase(hits.getTagValue("COD_CASE",""));
					document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
					document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
					document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
				}
				if(dbS.equals("IntMobileConsumer"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					if(relChange)
						document.setScore(changeRel(hits.getTagValue("autn:weight","")));
					else
						document.setScore(hits.getTagValue("autn:weight",""));
					document.setQuery(testo);
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					//document.setTitleDoc(hits.getTagValue("autn:title"));
					document.setTitleDoc(hits.getTagValue("DRETITLE",""));
					document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
					document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
					document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
					document.setDirezione(hits.getTagValue("DIREZIONE",""));
					document.setMotivo(hits.getTagValue("MOTIVO",""));
					document.setArgomento(hits.getTagValue("ARGOMENTO",""));
					document.setSpecifica(hits.getTagValue("SPECIFICA",""));
					document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
					document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
					document.setStato(hits.getTagValue("STATO",""));
					document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
					document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
					document.setCodCase(hits.getTagValue("COD_CASE",""));
					document.setSegmento(hits.getTagValue("SEGMENTO",""));
					document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
					document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
				}
				if(dbS.equals("CaseMobileConsumer"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					if(relChange)
						document.setScore(changeRel(hits.getTagValue("autn:weight","")));
					else
						document.setScore(hits.getTagValue("autn:weight",""));
					document.setQuery(testo);
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					//document.setTitleDoc(hits.getTagValue("autn:title"));
					document.setTitleDoc(hits.getTagValue("DRETITLE",""));
					document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
					document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
					document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
					document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
					document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
					document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
					document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
					document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
					document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
					document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
					document.setStato(hits.getTagValue("STATO",""));
					document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
					document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
					document.setCodCase(hits.getTagValue("COD_CASE",""));
					document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
					document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
					document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
				}
				if(dbS.equals("IntFissoCorporate"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					if(relChange)
						document.setScore(changeRel(hits.getTagValue("autn:weight","")));
					else
						document.setScore(hits.getTagValue("autn:weight",""));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
				}
				if(dbS.equals("CaseFissoCorporate"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					if(relChange)
						document.setScore(changeRel(hits.getTagValue("autn:weight","")));
					else
						document.setScore(hits.getTagValue("autn:weight",""));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
				}
				if(dbS.equals("IntMobileCorporate"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					if(relChange)
						document.setScore(changeRel(hits.getTagValue("autn:weight","")));
					else
						document.setScore(hits.getTagValue("autn:weight",""));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
				}
				if(dbS.equals("CaseMobileCorporate"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					if(relChange)
						document.setScore(changeRel(hits.getTagValue("autn:weight","")));
					else
						document.setScore(hits.getTagValue("autn:weight",""));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
				}
				
				document.setTotaleDocumenti(numDoc);
				documents.add(document);
				
				hits = hits.next("autn:hit");
			}
			
		}
				
		return documents;
	}
	
	public ArrayList<DocumentoQueryTO> BuildQueryStruct(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	{
				
		String dbS = null;
		ArrayList<DocumentoQueryTO> documents = null;
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoConsumer";
				else
					dbS = "CaseFissoConsumer";
					
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileConsumer";
				else
					dbS = "CaseMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		String numDoc = getNumTotaleDocumenti(dbS);
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
//		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			Object valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(valoreCorrente instanceof String){
				if(!valoreCorrente.equals("--"))
				{
/*					if(i > 0)
						valori = valori + "+AND+";
*/					
					valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//					i++;
				}
			}else if(valoreCorrente instanceof String[]){
				String[] values = (String[])valoreCorrente;
				String testNull = values[0] + values[1];
				if(testNull.trim().length()>2){
					valori = valori + "RANGE{"+values[0]+","+values[1]+ "}:" + chiaveCorrente + "+AND+";
				}
			}
		}
		if(valori.endsWith("+AND+")){
			int i = valori.lastIndexOf("+AND+");
			valori = valori.substring(0,i);
		}
		System.out.println("Valori: " + valori);
		
		
		AciConnection connection = null;
		
		if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer")||dbS.equals("IntFissoConsumer"))
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		else
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		
		if(!valori.equals(""))
			aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		aciAction.setParameter(new ActionParameter("Print", "all"));
		//aciAction.setParameter(new ActionParameter("Synonym", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		
		if(response.checkForSuccess())
		{
			documents = new ArrayList<DocumentoQueryTO>();
			DocumentoQueryTO document = null;
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			while(hits != null)
			{
				document = new DocumentoQueryTO();
				if(dbS.equals("IntFissoConsumer"))
				{
					document.setDataBase(hits.getTagValue("autn:database",""));
					document.setReferenceDoc(hits.getTagValue("autn:reference",""));
					document.setScore(hits.getTagValue("autn:weight",""));
					document.setQuery(testo);
					document.setSummary(hits.getTagValue("DRECONTENT",""));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title",""));
					document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
					document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
					document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
					document.setDirezione(hits.getTagValue("DIREZIONE",""));
					document.setMotivo(hits.getTagValue("MOTIVO",""));
					document.setArgomento(hits.getTagValue("ARGOMENTO",""));
					document.setSpecifica(hits.getTagValue("SPECIFICA",""));
					document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
					document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
					document.setStato(hits.getTagValue("STATO",""));
					document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
					document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
					document.setCodCase(hits.getTagValue("COD_CASE",""));
					document.setSegmento(hits.getTagValue("SEGMENTO",""));
					document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
					document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
					document.setNomeQuery(nomeQuery);
				}
				if(dbS.equals("CaseFissoConsumer"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					document.setScore(hits.getTagValue("autn:weight"));
					document.setQuery(testo);
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
					document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
					document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
					document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
					document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
					document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
					document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
					document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
					document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
					document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
					document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
					document.setStato(hits.getTagValue("STATO",""));
					document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
					document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
					document.setCodCase(hits.getTagValue("COD_CASE",""));
					document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
					document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
					document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
					document.setNomeQuery(nomeQuery);
				}
				if(dbS.equals("IntMobileConsumer"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					document.setScore(hits.getTagValue("autn:weight"));
					document.setQuery(testo);
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
					document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
					document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
					document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
					document.setDirezione(hits.getTagValue("DIREZIONE",""));
					document.setMotivo(hits.getTagValue("MOTIVO",""));
					document.setArgomento(hits.getTagValue("ARGOMENTO",""));
					document.setSpecifica(hits.getTagValue("SPECIFICA",""));
					document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
					document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
					document.setStato(hits.getTagValue("STATO",""));
					document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
					document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
					document.setCodCase(hits.getTagValue("COD_CASE",""));
					document.setSegmento(hits.getTagValue("SEGMENTO",""));
					document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
					document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
					document.setNomeQuery(nomeQuery);
				}
				if(dbS.equals("CaseMobileConsumer"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					document.setScore(hits.getTagValue("autn:weight"));
					document.setQuery(testo);
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
					document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
					document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
					document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
					document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
					document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
					document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
					document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
					document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
					document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
					document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
					document.setStato(hits.getTagValue("STATO",""));
					document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
					document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
					document.setCodCase(hits.getTagValue("COD_CASE",""));
					document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
					document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
					document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
					document.setNomeQuery(nomeQuery);
				}
				if(dbS.equals("IntFissoCorporate"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					document.setScore(hits.getTagValue("autn:weight"));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
				}
				if(dbS.equals("CaseFissoCorporate"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					document.setScore(hits.getTagValue("autn:weight"));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
				}
				if(dbS.equals("IntMobileCorporate"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					document.setScore(hits.getTagValue("autn:weight"));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
				}
				if(dbS.equals("CaseMobileCorporate"))
				{
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					document.setScore(hits.getTagValue("autn:weight"));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
				}
				
				document.setTotaleDocumenti(numDoc);
				documents.add(document);
				
				hits = hits.next("autn:hit");
			}
			
		}
		
		
		return documents;
	}
	
	public ArrayList<DocumentoQueryTO> BuildQueryStructInt(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	{
				
		String dbS = null;
		ArrayList<DocumentoQueryTO> documents = null;
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				dbS = "IntFissoConsumer";
			}
			else
			{
				dbS = "IntMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		String numDoc = getNumTotaleDocumenti(dbS);
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
//		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			Object valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(valoreCorrente instanceof String){
				if(!valoreCorrente.equals("--"))
				{
/*					if(i > 0)
						valori = valori + "+AND+";
*/					
					valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//					i++;
				}
			}else if(valoreCorrente instanceof String[]){
				String[] values = (String[])valoreCorrente;
				String testNull = values[0] + values[1];
				if(testNull.trim().length()>2){
					valori = valori + "RANGE{"+values[0]+","+values[1]+ "}:" + chiaveCorrente + "+AND+";
				}
			}
		}
		if(valori.endsWith("+AND+")){
			int i = valori.lastIndexOf("+AND+");
			valori = valori.substring(0,i);
		}
		System.out.println("Valori: " + valori);
		
		
		AciConnection connection = null;
		
		if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer")||dbS.equals("IntFissoConsumer"))
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		else
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		
		if(!valori.equals(""))
			aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		//aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("Print", "fields"));
		aciAction.setParameter(new ActionParameter("PrintFields", "DRETITLE,COD_INTERAZIONE,SPECIFICA,MOTIVO,ARGOMENTO,STATO,DATA_CREAZIONE,TIPO_CANALE,DIREZIONE,COD_CLIENTE,CRM_NATIVO,SEGMENTO,TEAM_INBOX_CREAZ"));
		//aciAction.setParameter(new ActionParameter("Synonym", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		
		if(response.checkForSuccess())
		{
			documents = new ArrayList<DocumentoQueryTO>();
			DocumentoQueryTO document = null;
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			System.out.println("prima di ciclo su risultati Idol");
			while(hits != null)
			{
				document = new DocumentoQueryTO();
				
				document.setDataBase(hits.getTagValue("autn:database"));
				document.setReferenceDoc(hits.getTagValue("autn:reference"));
				document.setScore(hits.getTagValue("autn:weight"));
				document.setQuery(testo);
				//document.setSummary(hits.getTagValue("DRECONTENT"));
				//document.setSummary(hits.getTagValue("autn:summary"));
				document.setTitleDoc(hits.getTagValue("autn:title"));
				document.setCodInterazione(hits.getTagValue("COD_INTERAZIONE",""));
				document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
				document.setTipoCanale(hits.getTagValue("TIPO_CANALE",""));
				document.setDirezione(hits.getTagValue("DIREZIONE",""));
				document.setMotivo(hits.getTagValue("MOTIVO",""));
				document.setArgomento(hits.getTagValue("ARGOMENTO",""));
				document.setSpecifica(hits.getTagValue("SPECIFICA",""));
				document.setCodCliente(hits.getTagValue("COD_CLIENTE",""));
				document.setCrmNativo(hits.getTagValue("CRM_NATIVO",""));
				document.setStato(hits.getTagValue("STATO",""));
				//document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
				//document.setSubConclusioni(hits.getTagValue("SUB_CONCLUSIONI",""));
				document.setCodCase(hits.getTagValue("COD_CASE",""));
				document.setSegmento(hits.getTagValue("SEGMENTO",""));
				document.setServiceTeam(hits.getTagValue("SERVICE_TEAM",""));
				document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZ",""));
				document.setNomeQuery(nomeQuery);
				
				
				document.setTotaleDocumenti(numDoc);
				documents.add(document);
				
				hits = hits.next("autn:hit");
			}
			System.out.println("fine ciclo su risultati Idol");
		}
		
		
		return documents;
	}
	
	private int getCod(String cod)
	{
		int codice;
		
		if(cod.equals(null)||cod.trim().length()==0)
		{	
			
			codice = -1;
		}
		else
			codice = Integer.parseInt(cod);
		
		
		return codice;
		
	}
	
	/*public String BuildQueryStructIntTest(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery, Connection con, String categoriaTicket, String table) throws Exception
	{
				
		String dbS = null;
		String esito = "1";
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				dbS = "IntFissoConsumer";
			}
			else
			{
				dbS = "IntMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		String numDoc = getNumTotaleDocumenti(dbS);
		Collection<String> dataCreazioneList = (Collection<String>) chiaveValore.get("DATA_CREAZIONE"); 
		if(dataCreazioneList==null)
			System.out.println("lista vuota");
		else
		{
			for(String dataCorrente: dataCreazioneList)
			{	
			
				Set<String> chiavi = null;
				Iterator<String> iterChiavi = null;
				chiavi = chiaveValore.keySet();
				iterChiavi = chiavi.iterator();
				String valori = "";
//				int i = 0;
				while(iterChiavi.hasNext()){
					String chiaveCorrente = iterChiavi.next();
					Object valoreCorrente = chiaveValore.get(chiaveCorrente);
					if(valoreCorrente instanceof String){
						if(!valoreCorrente.equals("--"))
						{
		//					if(i > 0)
		//						valori = valori + "+AND+";
		//					
							valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//							i++;
						}
					}
				}
				
				if(valori.endsWith("+AND+")){
					int i = valori.lastIndexOf("+AND+");
					valori = valori.substring(0,i);
				}
				
				
				System.out.println("Valori: " + valori);
				
				AciConnection connection = null;
				
				if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer")||dbS.equals("IntFissoConsumer"))
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				else
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				connection.setCharacterEncoding(IDOLEncodings.UTF8);
				connection.setTimeout(600000);
				AciAction aciAction = new AciAction("Query");
				if(!testo.equals(""))
					aciAction.setParameter(new ActionParameter("text", testo));
				else
					aciAction.setParameter(new ActionParameter("text", "*"));
				
				aciAction.setParameter(new ActionParameter("minscore", relevance));
				aciAction.setParameter(new ActionParameter("maxresults", "1"));
		
				if(!valori.equals(""))
					valori = valori + "+AND+STRING{"+dataCorrente+"}:DATA_CREAZIONE";
				else
					valori = "STRING{"+dataCorrente+"}:DATA_CREAZIONE";
					
				aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
				aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
				aciAction.setParameter(new ActionParameter("Print", "all"));
				//aciAction.setParameter(new ActionParameter("Highlight", "terms"));
				//aciAction.setParameter(new ActionParameter("Synonym", "true"));
				
				AciResponse response = connection.aciActionExecute(aciAction);
		
				if(response.checkForSuccess())
				{
					System.out.println("categoriaTicket: " + tipo);
					String queryProperty = ("struttura." + tipo).toLowerCase() + "Col";
					String column = PropertiesManager.getMyProperty(queryProperty);
			
					String dataNow = null ;
					Calendar nowDate = GregorianCalendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
					dataNow = dateFormat.format(nowDate.getTime());
					String sql = null;
					AciResponse hits = response.findFirstOccurrence("autn:hit");
					System.out.println("prima di ciclo su risultati Idol");
					PreparedStatement ps = null;
					while(hits != null)
					{
						System.out.println("dentro while");
						AciResponse cont = hits.findFirstOccurrence("autn:content");
						AciResponse doc = cont.findFirstOccurrence("DOCUMENT");
						if(table.equalsIgnoreCase("autonomy_int_mobile"))
						{	
							sql = "INSERT INTO " + table + " ( " + column + " ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							//sql = "INSERT INTO " + table + " ( " + column + " ) VALUES ('"+hits.getTagValue("autn:reference")+"','"+hits.getTagValue("autn:title")+"','"+hits.getTagValue("autn:database")+"',"+hits.getTagValue("autn:weight")+",'','"+nomeQuery+"',"+0+")";
							System.out.println("sql: " + sql);
							ps = con.prepareStatement(sql.trim());
							ps.setString(1, doc.getTagValue("DREREFERENCE",""));
							ps.setString(2, doc.getTagValue("DRETITLE",""));
							ps.setString(3, doc.getTagValue("DREDBNAME",""));
							ps.setInt(4, Integer.parseInt(doc.getTagValue("COD_INTERAZIONE","")));
							ps.setString(5, doc.getTagValue("SPECIFICA",""));
							ps.setString(6, doc.getTagValue("MOTIVO",""));
							ps.setString(7, doc.getTagValue("ARGOMENTO",""));
							ps.setString(8, doc.getTagValue("STATO",""));
							ps.setString(9, doc.getTagValue("DATA_CREAZIONE",""));
							ps.setString(10, doc.getTagValue("TIPO_CANALE",""));
							ps.setString(11, doc.getTagValue("DIREZIONE",""));
							ps.setString(12, doc.getTagValue("COD_CLIENTE",""));
							ps.setString(13, doc.getTagValue("CRM_NATIVO",""));
							ps.setString(14, doc.getTagValue("CONCLUSIONI",""));
							ps.setString(15, doc.getTagValue("SUB_CONCLUSIONI",""));
							ps.setInt(16, Integer.parseInt(doc.getTagValue("COD_CASE","")));
							ps.setString(17, doc.getTagValue("SEGMENTO",""));
							ps.setString(18, doc.getTagValue("SERVICE_TEAM",""));
							ps.setString(19, dataNow);
							ps.setString(20, doc.getTagValue("TEAM_INBOX_CREAZ",""));
							ps.setDouble(21, Double.parseDouble(hits.getTagValue("autn:weight","")));
							ps.setString(22, doc.getTagValue("DRECONTENT",""));
							ps.setString(23, nomeQuery);
							ps.setInt(24, 0);
							try{
							int i = ps.executeUpdate();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
				
						//ps = con.createStatement();
						//ps.executeUpdate(sql);
				
						if(ps!=null) ps.close();
				
						System.out.println("inserito record :" + System.currentTimeMillis()); 
						hits = hits.next("autn:hit");
						esito = "0";
					}
					System.out.println("fine ciclo su risultati Idol");
				}
			}
		}	
		System.out.println("fine inserimento su tabella risultati Idol");
		return esito;
	}*/
	
	public String BuildQueryStructIntTest(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery, Connection con, String categoriaTicket, String table) throws Exception
	{
				
		String dbS = null;
		String esito = "1";
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				dbS = "IntFissoConsumer";
			}
			else
			{
				dbS = "IntMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		boolean relChange=false;
		if(testo.contains("(NOT)"))
		{	
			testo = testo.replace("(NOT)", "NOT");
			relChange = true;
		}
		
		String numDoc = getNumTotaleDocumenti(dbS);
		Collection<String> dataCreazioneList = (Collection<String>) chiaveValore.get("DATA_CREAZIONE"); 
		if(dataCreazioneList==null)
			System.out.println("lista vuota");
		else
		{
			for(String dataCorrente: dataCreazioneList)
			{	
			
				Set<String> chiavi = null;
				Iterator<String> iterChiavi = null;
				chiavi = chiaveValore.keySet();
				iterChiavi = chiavi.iterator();
				String valori = "";
//				int i = 0;
				while(iterChiavi.hasNext()){
					String chiaveCorrente = iterChiavi.next();
					Object valoreCorrente = chiaveValore.get(chiaveCorrente);
					if(valoreCorrente instanceof String){
						if(!valoreCorrente.equals("--"))
						{
		//					if(i > 0)
		//						valori = valori + "+AND+";
		//					
							valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//							i++;
						}
					}
				}
				
				if(valori.endsWith("+AND+")){
					int i = valori.lastIndexOf("+AND+");
					valori = valori.substring(0,i);
				}
				
				
				System.out.println("Valori: " + valori);
				
				AciConnection connection = null;
				
				if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer")||dbS.equals("IntFissoConsumer"))
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				else
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				connection.setCharacterEncoding(IDOLEncodings.UTF8);
				connection.setTimeout(600000);
				AciAction aciAction = new AciAction("Query");
				if(!testo.equals(""))
					aciAction.setParameter(new ActionParameter("text", testo));
				else
					aciAction.setParameter(new ActionParameter("text", "*"));
				
				aciAction.setParameter(new ActionParameter("minscore", relevance));
				aciAction.setParameter(new ActionParameter("maxresults", "2000"));
				
				if(relChange)
					aciAction.setParameter(new ActionParameter("absweight", "true"));
		
				if(!valori.equals(""))
					valori = valori + "+AND+STRING{"+dataCorrente+"}:DATA_CREAZIONE";
				else
					valori = "STRING{"+dataCorrente+"}:DATA_CREAZIONE";
					
				aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
				aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
				aciAction.setParameter(new ActionParameter("Print", "all"));
				//aciAction.setParameter(new ActionParameter("Highlight", "terms"));
				//aciAction.setParameter(new ActionParameter("Synonym", "true"));
				
				AciResponse response = connection.aciActionExecute(aciAction);
		
				if(response.checkForSuccess())
				{
					System.out.println("categoriaTicket: " + tipo);
					String queryProperty = ("struttura." + tipo).toLowerCase() + "Col";
					String column = PropertiesManager.getMyProperty(queryProperty);
			
					String dataNow = null ;
					Calendar nowDate = GregorianCalendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
					dataNow = dateFormat.format(nowDate.getTime());
					String sql = null;
					AciResponse hits = response.findFirstOccurrence("autn:hit");
					System.out.println("prima di ciclo su risultati Idol");
					Statement ps = null;
					while(hits != null)
					{
						System.out.println("dentro while");
						AciResponse cont = hits.findFirstOccurrence("autn:content");
						AciResponse doc = cont.findFirstOccurrence("DOCUMENT");
						if(relChange)
							sql = "INSERT INTO " + table + " ( " + column + " ) VALUES ('"+doc.getTagValue("DREREFERENCE","")+"','"+doc.getTagValue("DRETITLE","")+"','"+doc.getTagValue("DREDBNAME","")+"',"+getCod(doc.getTagValue("COD_INTERAZIONE",""))+",'"+doc.getTagValue("SPECIFICA","")+"','"+doc.getTagValue("MOTIVO","")+"','"+doc.getTagValue("ARGOMENTO","")+"','"+doc.getTagValue("STATO","")+"','"+doc.getTagValue("DATA_CREAZIONE","")+"','"+doc.getTagValue("TIPO_CANALE","")+"','"+doc.getTagValue("DIREZIONE","")+"','"+doc.getTagValue("COD_CLIENTE","")+"','"+doc.getTagValue("CRM_NATIVO","")+"','"+doc.getTagValue("CONCLUSIONI","").replace("'", "").replace("\"", "")+"','"+doc.getTagValue("SUB_CONCLUSIONI","").replace("'", "").replace("\"", "")+"',"+getCod(doc.getTagValue("COD_CASE",""))+",'"+doc.getTagValue("SEGMENTO","")+"','"+doc.getTagValue("SERVICE_TEAM","")+"','"+dataNow+"','"+doc.getTagValue("TEAM_INBOX_CREAZ","")+"',"+changeRel(hits.getTagValue("autn:weight",""))+",'"+doc.getTagValue("DRECONTENT","").replace("'", "").replace("\"", "")+"','"+nomeQuery+"',"+0+")";
						else
							sql = "INSERT INTO " + table + " ( " + column + " ) VALUES ('"+doc.getTagValue("DREREFERENCE","")+"','"+doc.getTagValue("DRETITLE","")+"','"+doc.getTagValue("DREDBNAME","")+"',"+getCod(doc.getTagValue("COD_INTERAZIONE",""))+",'"+doc.getTagValue("SPECIFICA","")+"','"+doc.getTagValue("MOTIVO","")+"','"+doc.getTagValue("ARGOMENTO","")+"','"+doc.getTagValue("STATO","")+"','"+doc.getTagValue("DATA_CREAZIONE","")+"','"+doc.getTagValue("TIPO_CANALE","")+"','"+doc.getTagValue("DIREZIONE","")+"','"+doc.getTagValue("COD_CLIENTE","")+"','"+doc.getTagValue("CRM_NATIVO","")+"','"+doc.getTagValue("CONCLUSIONI","").replace("'", "").replace("\"", "")+"','"+doc.getTagValue("SUB_CONCLUSIONI","").replace("'", "").replace("\"", "")+"',"+getCod(doc.getTagValue("COD_CASE",""))+",'"+doc.getTagValue("SEGMENTO","")+"','"+doc.getTagValue("SERVICE_TEAM","")+"','"+dataNow+"','"+doc.getTagValue("TEAM_INBOX_CREAZ","")+"',"+hits.getTagValue("autn:weight","")+",'"+doc.getTagValue("DRECONTENT","").replace("'", "").replace("\"", "")+"','"+nomeQuery+"',"+0+")";
						
						ps = con.createStatement();
						ps.executeUpdate(sql);
				
						if(ps!=null) ps.close();
				
						System.out.println("inserito record in: " + table); 
						hits = hits.next("autn:hit");
						esito = "0";
					}
					System.out.println("fine ciclo su risultati Idol");
				}
			}
		}	
		System.out.println("fine inserimento su tabella risultati Idol");
		return esito;
	}
	
	public String BuildQueryStructCaseTest(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery, Connection con, String categoriaTicket, String table) throws Exception
	{
				
		String dbS = null;
		String esito = "1";
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				dbS = "CaseFissoConsumer";
					
			}
			else
			{
				dbS = "CaseMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		boolean relChange=false;
		if(testo.contains("(NOT)"))
		{	
			testo = testo.replace("(NOT)", "NOT");
			relChange = true;
		}
		
		String numDoc = getNumTotaleDocumenti(dbS);
		Collection<String> dataCreazioneList = (Collection<String>) chiaveValore.get("DATA_CREAZIONE"); 
		if(dataCreazioneList==null)
			System.out.println("lista vuota");
		else
		{
			for(String dataCorrente: dataCreazioneList)
			{	
			
				Set<String> chiavi = null;
				Iterator<String> iterChiavi = null;
				chiavi = chiaveValore.keySet();
				iterChiavi = chiavi.iterator();
				String valori = "";
//				int i = 0;
				while(iterChiavi.hasNext()){
					String chiaveCorrente = iterChiavi.next();
					Object valoreCorrente = chiaveValore.get(chiaveCorrente);
					if(valoreCorrente instanceof String){
						if(!valoreCorrente.equals("--"))
						{
		//					if(i > 0)
		//						valori = valori + "+AND+";
		//					
							valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//							i++;
						}
					}
				}
				
				if(valori.endsWith("+AND+")){
					int i = valori.lastIndexOf("+AND+");
					valori = valori.substring(0,i);
				}
				
				
				System.out.println("Valori: " + valori);
				
				AciConnection connection = null;
				
				if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer")||dbS.equals("IntFissoConsumer"))
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				else
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				connection.setCharacterEncoding(IDOLEncodings.UTF8);
				connection.setTimeout(600000);
				AciAction aciAction = new AciAction("Query");
				if(!testo.equals(""))
					aciAction.setParameter(new ActionParameter("text", testo));
				else
					aciAction.setParameter(new ActionParameter("text", "*"));
				
				aciAction.setParameter(new ActionParameter("minscore", relevance));
				aciAction.setParameter(new ActionParameter("maxresults", "2000"));
				
				if(relChange)
					aciAction.setParameter(new ActionParameter("absweight", "true"));
		
				if(!valori.equals(""))
					valori = valori + "+AND+STRING{"+dataCorrente+"}:DATA_CREAZIONE";
				else
					valori = "STRING{"+dataCorrente+"}:DATA_CREAZIONE";
					
				aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
				aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
				aciAction.setParameter(new ActionParameter("Print", "all"));
				//aciAction.setParameter(new ActionParameter("Highlight", "terms"));
				//aciAction.setParameter(new ActionParameter("Synonym", "true"));
				
				AciResponse response = connection.aciActionExecute(aciAction);
		
				if(response.checkForSuccess())
				{
					System.out.println("categoriaTicket: " + tipo);
					String queryProperty = ("struttura." + tipo).toLowerCase() + "Col";
					String column = PropertiesManager.getMyProperty(queryProperty);
			
					String dataNow = null ;
					Calendar nowDate = GregorianCalendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
					dataNow = dateFormat.format(nowDate.getTime());
					String sql = null;
					AciResponse hits = response.findFirstOccurrence("autn:hit");
					System.out.println("prima di ciclo su risultati Idol");
					Statement ps = null;
					
					while(hits != null)
					{
						System.out.println("dentro while");
						AciResponse cont = hits.findFirstOccurrence("autn:content");
						AciResponse doc = cont.findFirstOccurrence("DOCUMENT");
						if(relChange)
							sql = "INSERT INTO " + table + " ( " + column + " ) VALUES ('"+doc.getTagValue("DREREFERENCE","")+"','"+doc.getTagValue("DRETITLE","")+"',"+getCod(doc.getTagValue("COD_CASE",""))+",'"+doc.getTagValue("SPECIFICA_TRIPLETTA","")+"','"+doc.getTagValue("MOTIVO_TRIPLETTA","")+"','"+doc.getTagValue("ARGOMENTO_TRIPLETTA","")+"','"+doc.getTagValue("STATO","")+"','"+doc.getTagValue("DATA_CREAZIONE","")+"','"+doc.getTagValue("FLAG_ASSOCIATO_WTT","")+"','"+doc.getTagValue("FLAG_ASSOCIATO_RATT","")+"','"+doc.getTagValue("TEAM_INBOX_DEST","")+"','"+doc.getTagValue("CONCLUSIONI","").replace("'", "").replace("\"", "")+"','"+doc.getTagValue("SUBCONCLUSIONI","").replace("'", "").replace("\"", "")+"','"+doc.getTagValue("DESCRIZIONE_PROBLEMA","").replace("'", "").replace("\"", "")+"','"+doc.getTagValue("TEAM_INBOX_CHIUSURA","")+"','"+doc.getTagValue("DATA_CHIUSURA","")+"','"+doc.getTagValue("TEAM_INBOX_CREAZIONE","")+"','"+doc.getTagValue("ST_CODIF","")+"','"+yesterday+"','"+doc.getTagValue("DREDBNAME","")+"','"+doc.getTagValue("CODICE_CLIENTE","")+"',"+changeRel(hits.getTagValue("autn:weight",""))+",'"+doc.getTagValue("DRECONTENT","").replace("'", "").replace("\"", "")+"','"+nomeQuery+"',"+0+")";
						else
							sql = "INSERT INTO " + table + " ( " + column + " ) VALUES ('"+doc.getTagValue("DREREFERENCE","")+"','"+doc.getTagValue("DRETITLE","")+"',"+getCod(doc.getTagValue("COD_CASE",""))+",'"+doc.getTagValue("SPECIFICA_TRIPLETTA","")+"','"+doc.getTagValue("MOTIVO_TRIPLETTA","")+"','"+doc.getTagValue("ARGOMENTO_TRIPLETTA","")+"','"+doc.getTagValue("STATO","")+"','"+doc.getTagValue("DATA_CREAZIONE","")+"','"+doc.getTagValue("FLAG_ASSOCIATO_WTT","")+"','"+doc.getTagValue("FLAG_ASSOCIATO_RATT","")+"','"+doc.getTagValue("TEAM_INBOX_DEST","")+"','"+doc.getTagValue("CONCLUSIONI","").replace("'", "").replace("\"", "")+"','"+doc.getTagValue("SUBCONCLUSIONI","").replace("'", "").replace("\"", "")+"','"+doc.getTagValue("DESCRIZIONE_PROBLEMA","").replace("'", "").replace("\"", "")+"','"+doc.getTagValue("TEAM_INBOX_CHIUSURA","")+"','"+doc.getTagValue("DATA_CHIUSURA","")+"','"+doc.getTagValue("TEAM_INBOX_CREAZIONE","")+"','"+doc.getTagValue("ST_CODIF","")+"','"+yesterday+"','"+doc.getTagValue("DREDBNAME","")+"','"+doc.getTagValue("CODICE_CLIENTE","")+"',"+hits.getTagValue("autn:weight","")+",'"+doc.getTagValue("DRECONTENT","").replace("'", "").replace("\"", "")+"','"+nomeQuery+"',"+0+")";
						
						ps = con.createStatement();
						ps.executeUpdate(sql);
				
						if(ps!=null) ps.close();
				
						System.out.println("inserito record in: " + table); 
						hits = hits.next("autn:hit");
						esito = "0";
					}
					System.out.println("fine ciclo su risultati Idol");
				}
			}
		}	
		System.out.println("fine inserimento su tabella risultati Idol");
		return esito;
	}
	
	public ArrayList<DocumentoQueryTO> BuildQueryStructCase(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	{
				
		String dbS = null;
		ArrayList<DocumentoQueryTO> documents = null;
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				dbS = "CaseFissoConsumer";
					
			}
			else
			{
				dbS = "CaseMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		String numDoc = getNumTotaleDocumenti(dbS);
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
//		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			Object valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(valoreCorrente instanceof String){
				if(!valoreCorrente.equals("--"))
				{
/*					if(i > 0)
						valori = valori + "+AND+";
*/					
					valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//					i++;
				}
			}else if(valoreCorrente instanceof String[]){
				String[] values = (String[])valoreCorrente;
				String testNull = values[0] + values[1];
				if(testNull.trim().length()>2){
					valori = valori + "RANGE{"+values[0]+","+values[1]+ "}:" + chiaveCorrente + "+AND+";
				}
			}
		}
		if(valori.endsWith("+AND+")){
			int i = valori.lastIndexOf("+AND+");
			valori = valori.substring(0,i);
		}
		System.out.println("Valori: " + valori);
		
		
		AciConnection connection = null;
		
		if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer")||dbS.equals("IntFissoConsumer"))
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		else
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		
		if(!valori.equals(""))
			aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		aciAction.setParameter(new ActionParameter("Print", "all"));
		//aciAction.setParameter(new ActionParameter("Synonym", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		
		if(response.checkForSuccess())
		{
			documents = new ArrayList<DocumentoQueryTO>();
			DocumentoQueryTO document = null;
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			while(hits != null)
			{
				document = new DocumentoQueryTO();
				document.setDataBase(hits.getTagValue("autn:database"));
				document.setReferenceDoc(hits.getTagValue("autn:reference"));
				document.setScore(hits.getTagValue("autn:weight"));
				document.setQuery(testo);
				document.setSummary(hits.getTagValue("DRECONTENT"));
				//document.setSummary(hits.getTagValue("autn:summary"));
				document.setTitleDoc(hits.getTagValue("autn:title"));
				document.setDataCreazione(hits.getTagValue("DATA_CREAZIONE",""));
				document.setFlagWTT(hits.getTagValue("FLAG_ASSOCIATO_WTT",""));
				document.setFlagRATT(hits.getTagValue("FLAG_ASSOCIATO_RATT",""));
				document.setTeamInboxDest(hits.getTagValue("TEAM_INBOX_DEST",""));
				document.setDescProblema(hits.getTagValue("DESCRIZIONE_PROBLEMA",""));
				document.setMotivo(hits.getTagValue("MOTIVO_TRIPLETTA",""));
				document.setArgomento(hits.getTagValue("ARGOMENTO_TRIPLETTA",""));
				document.setSpecifica(hits.getTagValue("SPECIFICA_TRIPLETTA",""));
				document.setTeamInboxChiusura(hits.getTagValue("TEAM_INBOX_CHIUSURA",""));
				document.setDataChiusura(hits.getTagValue("DATA_CHIUSURA",""));
				document.setStato(hits.getTagValue("STATO",""));
				document.setConclusioni(hits.getTagValue("CONCLUSIONI",""));
				document.setSubConclusioni(hits.getTagValue("SUBCONCLUSIONI",""));
				document.setCodCase(hits.getTagValue("COD_CASE",""));
				document.setServiceTeam(hits.getTagValue("ST_CODIF",""));
				document.setTeamInboxCreaz(hits.getTagValue("TEAM_INBOX_CREAZIONE",""));
				document.setCodCliente(hits.getTagValue("CODICE_CLIENTE",""));
				document.setNomeQuery(nomeQuery);
								
				document.setTotaleDocumenti(numDoc);
				documents.add(document);
				
				hits = hits.next("autn:hit");
			}
			
		}
		
		
		return documents;
	}
	
	public ArrayList<DocumentoTO> QuerySocial(String root, String ticket, HashMap<String, String> chiaveValore, String numRes, String relevance, String testo) throws Exception
	{
		
		String dbS = null;
		ArrayList<DocumentoTO> documents = null;
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
				dbS = "ConsumerSocialFisso";
			else
				dbS = "ConsumerSocialMobile";
			
		}
		else
		{
			if(ticket.equals("FISSO"))
				dbS = "ConsumerSocialFisso";
			else
				dbS = "ConsumerSocialMobile";
		}
		
		String totDoc = getNumTotaleDocumenti(dbS);
		
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			String valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(i > 0)
				valori = valori + "+AND+";
			if(chiaveCorrente.equalsIgnoreCase("DRETITLE"))
				valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente;
			else	
				valori = valori + "STRING{"+valoreCorrente+"}:"+chiaveCorrente;
			i++;
			
			
		}
		System.out.println("Valori: " + valori);
		
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		/*if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		*/
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		
		if(!valori.equals(""))
			aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("Synonym", "true"));
		
		AciResponse response = connection.aciActionExecute(aciAction);
		
		if(response.checkForSuccess())
		{
			documents = new ArrayList<DocumentoTO>();
			DocumentoQueryTO document = null;
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			while(hits != null)
			{
				document = new DocumentoQueryTO();
				
				document.setTotaleDocumenti(totDoc);
				document.setDataBase(hits.getTagValue("autn:database",""));
				document.setReferenceDoc(hits.getTagValue("autn:reference",""));
				document.setScore(hits.getTagValue("autn:weight",""));
				document.setSummary(hits.getTagValue("DRECONTENT",""));
				//document.setSummary(hits.getTagValue("autn:summary"));
				document.setTitleDoc(hits.getTagValue("autn:title",""));
				
				documents.add(document);
				
				hits = hits.next("autn:hit");
			}
			
		}
		
		
		return documents;
	}
	
	public String BuildXMLStruct(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	{
		
		String dbS = null;
		System.out.println("dentro a XML Builder");
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoConsumer";
				else
					dbS = "CaseFissoConsumer";
					
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileConsumer";
				else
					dbS = "CaseMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
//		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			Object valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(valoreCorrente instanceof String){
				if(!valoreCorrente.equals("--"))
				{
/*					if(i > 0)
						valori = valori + "+AND+";
*/					
					valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//					i++;
				}
			}else if(valoreCorrente instanceof String[]){
				String[] values = (String[])valoreCorrente;
				String testNull = values[0] + values[1];
				if(testNull.trim().length()>2){
					valori = valori + "RANGE{"+values[0]+","+values[1]+ "}:" + chiaveCorrente + "+AND+";
				}
			}
		}
		if(valori.endsWith("+AND+")){
			int i = valori.lastIndexOf("+AND+");
			valori = valori.substring(0,i);
		}
		System.out.println("Valori: " + valori);
		System.out.println("Testo: " + testo);
		
		AciConnection connection = null;
		
		if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer"))
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		else
			connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		/*if(dbS.equals("IntMobileConsumer") && testo.equalsIgnoreCase("Shaping"))
		 aciAction.setParameter(new ActionParameter("minscore", "75"));
		else*/
		/*if(relevance.equalsIgnoreCase("50")||relevance.equalsIgnoreCase("60"))
			aciAction.setParameter(new ActionParameter("minscore", "70"));
		else*/
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		//aciAction.setParameter(new ActionParameter("maxresults", "100"));
		
		if(!valori.equals(""))
			aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		//aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("Print", "Fields"));
		if(dbS.equalsIgnoreCase("CaseFissoConsumer")||dbS.equalsIgnoreCase("CaseMobileConsumer"))
			aciAction.setParameter(new ActionParameter("PrintFields", "DREREFERENCE,DRETITLE,ARGOMENTO_TRIPLETTA,COD_CASE,CODICE_CLIENTE,CONCLUSIONI,DATA_CHIUSURA,DATA_CREAZIONE,DATA_IMPORT,DESCRIZIONE_PROBLEMA,DREDBNAME,FLAG_ASSOCIATO_RATT,FLAG_ASSOCIATO_WTT,MOTIVO_TRIPLETTA,SPECIFICA_TRIPLETTA,ST_CODIF,STATO,SUBCONCLUSIONI,TEAM_INBOX_CHIUSURA,TEAM_INBOX_CREAZIONE,TEAM_INBOX_DEST,DRECONTENT"));
		else
			aciAction.setParameter(new ActionParameter("PrintFields", "DREREFERENCE,DRETITLE,ARGOMENTO,COD_CASE,COD_CLIENTE,COD_INTERAZIONE,CONCLUSIONI,CRM_NATIVO,DATA_CREAZIONE,DATA_IMPORT,DIREZIONE,DREDBNAME,MOTIVO,SEGMENTO,SERVICE_TEAM,SPECIFICA,STATO,SUB_CONCLUSIONI,TEAM_INBOX_CREAZ,TIPO_CANALE,DRECONTENT"));
		
				
		AciResponse response = connection.aciActionExecute(aciAction);

		
		if(response.checkForSuccess())
		{
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			String nome = (ticket+"_"+tipo+"_"+username+"_"+nomeQuery).toLowerCase();
			String path = PropertiesManager.getMyProperty("pathXMLStruct")+nome+".xml";
			System.out.println("path file XML: " + path);
			Element autnresponse = doc.createElement("autnresponse");
			doc.appendChild(autnresponse);
			
			Element action = doc.createElement("action");
			action.appendChild(doc.createTextNode("QUERY"));
			autnresponse.appendChild(action);
			
			Element responseX = doc.createElement("response");
			responseX.appendChild(doc.createTextNode("SUCCESS"));
			autnresponse.appendChild(responseX);
			
			String numhits= response.getTagValue("autn:numhits", "");
			Element responsedata = doc.createElement("responsedata");
			autnresponse.appendChild(responsedata);
			
			Element nhits = doc.createElement("numhits");
			nhits.appendChild(doc.createTextNode(numhits));
			responsedata.appendChild(nhits);
			
			
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			while(hits != null)
			{
				Element hit = doc.createElement("hit");
				responsedata.appendChild(hit);
				String reference = null;
				String score = null;
				String idolDb = null;
				String title = null;
				
				reference = hits.getTagValue("autn:reference","");
				score = hits.getTagValue("autn:weight","");
				idolDb = hits.getTagValue("autn:database","");
				title = hits.getTagValue("autn:title","");
				
				Element ref = doc.createElement("reference");
				ref.appendChild(doc.createTextNode(reference));
				hit.appendChild(ref);
				
				Element id = doc.createElement("id");
				id.appendChild(doc.createTextNode(hits.getTagValue("autn:id","")));
				hit.appendChild(id);
				
				Element section = doc.createElement("section");
				section.appendChild(doc.createTextNode(hits.getTagValue("autn:section","")));
				hit.appendChild(section);
				
				Element rank = doc.createElement("weight");
				rank.appendChild(doc.createTextNode(score));
				hit.appendChild(rank);
				
				Element dbA = doc.createElement("database");
				dbA.appendChild(doc.createTextNode(idolDb));
				hit.appendChild(dbA);
				
				Element tit = doc.createElement("title");
				tit.appendChild(doc.createTextNode(title));
				hit.appendChild(tit);
				
				Element content = doc.createElement("content");
				hit.appendChild(content);
				
				Element documen = doc.createElement("DOCUMENT");
				content.appendChild(documen);
				
				Element drereference = doc.createElement("DREREFERENCE");
				drereference.appendChild(doc.createTextNode(reference));
				documen.appendChild(drereference);
				
				Element dretitle = doc.createElement("DRETITLE");
				dretitle.appendChild(doc.createTextNode(title));
				documen.appendChild(dretitle);
				
				if(dbS.equals("IntFissoConsumer"))
				{
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codinterazione = doc.createElement("COD_INTERAZIONE");
					codinterazione.appendChild(doc.createTextNode(hits.getTagValue("COD_INTERAZIONE","")));
					documen.appendChild(codinterazione);
					Element specificaTripletta = doc.createElement("SPECIFICA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("STATO");
					stato.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("DATA_CREAZIONE");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(datacreazione);
					Element tipocanale = doc.createElement("TIPO_CANALE");
					tipocanale.appendChild(doc.createTextNode(hits.getTagValue("TIPO_CANALE","")));
					documen.appendChild(tipocanale);
					Element direzione = doc.createElement("DIREZIONE");
					direzione.appendChild(doc.createTextNode(hits.getTagValue("DIREZIONE","")));
					documen.appendChild(direzione);
					Element codcliente = doc.createElement("COD_CLIENTE");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("COD_CLIENTE","")));
					documen.appendChild(codcliente);
					Element crm = doc.createElement("CRM_NATIVO");
					crm.appendChild(doc.createTextNode(hits.getTagValue("CRM_NATIVO","")));
					documen.appendChild(crm);
					Element conclusioni = doc.createElement("CONCLUSIONI");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("SUBCONCLUSIONI");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(subconclusioni);
					Element codcase = doc.createElement("COD_CASE");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codcase);
					Element segmento = doc.createElement("SEGMENTO");
					segmento.appendChild(doc.createTextNode(hits.getTagValue("SEGMENTO","")));
					documen.appendChild(segmento);
					Element st = doc.createElement("SERVICE_TEAM");
					st.appendChild(doc.createTextNode(hits.getTagValue("SERVICE_TEAM","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element teamcreazione = doc.createElement("TEAM_INBOX_CREAZ");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZ","")));
					documen.appendChild(teamcreazione);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
					
				}
				if(dbS.equals("CaseFissoConsumer"))
				{
					Element codcase = doc.createElement("COD_CASE");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codcase);
					Element specificaTripletta = doc.createElement("SPECIFICA_TRIPLETTA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA_TRIPLETTA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO_TRIPLETTA");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO_TRIPLETTA","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO_TRIPLETTA");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO_TRIPLETTA","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("STATO");
					stato.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("DATA_CREAZIONE");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(datacreazione);
					Element wtt = doc.createElement("FLAG_ASSOCIATO_WTT");
					wtt.appendChild(doc.createTextNode(hits.getTagValue("FLAG_ASSOCIATO_WTT","")));
					documen.appendChild(wtt);
					Element ratt = doc.createElement("FLAG_ASSOCIATO_RATT");
					ratt.appendChild(doc.createTextNode(hits.getTagValue("FLAG_ASSOCIATO_RATT","")));
					documen.appendChild(ratt);
					Element teamdest = doc.createElement("TEAM_INBOX_DEST");
					teamdest.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_DEST","")));
					documen.appendChild(teamdest);
					Element conclusioni = doc.createElement("CONCLUSIONI");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("SUBCONCLUSIONI");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(subconclusioni);
					Element descproblema = doc.createElement("DESCRIZIONE_PROBLEMA");
					descproblema.appendChild(doc.createTextNode(hits.getTagValue("DESCRIZIONE_PROBLEMA","")));
					documen.appendChild(descproblema);
					Element teamchiusura = doc.createElement("TEAM_INBOX_CHIUSURA");
					teamchiusura.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CHIUSURA","")));
					documen.appendChild(teamchiusura);
					Element datachiusura = doc.createElement("DATA_CHIUSURA");
					datachiusura.appendChild(doc.createTextNode(hits.getTagValue("DATA_CHIUSURA","")));
					documen.appendChild(datachiusura);
					Element teamcreazione = doc.createElement("TEAM_INBOX_CREAZIONE");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZIONE","")));
					documen.appendChild(teamcreazione);
					Element st = doc.createElement("ST_CODIF");
					st.appendChild(doc.createTextNode(hits.getTagValue("ST_CODIF","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codcliente = doc.createElement("CODICE_CLIENTE");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("CODICE_CLIENTE","")));
					documen.appendChild(codcliente);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
					
				}
				if(dbS.equals("IntMobileConsumer"))
				{
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codinterazione = doc.createElement("COD_INTERAZIONE");
					codinterazione.appendChild(doc.createTextNode(hits.getTagValue("COD_INTERAZIONE","")));
					documen.appendChild(codinterazione);
					Element specificaTripletta = doc.createElement("SPECIFICA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("STATO");
					stato.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("DATA_CREAZIONE");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(datacreazione);
					Element tipocanale = doc.createElement("TIPO_CANALE");
					tipocanale.appendChild(doc.createTextNode(hits.getTagValue("TIPO_CANALE","")));
					documen.appendChild(tipocanale);
					Element direzione = doc.createElement("DIREZIONE");
					direzione.appendChild(doc.createTextNode(hits.getTagValue("DIREZIONE","")));
					documen.appendChild(direzione);
					Element codcliente = doc.createElement("COD_CLIENTE");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("COD_CLIENTE","")));
					documen.appendChild(codcliente);
					Element crm = doc.createElement("CRM_NATIVO");
					crm.appendChild(doc.createTextNode(hits.getTagValue("CRM_NATIVO","")));
					documen.appendChild(crm);
					Element conclusioni = doc.createElement("CONCLUSIONI");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("SUBCONCLUSIONI");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(subconclusioni);
					Element codcase = doc.createElement("COD_CASE");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codcase);
					Element segmento = doc.createElement("SEGMENTO");
					segmento.appendChild(doc.createTextNode(hits.getTagValue("SEGMENTO","")));
					documen.appendChild(segmento);
					Element st = doc.createElement("SERVICE_TEAM");
					st.appendChild(doc.createTextNode(hits.getTagValue("SERVICE_TEAM","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element teamcreazione = doc.createElement("TEAM_INBOX_CREAZ");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZ","")));
					documen.appendChild(teamcreazione);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
				}
				if(dbS.equals("CaseMobileConsumer"))
				{
					Element codcase = doc.createElement("COD_CASE");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codcase);
					Element specificaTripletta = doc.createElement("SPECIFICA_TRIPLETTA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA_TRIPLETTA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO_TRIPLETTA");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO_TRIPLETTA","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO_TRIPLETTA");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO_TRIPLETTA","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("STATO");
					stato.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("DATA_CREAZIONE");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(datacreazione);
					Element wtt = doc.createElement("FLAG_ASSOCIATO_WTT");
					wtt.appendChild(doc.createTextNode(hits.getTagValue("FLAG_ASSOCIATO_WTT","")));
					documen.appendChild(wtt);
					Element ratt = doc.createElement("FLAG_ASSOCIATO_RATT");
					ratt.appendChild(doc.createTextNode(hits.getTagValue("FLAG_ASSOCIATO_RATT","")));
					documen.appendChild(ratt);
					Element teamdest = doc.createElement("TEAM_INBOX_DEST");
					teamdest.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_DEST","")));
					documen.appendChild(teamdest);
					Element conclusioni = doc.createElement("CONCLUSIONI");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("SUBCONCLUSIONI");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(subconclusioni);
					Element descproblema = doc.createElement("DESCRIZIONE_PROBLEMA");
					descproblema.appendChild(doc.createTextNode(hits.getTagValue("DESCRIZIONE_PROBLEMA","")));
					documen.appendChild(descproblema);
					Element teamchiusura = doc.createElement("TEAM_INBOX_CHIUSURA");
					teamchiusura.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CHIUSURA","")));
					documen.appendChild(teamchiusura);
					Element datachiusura = doc.createElement("DATA_CHIUSURA");
					datachiusura.appendChild(doc.createTextNode(hits.getTagValue("DATA_CHIUSURA","")));
					documen.appendChild(datachiusura);
					Element teamcreazione = doc.createElement("TEAM_INBOX_CREAZIONE");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZIONE","")));
					documen.appendChild(teamcreazione);
					Element st = doc.createElement("ST_CODIF");
					st.appendChild(doc.createTextNode(hits.getTagValue("ST_CODIF","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codcliente = doc.createElement("CODICE_CLIENTE");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("CODICE_CLIENTE","")));
					documen.appendChild(codcliente);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
				}
				if(dbS.equals("IntFissoCorporate"))
				{
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codinterazione = doc.createElement("COD_INTERAZIONE");
					codinterazione.appendChild(doc.createTextNode(hits.getTagValue("COD_INTERAZIONE","")));
					documen.appendChild(codinterazione);
					Element specificaTripletta = doc.createElement("SPECIFICA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("STATO");
					stato.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("DATA_CREAZIONE");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(datacreazione);
					Element tipocanale = doc.createElement("TIPO_CANALE");
					tipocanale.appendChild(doc.createTextNode(hits.getTagValue("TIPO_CANALE","")));
					documen.appendChild(tipocanale);
					Element direzione = doc.createElement("DIREZIONE");
					direzione.appendChild(doc.createTextNode(hits.getTagValue("DIREZIONE","")));
					documen.appendChild(direzione);
					Element codcliente = doc.createElement("COD_CLIENTE");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("COD_CLIENTE","")));
					documen.appendChild(codcliente);
					Element crm = doc.createElement("CRM_NATIVO");
					crm.appendChild(doc.createTextNode(hits.getTagValue("CRM_NATIVO","")));
					documen.appendChild(crm);
					Element conclusioni = doc.createElement("CONCLUSIONI");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("SUBCONCLUSIONI");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(subconclusioni);
					Element codcase = doc.createElement("COD_CASE");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codcase);
					Element segmento = doc.createElement("SEGMENTO");
					segmento.appendChild(doc.createTextNode(hits.getTagValue("SEGMENTO","")));
					documen.appendChild(segmento);
					Element st = doc.createElement("SERVICE_TEAM");
					st.appendChild(doc.createTextNode(hits.getTagValue("SERVICE_TEAM","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element teamcreazione = doc.createElement("TEAM_INBOX_CREAZ");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZ","")));
					documen.appendChild(teamcreazione);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
				}
				if(dbS.equals("CaseFissoCorporate"))
				{
					Element codcase = doc.createElement("COD_CASE");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codcase);
					Element specificaTripletta = doc.createElement("SPECIFICA_TRIPLETTA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA_TRIPLETTA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO_TRIPLETTA");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO_TRIPLETTA","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO_TRIPLETTA");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO_TRIPLETTA","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("STATO");
					stato.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("DATA_CREAZIONE");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(datacreazione);
					Element wtt = doc.createElement("FLAG_ASSOCIATO_WTT");
					wtt.appendChild(doc.createTextNode(hits.getTagValue("FLAG_ASSOCIATO_WTT","")));
					documen.appendChild(wtt);
					Element ratt = doc.createElement("FLAG_ASSOCIATO_RATT");
					ratt.appendChild(doc.createTextNode(hits.getTagValue("FLAG_ASSOCIATO_RATT","")));
					documen.appendChild(ratt);
					Element teamdest = doc.createElement("TEAM_INBOX_DEST");
					teamdest.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_DEST","")));
					documen.appendChild(teamdest);
					Element conclusioni = doc.createElement("CONCLUSIONI");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("SUBCONCLUSIONI");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(subconclusioni);
					Element descproblema = doc.createElement("DESCRIZIONE_PROBLEMA");
					descproblema.appendChild(doc.createTextNode(hits.getTagValue("DESCRIZIONE_PROBLEMA","")));
					documen.appendChild(descproblema);
					Element teamchiusura = doc.createElement("TEAM_INBOX_CHIUSURA");
					teamchiusura.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CHIUSURA","")));
					documen.appendChild(teamchiusura);
					Element datachiusura = doc.createElement("DATA_CHIUSURA");
					datachiusura.appendChild(doc.createTextNode(hits.getTagValue("DATA_CHIUSURA","")));
					documen.appendChild(datachiusura);
					Element teamcreazione = doc.createElement("TEAM_INBOX_CREAZIONE");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZIONE","")));
					documen.appendChild(teamcreazione);
					Element st = doc.createElement("ST_CODIF");
					st.appendChild(doc.createTextNode(hits.getTagValue("ST_CODIF","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codcliente = doc.createElement("CODICE_CLIENTE");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("CODICE_CLIENTE","")));
					documen.appendChild(codcliente);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
				}
				if(dbS.equals("IntMobileCorporate"))
				{
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codinterazione = doc.createElement("COD_INTERAZIONE");
					codinterazione.appendChild(doc.createTextNode(hits.getTagValue("COD_INTERAZIONE","")));
					documen.appendChild(codinterazione);
					Element specificaTripletta = doc.createElement("SPECIFICA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("STATO");
					stato.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("DATA_CREAZIONE");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(datacreazione);
					Element tipocanale = doc.createElement("TIPO_CANALE");
					tipocanale.appendChild(doc.createTextNode(hits.getTagValue("TIPO_CANALE","")));
					documen.appendChild(tipocanale);
					Element direzione = doc.createElement("DIREZIONE");
					direzione.appendChild(doc.createTextNode(hits.getTagValue("DIREZIONE","")));
					documen.appendChild(direzione);
					Element codcliente = doc.createElement("COD_CLIENTE");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("COD_CLIENTE","")));
					documen.appendChild(codcliente);
					Element crm = doc.createElement("CRM_NATIVO");
					crm.appendChild(doc.createTextNode(hits.getTagValue("CRM_NATIVO","")));
					documen.appendChild(crm);
					Element conclusioni = doc.createElement("CONCLUSIONI");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("SUBCONCLUSIONI");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(subconclusioni);
					Element codcase = doc.createElement("COD_CASE");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codcase);
					Element segmento = doc.createElement("SEGMENTO");
					segmento.appendChild(doc.createTextNode(hits.getTagValue("SEGMENTO","")));
					documen.appendChild(segmento);
					Element st = doc.createElement("SERVICE_TEAM");
					st.appendChild(doc.createTextNode(hits.getTagValue("SERVICE_TEAM","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element teamcreazione = doc.createElement("TEAM_INBOX_CREAZ");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZ","")));
					documen.appendChild(teamcreazione);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
				}
				if(dbS.equals("CaseMobileCorporate"))
				{
					Element codcase = doc.createElement("COD_CASE");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codcase);
					Element specificaTripletta = doc.createElement("SPECIFICA_TRIPLETTA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA_TRIPLETTA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO_TRIPLETTA");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO_TRIPLETTA","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO_TRIPLETTA");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO_TRIPLETTA","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("STATO");
					stato.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("DATA_CREAZIONE");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(datacreazione);
					Element wtt = doc.createElement("FLAG_ASSOCIATO_WTT");
					wtt.appendChild(doc.createTextNode(hits.getTagValue("FLAG_ASSOCIATO_WTT","")));
					documen.appendChild(wtt);
					Element ratt = doc.createElement("FLAG_ASSOCIATO_RATT");
					ratt.appendChild(doc.createTextNode(hits.getTagValue("FLAG_ASSOCIATO_RATT","")));
					documen.appendChild(ratt);
					Element teamdest = doc.createElement("TEAM_INBOX_DEST");
					teamdest.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_DEST","")));
					documen.appendChild(teamdest);
					Element conclusioni = doc.createElement("CONCLUSIONI");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("SUBCONCLUSIONI");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(subconclusioni);
					Element descproblema = doc.createElement("DESCRIZIONE_PROBLEMA");
					descproblema.appendChild(doc.createTextNode(hits.getTagValue("DESCRIZIONE_PROBLEMA","")));
					documen.appendChild(descproblema);
					Element teamchiusura = doc.createElement("TEAM_INBOX_CHIUSURA");
					teamchiusura.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CHIUSURA","")));
					documen.appendChild(teamchiusura);
					Element datachiusura = doc.createElement("DATA_CHIUSURA");
					datachiusura.appendChild(doc.createTextNode(hits.getTagValue("DATA_CHIUSURA","")));
					documen.appendChild(datachiusura);
					Element teamcreazione = doc.createElement("TEAM_INBOX_CREAZIONE");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZIONE","")));
					documen.appendChild(teamcreazione);
					Element st = doc.createElement("ST_CODIF");
					st.appendChild(doc.createTextNode(hits.getTagValue("ST_CODIF","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codcliente = doc.createElement("CODICE_CLIENTE");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("CODICE_CLIENTE","")));
					documen.appendChild(codcliente);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
				}
				
				hits = hits.next("autn:hit");
			}
			TransformerFactory tranFactory = TransformerFactory.newInstance();   
		    Transformer aTransformer = tranFactory.newTransformer();   
		    Source src = new DOMSource(doc);   
		    Result dest = new StreamResult( new File(path) );   
		    aTransformer.transform(src, dest);
		    System.out.println("FINE operazioni build xml");
		    return "0";// "Operazione terminata con successo";
		}
		else
			return "1";// "Si sono vericati errori. Operazioni non completata";
	}
	
	//public String BuildXMLStructCorporate(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	public String BuildXMLStructCorporate(String root, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	{
		
		String dbS = null;
		System.out.println("dentro a XML Builder");
		
		if(tipo.equals("INTERAZIONI"))
			dbS = "CorporateInt";
		else
			dbS = "CorporateCase";
					
			
		
		//System.out.println("Valori: " + valori);
		System.out.println("Testo: " + testo);
		
		
		
		//if(dbS.equals("CaseMobileConsumer")||dbS.equals("CaseFissoConsumer"))
		//	connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		//else
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		/*if(dbS.equals("IntMobileConsumer") && testo.equalsIgnoreCase("Shaping"))
		 aciAction.setParameter(new ActionParameter("minscore", "75"));
		else*/
		/*if(relevance.equalsIgnoreCase("50")||relevance.equalsIgnoreCase("60"))
			aciAction.setParameter(new ActionParameter("minscore", "70"));
		else*/
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		//aciAction.setParameter(new ActionParameter("maxresults", "100"));
		
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		//aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("Print", "Fields"));
		if(dbS.equalsIgnoreCase("CorporateCase"))
			aciAction.setParameter(new ActionParameter("PrintFields", "DRETITLE,DREREFERENCE,DATA_IMPORT,COD_CASE,SPECIFICA_TRIPLETTA,MOTIVO_TRIPLETTA,ARGOMENTO_TRIPLETTA,CONCLUSIONI,SUBCONCLUSIONI,STATO,DATA_CREAZIONE,DATA_CHIUSURA,TEAM_INBOX_CREAZIONE,COD_CLIENTE,PARTITA_IVA,SERVICE_TEAM,UFFICIO,TIPOLOGIA_CLIENTE,TEAM_INBOX_DESTINAZIONE,TEAM_INBOX_CHIUSURA,SEGMENTO,DRECONTENT,DREDBNAME"));
		else
			aciAction.setParameter(new ActionParameter("PrintFields", "DRETITLE,DREREFERENCE,DATA_IMPORT,COD_CASE,COD_INTERAZIONE,SPECIFICA_TRIPLETTA,MOTIVO_TRIPLETTA,ARGOMENTO_TRIPLETTA,DATA_CREAZIONE,TIPO_CANALE,DIREZIONE,COD_CLIENTE,TIPOLOGIA_CLIENTE,STATO,UFFICIO,SERVICE_TEAM,CONCLUSIONI,SUBCONCLUSIONI,SEGMENTO,DRECONTENT,DREDBNAME"));
		
				
		AciResponse response = connection.aciActionExecute(aciAction);

		
		if(response.checkForSuccess())
		{
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			String nome = ("corporate_"+tipo+"_"+username+"_"+nomeQuery).toLowerCase();
			String path = PropertiesManager.getMyProperty("pathXMLStruct")+nome+".xml";
			System.out.println("path file XML: " + path);
			Element autnresponse = doc.createElement("autnresponse");
			doc.appendChild(autnresponse);
			
			Element action = doc.createElement("action");
			action.appendChild(doc.createTextNode("QUERY"));
			autnresponse.appendChild(action);
			
			Element responseX = doc.createElement("response");
			responseX.appendChild(doc.createTextNode("SUCCESS"));
			autnresponse.appendChild(responseX);
			
			String numhits= response.getTagValue("autn:numhits", "");
			Element responsedata = doc.createElement("responsedata");
			autnresponse.appendChild(responsedata);
			
			Element nhits = doc.createElement("numhits");
			nhits.appendChild(doc.createTextNode(numhits));
			responsedata.appendChild(nhits);
			
			
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			while(hits != null)
			{
				Element hit = doc.createElement("hit");
				responsedata.appendChild(hit);
				String reference = null;
				String score = null;
				String idolDb = null;
				String title = null;
				
				reference = hits.getTagValue("autn:reference","");
				score = hits.getTagValue("autn:weight","");
				idolDb = hits.getTagValue("autn:database","");
				title = hits.getTagValue("autn:title","");
				
				Element ref = doc.createElement("reference");
				ref.appendChild(doc.createTextNode(reference));
				hit.appendChild(ref);
				
				Element id = doc.createElement("id");
				id.appendChild(doc.createTextNode(hits.getTagValue("autn:id","")));
				hit.appendChild(id);
				
				Element section = doc.createElement("section");
				section.appendChild(doc.createTextNode(hits.getTagValue("autn:section","")));
				hit.appendChild(section);
				
				Element rank = doc.createElement("weight");
				rank.appendChild(doc.createTextNode(score));
				hit.appendChild(rank);
				
				Element dbA = doc.createElement("database");
				dbA.appendChild(doc.createTextNode(idolDb));
				hit.appendChild(dbA);
				
				Element tit = doc.createElement("title");
				tit.appendChild(doc.createTextNode(title));
				hit.appendChild(tit);
				
				Element content = doc.createElement("content");
				hit.appendChild(content);
				
				Element documen = doc.createElement("DOCUMENT");
				content.appendChild(documen);
				
				Element drereference = doc.createElement("DREREFERENCE");
				drereference.appendChild(doc.createTextNode(reference));
				documen.appendChild(drereference);
				
				Element dretitle = doc.createElement("DRETITLE");
				dretitle.appendChild(doc.createTextNode(title));
				documen.appendChild(dretitle);
				
				if(dbS.equals("CorporateCase"))
				{
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codinterazione = doc.createElement("COD_CASE");
					codinterazione.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codinterazione);
					Element specificaTripletta = doc.createElement("SPECIFICA_TRIPLETTA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA_TRIPLETTA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO_TRIPLETTA");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO_TRIPLETTA","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO_TRIPLETTA");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO_TRIPLETTA","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("CONCLUSIONI");
					stato.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("SUBCONCLUSIONI");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(datacreazione);
					Element tipocanale = doc.createElement("STATO");
					tipocanale.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(tipocanale);
					Element direzione = doc.createElement("DATA_CREAZIONE");
					direzione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(direzione);
					Element codcliente = doc.createElement("DATA_CHIUSURA");
					codcliente.appendChild(doc.createTextNode(hits.getTagValue("DATA_CHIUSURA","")));
					documen.appendChild(codcliente);
					Element crm = doc.createElement("TEAM_INBOX_CREAZIONE");
					crm.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CREAZIONE","")));
					documen.appendChild(crm);
					Element conclusioni = doc.createElement("COD_CLIENTE");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("COD_CLIENTE","")));
					documen.appendChild(conclusioni);
					Element subconclusioni = doc.createElement("PARTITA_IVA");
					subconclusioni.appendChild(doc.createTextNode(hits.getTagValue("PARTITA_IVA","")));
					documen.appendChild(subconclusioni);
					Element codcase = doc.createElement("SERVICE_TEAM");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("SERVICE_TEAM","")));
					documen.appendChild(codcase);
					Element segmento = doc.createElement("SEGMENTO");
					segmento.appendChild(doc.createTextNode(hits.getTagValue("SEGMENTO","")));
					documen.appendChild(segmento);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element teamcreazione = doc.createElement("UFFICIO");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("UFFICIO","")));
					documen.appendChild(teamcreazione);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element tipCli = doc.createElement("TIPOLOGIA_CLIENTE");
					tipCli.appendChild(doc.createTextNode(hits.getTagValue("TIPOLOGIA_CLIENTE")));
					documen.appendChild(tipCli);
					Element teamInbDest = doc.createElement("TEAM_INBOX_DESTINAZIONE");
					teamInbDest.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_DESTINAZIONE")));
					documen.appendChild(teamInbDest);
					Element teamInbChius = doc.createElement("TEAM_INBOX_CHIUSURA");
					teamInbChius.appendChild(doc.createTextNode(hits.getTagValue("TEAM_INBOX_CHIUSURA")));
					documen.appendChild(teamInbChius);
					Element seg = doc.createElement("SEGMENTO");
					seg.appendChild(doc.createTextNode(hits.getTagValue("SEGMENTO")));
					documen.appendChild(seg);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
				}
				else
				{	
					
					Element dredbname = doc.createElement("DREDBNAME");
					dredbname.appendChild(doc.createTextNode(idolDb));
					documen.appendChild(dredbname);
					Element codicase = doc.createElement("COD_CASE");
					codicase.appendChild(doc.createTextNode(hits.getTagValue("COD_CASE","")));
					documen.appendChild(codicase);
					Element codinterazione = doc.createElement("COD_INTERAZIONE");
					codinterazione.appendChild(doc.createTextNode(hits.getTagValue("COD_INTERAZIONE","")));
					documen.appendChild(codinterazione);
					Element specificaTripletta = doc.createElement("SPECIFICA_TRIPLETTA");
					specificaTripletta.appendChild(doc.createTextNode(hits.getTagValue("SPECIFICA_TRIPLETTA","")));
					documen.appendChild(specificaTripletta);
					Element motivoTripletta = doc.createElement("MOTIVO_TRIPLETTA");
					motivoTripletta.appendChild(doc.createTextNode(hits.getTagValue("MOTIVO_TRIPLETTA","")));
					documen.appendChild(motivoTripletta);
					Element argomentoTripletta = doc.createElement("ARGOMENTO_TRIPLETTA");
					argomentoTripletta.appendChild(doc.createTextNode(hits.getTagValue("ARGOMENTO_TRIPLETTA","")));
					documen.appendChild(argomentoTripletta);
					Element stato = doc.createElement("CONCLUSIONI");
					stato.appendChild(doc.createTextNode(hits.getTagValue("CONCLUSIONI","")));
					documen.appendChild(stato);
					Element datacreazione = doc.createElement("SUBCONCLUSIONI");
					datacreazione.appendChild(doc.createTextNode(hits.getTagValue("SUBCONCLUSIONI","")));
					documen.appendChild(datacreazione);
					Element tipocanale = doc.createElement("STATO");
					tipocanale.appendChild(doc.createTextNode(hits.getTagValue("STATO","")));
					documen.appendChild(tipocanale);
					Element direzione = doc.createElement("DATA_CREAZIONE");
					direzione.appendChild(doc.createTextNode(hits.getTagValue("DATA_CREAZIONE","")));
					documen.appendChild(direzione);
					Element conclusioni = doc.createElement("COD_CLIENTE");
					conclusioni.appendChild(doc.createTextNode(hits.getTagValue("COD_CLIENTE","")));
					documen.appendChild(conclusioni);
					Element codcase = doc.createElement("SERVICE_TEAM");
					codcase.appendChild(doc.createTextNode(hits.getTagValue("SERVICE_TEAM","")));
					documen.appendChild(codcase);
					Element segmento = doc.createElement("DIREZIONE");
					segmento.appendChild(doc.createTextNode(hits.getTagValue("DIREZIONE","")));
					documen.appendChild(segmento);
					Element st = doc.createElement("TIPO_CANALE");
					st.appendChild(doc.createTextNode(hits.getTagValue("TIPO_CANALE","")));
					documen.appendChild(st);
					Element dataimport = doc.createElement("DATA_IMPORT");
					dataimport.appendChild(doc.createTextNode(hits.getTagValue("DATA_IMPORT","")));
					documen.appendChild(dataimport);
					Element teamcreazione = doc.createElement("UFFICIO");
					teamcreazione.appendChild(doc.createTextNode(hits.getTagValue("UFFICIO","")));
					documen.appendChild(teamcreazione);
					Element rel = doc.createElement("RELEVANCE");
					rel.appendChild(doc.createTextNode(score));
					documen.appendChild(rel);
					Element tipCli = doc.createElement("TIPOLOGIA_CLIENTE");
					tipCli.appendChild(doc.createTextNode(hits.getTagValue("TIPOLOGIA_CLIENTE")));
					documen.appendChild(tipCli);
					Element seg = doc.createElement("SEGMENTO");
					seg.appendChild(doc.createTextNode(hits.getTagValue("SEGMENTO")));
					documen.appendChild(seg);
					Element drecontent = doc.createElement("DRECONTENT");
					drecontent.appendChild(doc.createTextNode(hits.getTagValue("DRECONTENT")));
					documen.appendChild(drecontent);
				}
			
				hits = hits.next("autn:hit");
			}
			TransformerFactory tranFactory = TransformerFactory.newInstance();   
		    Transformer aTransformer = tranFactory.newTransformer();   
		    Source src = new DOMSource(doc);   
		    Result dest = new StreamResult( new File(path) );   
		    aTransformer.transform(src, dest);
		    System.out.println("FINE operazioni build xml");
		    return "0";// "Operazione terminata con successo";
		}
		else
			return "1";// "Si sono vericati errori. Operazioni non completata";
	}
	
	public String BuildXMLStructTestLocale(String root, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	{
		
		String dbS = null;
		System.out.println("dentro a XML Builder");
		if(tipo.equals("INTERAZIONI"))
			dbS = "CorporateInt";
		else
			dbS = "CorporateCase";
		
		
		System.out.println("Testo: " + testo);
		
		
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		/*if(dbS.equals("IntMobileConsumer") && testo.equalsIgnoreCase("Shaping"))
		 aciAction.setParameter(new ActionParameter("minscore", "75"));
		else*/
		/*if(relevance.equalsIgnoreCase("50")||relevance.equalsIgnoreCase("60"))
			aciAction.setParameter(new ActionParameter("minscore", "70"));
		else*/
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		//aciAction.setParameter(new ActionParameter("maxresults", "100"));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		//aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("Print", "Fields"));
		if(dbS.equalsIgnoreCase("CorporateCase"))
			aciAction.setParameter(new ActionParameter("PrintFields", "DRETITLE,DREREFERENCE,DATA_IMPORT,COD_CASE,SPECIFICA_TRIPLETTA,MOTIVO_TRIPLETTA,ARGOMENTO_TRIPLETTA,CONCLUSIONI,SUB_CONCLUSIONI,STATO,DATA_CREAZIONE,DATA_CHIUSURA,TEAM_INBOX_CREAZIONE,COD_CLIENTE,PARTITA_IVA,SERVICE_TEAM,UFFICIO,TIPOLOGIA_CLIENTE,DRECONTENT,DREDBNAME"));
		else
			aciAction.setParameter(new ActionParameter("PrintFields", "DRETITLE,DREREFERENCE,DATA_IMPORT,COD_INTERAZIONE,SPECIFICA_TRIPLETTA,MOTIVO_TRIPLETTA,ARGOMENTO_TRIPLETTA,DATA_CREAZIONE,TIPO_CANALE,DIREZIONE,COD_CLIENTE,TIPOLOGIA_CLIENTE,STATO,UFFICIO,SERVICE_TEAM,CONCLUSIONI,SUB_CONCLUSIONI,DRECONTENT,DREDBNAME"));
		
				
		AciResponse response = connection.aciActionExecute(aciAction);

		
		if(response.checkForSuccess())
		{
			
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			while(hits != null)
			{
				Calendar da = GregorianCalendar.getInstance();
				SimpleDateFormat daFormat = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS", Locale.ITALY);
				String dat = daFormat.format(da.getTime());
				System.out.println("Data: " + dat);
				hits = hits.next("autn:hit");
			}
			System.out.println("fine ciclo risultati");
					    return "0";// "Operazione terminata con successo";
		}
		else
			return "1";// "Si sono vericati errori. Operazioni non completata";
	}
	
	public String BuildXMLStructTest(String root, String ticket, String tipo, HashMap<String, Object> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	{
		
		String dbS = null;
		System.out.println("dentro a XML Builder");
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoConsumer";
				else
					dbS = "CaseFissoConsumer";
					
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileConsumer";
				else
					dbS = "CaseMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
//		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			Object valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(valoreCorrente instanceof String){
				if(!valoreCorrente.equals("--"))
				{
/*					if(i > 0)
						valori = valori + "+AND+";
*/					
					valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente + "+AND+";
//					i++;
				}
			}else if(valoreCorrente instanceof String[]){
				String[] values = (String[])valoreCorrente;
				String testNull = values[0] + values[1];
				if(testNull.trim().length()>2){
					valori = valori + "RANGE{"+values[0]+","+values[1]+ "}:" + chiaveCorrente + "+AND+";
				}
			}
		}
		if(valori.endsWith("+AND+")){
			int i = valori.lastIndexOf("+AND+");
			valori = valori.substring(0,i);
		}
		System.out.println("Valori: " + valori);
		System.out.println("Testo: " + testo);
		
		
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		/*if(dbS.equals("IntMobileConsumer") && testo.equalsIgnoreCase("Shaping"))
		 aciAction.setParameter(new ActionParameter("minscore", "75"));
		else*/
		/*if(relevance.equalsIgnoreCase("50")||relevance.equalsIgnoreCase("60"))
			aciAction.setParameter(new ActionParameter("minscore", "70"));
		else*/
		aciAction.setParameter(new ActionParameter("minscore", relevance));
		
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		//aciAction.setParameter(new ActionParameter("maxresults", "100"));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		//aciAction.setParameter(new ActionParameter("Print", "all"));
		aciAction.setParameter(new ActionParameter("Print", "Fields"));
		if(dbS.equalsIgnoreCase("CorporateCase"))
			aciAction.setParameter(new ActionParameter("PrintFields", "DRETITLE,DREREFERENCE,DATA_IMPORT,COD_CASE,SPECIFICA_TRIPLETTA,MOTIVO_TRIPLETTA,ARGOMENTO_TRIPLETTA,CONCLUSIONI,SUB_CONCLUSIONI,STATO,DATA_CREAZIONE,DATA_CHIUSURA,TEAM_INBOX_CREAZIONE,COD_CLIENTE,PARTITA_IVA,SERVICE_TEAM,UFFICIO,TIPOLOGIA_CLIENTE,DRECONTENT,DREDBNAME"));
		else
			aciAction.setParameter(new ActionParameter("PrintFields", "DRETITLE,DREREFERENCE,DATA_IMPORT,COD_INTERAZIONE,SPECIFICA_TRIPLETTA,MOTIVO_TRIPLETTA,ARGOMENTO_TRIPLETTA,DATA_CREAZIONE,TIPO_CANALE,DIREZIONE,COD_CLIENTE,TIPOLOGIA_CLIENTE,STATO,UFFICIO,SERVICE_TEAM,CONCLUSIONI,SUB_CONCLUSIONI,DRECONTENT,DREDBNAME"));
		
				
		AciResponse response = connection.aciActionExecute(aciAction);

		
		if(response.checkForSuccess())
		{
			
			AciResponse hits = response.findFirstOccurrence("autn:hit");
			while(hits != null)
			{
				Calendar da = GregorianCalendar.getInstance();
				SimpleDateFormat daFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ITALY);
				String dat = daFormat.format(da.getTime());
				System.out.println("Data: " + dat);
				hits = hits.next("autn:hit");
			}
			System.out.println("fine ciclo risultati");
					    return "0";// "Operazione terminata con successo";
		}
		else
			return "1";// "Si sono vericati errori. Operazioni non completata";
	}
	
	public String createXMLStruct(String root, String ticket, String tipo, HashMap<String, String> chiaveValore, String numRes, String relevance, String testo, String username, String nomeQuery) throws Exception
	{
		
		String dbS = null;
		if(root.equals("Consumer"))
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoConsumer";
				else
					dbS = "CaseFissoConsumer";
					
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileConsumer";
				else
					dbS = "CaseMobileConsumer";
			}
		}
		else
		{
			if(ticket.equals("FISSO"))
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntFissoCorporate";
				else
					dbS = "CaseFissoCorporate";
			
			}
			else
			{
				if(tipo.equals("INTERAZIONI"))
					dbS = "IntMobileCorporate";
				else
					dbS = "CaseMobileCorporate";
			}
		}
		
		
		Set<String> chiavi = chiaveValore.keySet();
		Iterator<String> iterChiavi = chiavi.iterator();
		String valori = "";
		int i = 0;
		while(iterChiavi.hasNext()){
			String chiaveCorrente = iterChiavi.next();
			String valoreCorrente = chiaveValore.get(chiaveCorrente);
			if(!valoreCorrente.equals("--"))
			{
				if(i > 0)
					valori = valori + "+AND+";
				
				valori = valori + "MATCH{"+valoreCorrente+"}:"+chiaveCorrente;
				i++;
			}
			
			
		}
		System.out.println("Valori: " + valori);
		
		AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
		connection.setCharacterEncoding(IDOLEncodings.UTF8);
		connection.setTimeout(600000);
		AciAction aciAction = new AciAction("Query");
		if(!testo.equals(""))
			aciAction.setParameter(new ActionParameter("text", testo));
		else
			aciAction.setParameter(new ActionParameter("text", "*"));
		
		if(dbS.equals("IntMobileConsumer") && testo.equalsIgnoreCase("Shaping"))
		 aciAction.setParameter(new ActionParameter("minscore", "75"));
		else
			aciAction.setParameter(new ActionParameter("minscore", relevance));
		
		aciAction.setParameter(new ActionParameter("maxresults", numRes));
		
		if(!valori.equals(""))
			aciAction.setParameter(new ActionParameter("FieldText", valori.trim()));
		
		aciAction.setParameter(new ActionParameter("DataBaseMatch", dbS));
		aciAction.setParameter(new ActionParameter("Print", "all"));
				
		AciResponse response = connection.aciActionExecute(aciAction);
		
		if(response.checkForSuccess())
		{
			String risultato = response.toString();
			String filexml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" + risultato;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		    DocumentBuilder builder;
		    String nome = (ticket+"_"+tipo+"_"+username+"_"+nomeQuery).toLowerCase();
	        String path = PropertiesManager.getMyProperty("pathXMLStruct")+nome+".xml";
	        builder = factory.newDocumentBuilder();   
	        Document document = builder.parse(new InputSource(new StringReader(filexml)));   
	  
	       TransformerFactory tranFactory = TransformerFactory.newInstance();   
	       Transformer aTransformer = tranFactory.newTransformer();   
	       Source src = new DOMSource(document);   
	       Result dest = new StreamResult( new File(path) );   
	       aTransformer.transform(src, dest);
			
			return "0";// "Operazione terminata con successo";
		}
		else
			return "1";// "Si sono vericati errori. Operazioni non completata";
	}
	
	public void createXML(AciResponse responseXML, String tipo, String ambito, String User)
	{
		String risultato = responseXML.toString();
		String filexml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" + risultato;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
	    DocumentBuilder builder;
	    
        try  
        {   
        	String nome = (tipo +"_"+ambito+"_"+User).toLowerCase();
        	String path = PropertiesManager.getMyProperty("pathXML")+nome+".xml";
        	System.out.println("path file XML: " + path);
        	builder = factory.newDocumentBuilder();   
  
            // Use String reader   
            Document document = builder.parse( new InputSource(   
                    new StringReader(filexml) ) );   
  
            
            TransformerFactory tranFactory = TransformerFactory.newInstance();   
            Transformer aTransformer = tranFactory.newTransformer();   
            Source src = new DOMSource(document);
            Result dest = new StreamResult( new File(path) );   
            aTransformer.transform(src, dest);   
        } catch (Exception e)   
        {   
            // TODO Auto-generated catch block   
            e.printStackTrace();   
        }
	}
	public ArrayList<DocumentoTO> categoryQuery(String ticket, String root, String ambito, String Id) throws AutonomyException
	{
		ArrayList<DocumentoTO> documents = null;
		
			try{
			
				AciConnection connection = null;
				if(ticket.equals("MOBILE_INTERAZIONI_CONSUMER"))	
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
				else
					connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			
				connection.setCharacterEncoding(IDOLEncodings.UTF8);
			connection.setTimeout(600000);
			AciAction aciAction = new AciAction("CategoryQuery");
			aciAction.setParameter(new ActionParameter("category", Id));
			aciAction.setParameter(new ActionParameter("params", "print,maxresults,databasematch,minscore,summary,Synonym"));
			
			System.out.println("prima di wind consumer");
			
			String db = null;
			if(root.toUpperCase().equals("WINDCONSUMER"))
			{
				System.out.println("dentro a wind consumer");
				if(ticket.equals("FISSO_INTERAZIONI_CONSUMER"))
				{
					db = "IntFissoConsumer";
					aciAction.setParameter(new ActionParameter("values", "all,10000,IntFissoConsumer,60,context,true"));
					System.out.println("dentro a FissoInterazioni");
				}
				if(ticket.equals("FISSO_CASE_CONSUMER"))
				{
					
					db = "CaseFissoConsumer";
					aciAction.setParameter(new ActionParameter("values", "all,10000,CaseFissoConsumer,60,context,true"));
					System.out.println("dentro a FissoCase");
				}
				if(ticket.equals("MOBILE_INTERAZIONI_CONSUMER"))
				{
					db = "IntMobileConsumer";
					aciAction.setParameter(new ActionParameter("values", "all,10000,IntMobileConsumer,60,context,true"));
					System.out.println("MobileInterazioni");
				}
				if(ticket.equals("MOBILE_CASE_CONSUMER"))
				{
					db = "CaseMobileConsumer";
					aciAction.setParameter(new ActionParameter("values", "all,10000,CaseMobileConsumer,60,context,true"));
					System.out.println("MobileCase");
				}
				if(ambito.equals("MobileSocialRisposte"))
				{
					db = "ConsumerSocialMobile";
					aciAction.setParameter(new ActionParameter("values", "all,10000,ConsumerSocialMobile,40,context,true"));
					System.out.println("MobileSocial");
				}
				if(ambito.equals("MobileSocialDomande"))
				{
					db = "ConsumerSocialMobile";
					aciAction.setParameter(new ActionParameter("values", "all,10000,ConsumerSocialMobile,40,context,true"));
					System.out.println("MobileSocial");
				}
				if(ambito.equals("FissoSocialDomande"))
				{
					db = "ConsumerSocialFisso";
					aciAction.setParameter(new ActionParameter("values", "all,10000,ConsumerSocialFisso,40,context,true"));
					System.out.println("FissoSocial");
				}
				if(ambito.equals("FissoSocialRisposte"))
				{
					db = "ConsumerSocialFisso";
					aciAction.setParameter(new ActionParameter("values", "all,10000,ConsumerSocialFisso,40,context,true"));
					System.out.println("FissoSocial");
				}
			}
			else
			{
				if(ambito.equals("FissoInterazioni"))
					aciAction.setParameter(new ActionParameter("values", "all,10000,IntFissoCorporate,60,context,true"));
				if(ambito.equals("FissoCase"))
					aciAction.setParameter(new ActionParameter("values", "all,10000,CaseFissoCorporate,60,context,true"));
				if(ambito.equals("MobileInterazioni"))
					aciAction.setParameter(new ActionParameter("values", "all,10000,IntMobileCorporate,60,context,true"));
				if(ambito.equals("MobileCase"))
					aciAction.setParameter(new ActionParameter("values", "all,10000,CaseMobileCorporate,60,context,true"));
			}
			//LOCALE
			/*if(root.equals("WindConsumer"))
			{
				if(ambito.equals("FissoInterazioni"))
					aciAction.setParameter(new ActionParameter("values", "all,1000,WindF,70,context"));
				if(ambito.equals("FissoCase"))
					aciAction.setParameter(new ActionParameter("values", "all,1000,WindF,70,context"));
				if(ambito.equals("MobileInterazioni"))
					aciAction.setParameter(new ActionParameter("values", "all,1000,WindT,70,context"));
				if(ambito.equals("MobileCase"))
					aciAction.setParameter(new ActionParameter("values", "all,1000,WindT,70,context"));
			}
			else
			{
				if(ambito.equals("FissoInterazioni"))
					aciAction.setParameter(new ActionParameter("values", "all,1000,WindF,70,context"));
				if(ambito.equals("FissoCase"))
					aciAction.setParameter(new ActionParameter("values", "all,1000,WindF,70,context"));
				if(ambito.equals("MobileInterazioni"))
					aciAction.setParameter(new ActionParameter("values", "all,1000,WindT,70,context"));
				if(ambito.equals("MobileCase"))
					aciAction.setParameter(new ActionParameter("values", "all,1000,WindT,70,context"));
			}*/
			AciResponse response = connection.aciActionExecute(aciAction);
			
			String numDoc = getNumTotaleDocumenti(db);
			if(response.checkForSuccess())
			{
				documents = new ArrayList<DocumentoTO>();
				DocumentoTO document = null;
				AciResponse hits = response.findFirstOccurrence("autn:hit");
				while(hits != null)
				{
					document = new DocumentoTO();
					
					document.setTotaleDocumenti(numDoc);
					document.setDataBase(hits.getTagValue("autn:database"));
					document.setReferenceDoc(hits.getTagValue("autn:reference"));
					document.setScore(hits.getTagValue("autn:weight"));
					document.setSummary(hits.getTagValue("DRECONTENT"));
					//document.setSummary(hits.getTagValue("autn:summary"));
					document.setTitleDoc(hits.getTagValue("autn:title"));
					
					documents.add(document);
					
					hits = hits.next();
				}
			}
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		return documents;
	}
	
	public ArrayList<CategoryTO> categoryHier(String root, String ambito, String specifica, String ds) throws AutonomyException
	{
		ArrayList<CategoryTO> categories = null;
		
			try{
			
				AciConnection connection = null;	
			if(ambito.equals("MOBILE") && specifica.equals("INTERAZIONI"))	
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			else
				connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.query"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			AciAction aciAction = new AciAction("CategoryGetHierDetails");
			aciAction.setParameter(new ActionParameter("expand", "all"));
			AciResponse response = connection.aciActionExecute(aciAction);
			if(response.checkForSuccess())
			{
				categories = new ArrayList<CategoryTO>();
				CategoryTO catPadre = null;
				CategoryTO catPadreSecond = null;
				CategoryTO catFiglio = null;
				CategoryTO catFiglioUno = null;
				AciResponse child = response.findFirstOccurrence("autn:child");
				while(child != null)
				{
					catPadre = new CategoryTO();
					if(child.getTagValue("autn:childname").toUpperCase().equals(root))
					{
						catPadre.setNome(child.getTagValue("autn:childname"));
						catPadre.setParent(null);
						catPadre.setId(child.getTagValue("autn:childid"));
						catPadre.setFoglia(false);
						categories.add(catPadre);
						int numChildren = Integer.parseInt(child.getTagValue("autn:numchildren"));
						
						if(numChildren > 0)
						{
							AciResponse children = child.findFirstEnclosedOccurrence("autn:children");
							if(children != null)
							{
								AciResponse childSecondL = children.findFirstOccurrence("autn:child");
								while(childSecondL != null)
								{
									
									if(ambito.equals("MOBILE") && specifica.equals("INTERAZIONI"))
									{
										if(childSecondL.getTagValue("autn:childname").equals("MobileInterazioni"))
										{
											catPadreSecond = new CategoryTO();
											catPadreSecond.setNome(childSecondL.getTagValue("autn:childname"));
											catPadreSecond.setParent(catPadre.getId());
											catPadreSecond.setId(childSecondL.getTagValue("autn:childid"));
											catPadreSecond.setFoglia(false);
											categories.add(catPadreSecond);
											
											int numChildrenSecond = Integer.parseInt(childSecondL.getTagValue("autn:numchildren"));
											
											
											
											if(numChildrenSecond > 0)
											{
												AciResponse childrenFoglie = childSecondL.findFirstEnclosedOccurrence("autn:children");
												if(childrenFoglie != null)
												{
													AciResponse childFoglie = childrenFoglie.findFirstOccurrence("autn:child");
													while(childFoglie != null)
													{
														catFiglio = new CategoryTO();
														catFiglio.setNome(childFoglie.getTagValue("autn:childname"));
														catFiglio.setParent(catPadreSecond.getId());
														catFiglio.setId(childFoglie.getTagValue("autn:childid"));
														catFiglio.setFoglia(false);
														categories.add(catFiglio);
														int numChildrenFoglia = Integer.parseInt(childFoglie.getTagValue("autn:numchildren"));
														
														if(numChildrenFoglia > 0)
														{
															AciResponse childrenFoglieUno = childFoglie.findFirstEnclosedOccurrence("autn:children");
															if(childrenFoglieUno != null)
															{
																AciResponse childFoglieUno = childrenFoglieUno.findFirstOccurrence("autn:child");
																while(childFoglieUno != null)
																{
																	catFiglioUno = new CategoryTO();
																	catFiglioUno.setNome(childFoglieUno.getTagValue("autn:childname"));
																	catFiglioUno.setParent(catFiglio.getId());
																	catFiglioUno.setId(childFoglieUno.getTagValue("autn:childid"));
																	catFiglioUno.setFoglia(true);
																	categories.add(catFiglioUno);
																	//int numChildrenFoglia = Integer.parseInt(childFoglie.getTagValue("autn:numchildren"));
																	
																	childFoglieUno = childFoglieUno.next("autn:child");
																}
																	
															}
																
														}
														childFoglie = childFoglie.next("autn:child");
													}
														
												}
													
											}
										}
									}
									
									childSecondL = childSecondL.next("autn:child");
								}
							}
								
						}
						 
						
					}
					child = child.next("autn:child");
				}
		}
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		return categories;
	}

	/*public ArrayList<CategoryTO> categoryHier(String root, String ambito, String specifica) throws AutonomyException
	{
		ArrayList<CategoryTO> categories = null;
		
			try{
			AciConnection connection = new AciConnection(PropertiesManager.getMyProperty("autonomy.url"), PropertiesManager.getMyPropertyAsInt("autonomy.port"));
			connection.setCharacterEncoding(IDOLEncodings.UTF8);
			AciAction aciAction = new AciAction("CategoryGetHierDetails");
			aciAction.setParameter(new ActionParameter("expand", "all"));
			AciResponse response = connection.aciActionExecute(aciAction);
			if(response.checkForSuccess())
			{
				categories = new ArrayList<CategoryTO>();
				CategoryTO catPadre = null;
				CategoryTO catPadreSecond = null;
				CategoryTO catFiglio = null;
				AciResponse child = response.findFirstOccurrence("autn:child");
				while(child != null)
				{
					catPadre = new CategoryTO();
					if(child.getTagValue("autn:childname").toUpperCase().equals(root))
					{
						catPadre.setNome(child.getTagValue("autn:childname"));
						catPadre.setParent(null);
						catPadre.setId(child.getTagValue("autn:childid"));
						categories.add(catPadre);
						int numChildren = Integer.parseInt(child.getTagValue("autn:numchildren"));
						if(numChildren > 0)
						{
							AciResponse children = child.findFirstEnclosedOccurrence("autn:children");
							if(children != null)
							{
								AciResponse childSecondL = children.findFirstOccurrence("autn:child");
								while(childSecondL != null)
								{
									
									if(ambito.equals("FISSO") && specifica.equals("SOCIAL"))
									{
										if(childSecondL.getTagValue("autn:childname").equals("FissoSocial"))
										{
											catPadreSecond = new CategoryTO();
											catPadreSecond.setNome(childSecondL.getTagValue("autn:childname"));
											catPadreSecond.setParent(catPadre.getId());
											catPadreSecond.setId(childSecondL.getTagValue("autn:childid"));
											categories.add(catPadreSecond);
											int numChildrenSecond = Integer.parseInt(childSecondL.getTagValue("autn:numchildren"));
											if(numChildrenSecond > 0)
											{
												AciResponse childrenFoglie = childSecondL.findFirstEnclosedOccurrence("autn:children");
												if(childrenFoglie != null)
												{
													AciResponse childFoglie = childrenFoglie.findFirstOccurrence("autn:child");
													while(childFoglie != null)
													{
														catFiglio = new CategoryTO();
														catFiglio.setNome(childFoglie.getTagValue("autn:childname"));
														catFiglio.setParent(catPadreSecond.getId());
														catFiglio.setId(childFoglie.getTagValue("autn:childid"));
														categories.add(catFiglio);
														childFoglie = childFoglie.next("autn:child");
													}
														
												}
													
											}
										}
									}
									if(ambito.equals("MOBILE") && specifica.equals("SOCIAL"))
									{
										if(childSecondL.getTagValue("autn:childname").equals("MobileSocial"))
										{
											catPadreSecond = new CategoryTO();
											catPadreSecond.setNome(childSecondL.getTagValue("autn:childname"));
											catPadreSecond.setParent(catPadre.getId());
											catPadreSecond.setId(childSecondL.getTagValue("autn:childid"));
											categories.add(catPadreSecond);
											int numChildrenSecond = Integer.parseInt(childSecondL.getTagValue("autn:numchildren"));
											if(numChildrenSecond > 0)
											{
												AciResponse childrenFoglie = childSecondL.findFirstEnclosedOccurrence("autn:children");
												if(childrenFoglie != null)
												{
													AciResponse childFoglie = childrenFoglie.findFirstOccurrence("autn:child");
													while(childFoglie != null)
													{
														catFiglio = new CategoryTO();
														catFiglio.setNome(childFoglie.getTagValue("autn:childname"));
														catFiglio.setParent(catPadreSecond.getId());
														catFiglio.setId(childFoglie.getTagValue("autn:childid"));
														categories.add(catFiglio);
														childFoglie = childFoglie.next("autn:child");
													}
														
												}
													
											}
										}
									}									
									if(ambito.equals("FISSO") && specifica.equals("CASE"))
									{
										if(childSecondL.getTagValue("autn:childname").equals("FissoCase"))
										{
											catPadreSecond = new CategoryTO();
											catPadreSecond.setNome(childSecondL.getTagValue("autn:childname"));
											catPadreSecond.setParent(catPadre.getId());
											catPadreSecond.setId(childSecondL.getTagValue("autn:childid"));
											categories.add(catPadreSecond);
											int numChildrenSecond = Integer.parseInt(childSecondL.getTagValue("autn:numchildren"));
											if(numChildrenSecond > 0)
											{
												AciResponse childrenFoglie = childSecondL.findFirstEnclosedOccurrence("autn:children");
												if(childrenFoglie != null)
												{
													AciResponse childFoglie = childrenFoglie.findFirstOccurrence("autn:child");
													while(childFoglie != null)
													{
														catFiglio = new CategoryTO();
														catFiglio.setNome(childFoglie.getTagValue("autn:childname"));
														catFiglio.setParent(catPadreSecond.getId());
														catFiglio.setId(childFoglie.getTagValue("autn:childid"));
														categories.add(catFiglio);
														childFoglie = childFoglie.next("autn:child");
													}
														
												}
													
											}
										}
									}
									if(ambito.equals("FISSO") && specifica.equals("INTERAZIONI"))
									{
										if(childSecondL.getTagValue("autn:childname").equals("FissoInterazioni"))
										{
											catPadreSecond = new CategoryTO();
											catPadreSecond.setNome(childSecondL.getTagValue("autn:childname"));
											catPadreSecond.setParent(catPadre.getId());
											catPadreSecond.setId(childSecondL.getTagValue("autn:childid"));
											categories.add(catPadreSecond);
											int numChildrenSecond = Integer.parseInt(childSecondL.getTagValue("autn:numchildren"));
											if(numChildrenSecond > 0)
											{
												AciResponse childrenFoglie = childSecondL.findFirstEnclosedOccurrence("autn:children");
												if(childrenFoglie != null)
												{
													AciResponse childFoglie = childrenFoglie.findFirstOccurrence("autn:child");
													while(childFoglie != null)
													{
														catFiglio = new CategoryTO();
														catFiglio.setNome(childFoglie.getTagValue("autn:childname"));
														catFiglio.setParent(catPadreSecond.getId());
														catFiglio.setId(childFoglie.getTagValue("autn:childid"));
														categories.add(catFiglio);
														childFoglie = childFoglie.next("autn:child");
													}
														
												}
													
											}
										}
									}
									if(ambito.equals("MOBILE") && specifica.equals("INTERAZIONI"))
									{
										if(childSecondL.getTagValue("autn:childname").equals("MobileInterazioni"))
										{
											catPadreSecond = new CategoryTO();
											catPadreSecond.setNome(childSecondL.getTagValue("autn:childname"));
											catPadreSecond.setParent(catPadre.getId());
											catPadreSecond.setId(childSecondL.getTagValue("autn:childid"));
											categories.add(catPadreSecond);
											int numChildrenSecond = Integer.parseInt(childSecondL.getTagValue("autn:numchildren"));
											if(numChildrenSecond > 0)
											{
												AciResponse childrenFoglie = childSecondL.findFirstEnclosedOccurrence("autn:children");
												if(childrenFoglie != null)
												{
													AciResponse childFoglie = childrenFoglie.findFirstOccurrence("autn:child");
													while(childFoglie != null)
													{
														catFiglio = new CategoryTO();
														catFiglio.setNome(childFoglie.getTagValue("autn:childname"));
														catFiglio.setParent(catPadreSecond.getId());
														catFiglio.setId(childFoglie.getTagValue("autn:childid"));
														categories.add(catFiglio);
														childFoglie = childFoglie.next("autn:child");
													}
														
												}
													
											}
										}
									}
									if(ambito.equals("MOBILE") && specifica.equals("CASE"))
									{
										if(childSecondL.getTagValue("autn:childname").equals("MobileCase"))
										{
											catPadreSecond = new CategoryTO();
											catPadreSecond.setNome(childSecondL.getTagValue("autn:childname"));
											catPadreSecond.setParent(catPadre.getId());
											catPadreSecond.setId(childSecondL.getTagValue("autn:childid"));
											categories.add(catPadreSecond);
											int numChildrenSecond = Integer.parseInt(childSecondL.getTagValue("autn:numchildren"));
											if(numChildrenSecond > 0)
											{
												AciResponse childrenFoglie = childSecondL.findFirstEnclosedOccurrence("autn:children");
												if(childrenFoglie != null)
												{
													AciResponse childFoglie = childrenFoglie.findFirstOccurrence("autn:child");
													while(childFoglie != null)
													{
														catFiglio = new CategoryTO();
														catFiglio.setNome(childFoglie.getTagValue("autn:childname"));
														catFiglio.setParent(catPadreSecond.getId());
														catFiglio.setId(childFoglie.getTagValue("autn:childid"));
														categories.add(catFiglio);
														childFoglie = childFoglie.next("autn:child");
													}
														
												}
													
											}
										}
									}
									childSecondL = childSecondL.next("autn:child");
								}
							}
								
						}
						 
						
					}
					child = child.next("autn:child");
				}
		}
		}catch (AciException e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new AutonomyException(e.getMessage());
		}
		return categories;
	}*/
}
