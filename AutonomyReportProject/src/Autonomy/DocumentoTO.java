package Autonomy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utility.AppConstants;
import utility.NumberGroupingSeparator;

public class DocumentoTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String reference;
	private String totaleDocumenti;
	private String dataBase;
	private String titleDoc;
	private String dreDate;
	private String score;
	private String summary;
	private String summaryShort;
	private String content;
	private String id;
	private String originalDBForProfile;
	private String risposta;
	private String rispostaShort;
	private String query;
	private String summaryShortReal;
	
	private List<DocumentoTO> listOfDocRetrained = new ArrayList<DocumentoTO>();

	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}
	
	public String getReferenceDoc() {return reference;}
	public void setReferenceDoc(String Reference) {
		this.reference = Reference.replace("\"", AppConstants.REFERENCE_DOPPIOAPICE);
	}
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public String getDataBase() {return dataBase;}
	public void setDataBase(String dataBase) {this.dataBase = dataBase;}

	public String getTitleDoc() {return titleDoc;}
	public void setTitleDoc(String titleDoc) {this.titleDoc = titleDoc;}
	
	public String getDreDate() {return dreDate;}
	public void setDreDate(String dreDate) {this.dreDate = dreDate;}

	public String getScore() {return score;}
	public void setScore(String score) {this.score = score;}

	public String getSummary() {return summary;}
	public void setSummary(String summary) {this.summary = summary;}
	
	public String getOriginalDBForProfile() {return originalDBForProfile;}
	public void setOriginalDBForProfile(String originalDBForProfile) {this.originalDBForProfile = originalDBForProfile;}

	public List<DocumentoTO> getListOfDocRetrained() {return listOfDocRetrained;}
	public void setListOfDocRetrained(List<DocumentoTO> listOfDocRetrained) {this.listOfDocRetrained = listOfDocRetrained;}

	public String getTotaleDocumenti() {return NumberGroupingSeparator.formatNumber(totaleDocumenti);}
	public void setTotaleDocumenti(String totaleDocumenti) {this.totaleDocumenti = totaleDocumenti;}

	public String getSummaryShort() {
		summaryShort = getSummary();
		if(getSummary()!=null && getSummary().trim().length()>200) summaryShort = getSummary().substring(0,199) + "..."; 
		return summaryShort;
	}
	public void setSummaryShort(String summaryShort) {
		this.summaryShort = summaryShort;
	}
	public String getRisposta() {return risposta;}
	public void setRisposta(String risposta) {this.risposta = risposta;}
	
	public String getRispostaShort() {
		rispostaShort = getRisposta();
		if(getRisposta()!=null && getRisposta().trim().length()>200) rispostaShort = getRisposta().substring(0,199) + "..."; 
		return rispostaShort;
	}
	public void setRispostaShort(String rispostaShort) {
		this.rispostaShort = rispostaShort;
	}
	public String getQuery() {return query;}
	public void setQuery(String query) {this.query = query;}
	
	public String getSummaryShortReal() {
		
		summaryShortReal = getSummary();
		if(getSummary()!=null && getSummary().trim().length()>700)
		{	
			String appo = getSummary().substring(0,699);
			String appo2 = appo.substring(679);
			if(appo2.contains("<font color=red>")&&appo2.contains("</font>"))
			{	
				summaryShortReal = getSummary().substring(0,699) + "...";
			}
				
		}
		
		return summaryShortReal;
	}
	public void setSummaryShortReal(String summaryShortReal) {
		this.summaryShortReal = summaryShortReal;
	}
	

}
