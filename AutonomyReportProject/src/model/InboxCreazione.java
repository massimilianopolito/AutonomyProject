package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InboxCreazione implements Serializable {

	private String ID;
	private String mercato;
	private String descrizione;

	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}

	public String getMercato() {return mercato;}
	public void setMercato(String mercato) {this.mercato = mercato;}

	public String getDescrizione() {return descrizione;}
	public void setDescrizione(String descrizione) {this.descrizione = descrizione;}
}
