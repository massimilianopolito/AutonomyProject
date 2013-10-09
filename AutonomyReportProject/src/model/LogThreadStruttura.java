package model;

import java.sql.Timestamp;

public class LogThreadStruttura {
	public static String INFO = "INFO";
	public static String WARNING = "WARN";
	public static String ERROR = "ERROR";
	
	private String ID;
	private Timestamp dataEsecuzione;
	private String idQueryEseguita;
	private String livello;
	private String testo;

	public LogThreadStruttura() {
		super();
	}

	public LogThreadStruttura(Timestamp dataEsecuzione, String idQueryEseguita,
			String livello, String testo) {
		super();
		this.dataEsecuzione = dataEsecuzione;
		this.idQueryEseguita = idQueryEseguita;
		this.livello = livello;
		this.testo = testo;
	}
	
	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}

	public Timestamp getDataEsecuzione() {return dataEsecuzione;}
	public void setDataEsecuzione(Timestamp dataEsecuzione) {this.dataEsecuzione = dataEsecuzione;}

	public String getIdQueryEseguita() {return idQueryEseguita;}
	public void setIdQueryEseguita(String idQueryEseguita) {this.idQueryEseguita = idQueryEseguita;}

	public String getLivello() {return livello;}
	public void setLivello(String livello) {this.livello = livello;}

	public String getTesto() {return testo;}
	public void setTesto(String testo) {this.testo = testo;}
}
