package model;

import java.sql.Timestamp;



public class HTClusterObject {
	private String ID;
	private String idQuery;
	private String nome;
	private Timestamp dataElaborazione;
	private String numberOfDocs;

	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}
	
	public String getIdQuery() {return idQuery;}
	public void setIdQuery(String idQuery) {this.idQuery = idQuery;}
	
	public String getNome() {return nome!=null?nome.trim():null;}
	public void setNome(String nome) {this.nome = nome;}
	
	public Timestamp getDataElaborazione() {return dataElaborazione;}
	public void setDataElaborazione(Timestamp dataElaborazione) {this.dataElaborazione = dataElaborazione;}
	
	public String getNumberOfDocs() {return numberOfDocs;}
	public void setNumberOfDocs(String numberOfDocs) {this.numberOfDocs = numberOfDocs;}
}
