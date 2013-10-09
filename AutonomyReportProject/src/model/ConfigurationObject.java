package model;

import java.math.BigInteger;

public class ConfigurationObject {
	private String ID;
	private String ticket;
	private String tipo;
	private String label;
	private String nomeColonna;
	private String tipoCampo;
	private String valore;
	private int colonnaPagina;

	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}
	
	public String getTicket() {return ticket;}
	public void setTicket(String ticket) {this.ticket = ticket;}

	public String getTipo() {return tipo;}
	public void setTipo(String tipo) {this.tipo = tipo;}

	public String getLabel() {return label;}
	public void setLabel(String label) {this.label = label;}
	
	public String getNomeColonna() {return nomeColonna;}
	public void setNomeColonna(String nomeColonna) {this.nomeColonna = nomeColonna;}

	public String getTipoCampo() {return tipoCampo;}
	public void setTipoCampo(String tipoCampo) {this.tipoCampo = tipoCampo;}

	public String getValore() {return valore;}
	public void setValore(String valore) {this.valore = valore;}

	public int getColonnaPagina() {return colonnaPagina;}
	public void setColonnaPagina(int colonnaPagina) {this.colonnaPagina = colonnaPagina;}
}
