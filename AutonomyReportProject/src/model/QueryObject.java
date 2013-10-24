package model;

public class QueryObject {
	private String ID;
	private String area;
	private String ticket;
	private String tipo;
	private String nomeUtente;
	private String nomeQuery;
	private String testo;
	private String relevance;
	private String numRisultati;
	private String riferimento;
	
	public String getID() {return (ID!=null&&ID.trim().length()==0)?null:ID;}
	public void setID(String iD) {ID = iD;}

	public String getNomeUtente() {return nomeUtente;}
	public void setNomeUtente(String nomeUtente) {this.nomeUtente = nomeUtente;}

	public String getNomeQuery() {return nomeQuery==null?null:nomeQuery.trim();}
	public void setNomeQuery(String nomeQuery) {this.nomeQuery = nomeQuery;}
	
	public String getTesto() {return testo==null?"":testo.trim();}
	public void setTesto(String testo) {this.testo = testo;}

	public String getRelevance() {return relevance;}
	public void setRelevance(String relevance) {this.relevance = relevance;}
	
	public String getNumRisultati() {return numRisultati;}
	public void setNumRisultati(String numRisultati) {this.numRisultati = numRisultati;}

	public String getTicket() {return ticket;}
	public void setTicket(String ticket) {this.ticket = ticket;}

	public String getTipo() {return tipo;}
	public void setTipo(String tipo) {this.tipo = tipo;}

	public String getArea() {return area;}
	public void setArea(String area) {this.area = area;}
	
	public String getRiferimento() {return riferimento;}
	public void setRiferimento(String riferimento) {this.riferimento = riferimento;}
	
}
