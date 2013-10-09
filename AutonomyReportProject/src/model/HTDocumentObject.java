package model;

import java.sql.Timestamp;

import Autonomy.DocumentoTO;



public class HTDocumentObject {
	private String idCluster;
	private DocumentoTO documento;
	private Timestamp dataElaborazione;
	private String score;

	public String getIdCluster() {return idCluster;}
	public void setIdCluster(String idCluster) {this.idCluster = idCluster;}

	public DocumentoTO getDocumento() {return documento;}
	public void setDocumento(DocumentoTO documento) {this.documento = documento;}
	
	public Timestamp getDataElaborazione() {return dataElaborazione;}
	public void setDataElaborazione(Timestamp dataElaborazione) {this.dataElaborazione = dataElaborazione;}
	
	public String getScore() {return score;}
	public void setScore(String score) {this.score = score;}
}
