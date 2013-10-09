package model;

import java.io.Serializable;

public class Troika implements Serializable{

	private String ID;
	private String firstValue;
	private String secondValue;
	private String thirdValue;
	private String descrizione;
	private String custom;
	private String attiva;
	private String codTripletta;
	private String risposta;
	private String caratterizzazione;

	public String getID() {return (ID!=null&&ID.trim().length()==0)?null:ID;}
	public void setID(String iD) {ID = iD;}

	public String getFirstValue() {return firstValue;}
	public void setFirstValue(String firstValue) {this.firstValue = firstValue;}

	public String getSecondValue() {return secondValue;}
	public void setSecondValue(String secondValue) {this.secondValue = secondValue;}

	public String getThirdValue() {return thirdValue;}
	public void setThirdValue(String thirdValue) {this.thirdValue = thirdValue;}

	public String getDescrizione() {return descrizione==null?"":descrizione;}
	public void setDescrizione(String descrizione) {this.descrizione = descrizione;}
	
	public String getCustom() {return custom==null?"":custom;}
	public void setCustom(String custom) {this.custom = custom;}
	
	public String getAttiva() {return attiva;}
	public void setAttiva(String attiva) {this.attiva = attiva;}

	public String getCodTripletta() {return codTripletta;}
	public void setCodTripletta(String codTripletta) {this.codTripletta = codTripletta;}
	
	public String getRisposta() {return risposta;}
	public void setRisposta(String risposta) {this.risposta = risposta;}

	public String getCaratterizzazione() {return caratterizzazione;}
	public void setCaratterizzazione(String caratterizzazione) {this.caratterizzazione = caratterizzazione;}

	public String getCompleteName(){
		return getFirstValue() + "/" + getSecondValue() + "/" + getThirdValue() + ((getCustom()==null||getCustom().trim().length()==0)?"":"/" + getCustom().toUpperCase());
	}
	
	public boolean isFather(){
		return getCustom()==null || getCustom().trim().length()==0;
	}
}
