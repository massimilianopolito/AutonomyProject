package model;

import java.io.Serializable;

public class DatiQuery implements Serializable{
	private String ID;
	private String idQuery;
	private String idCampo;
	private String valoreCampo;
	
	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}

	public String getIdQuery() {return idQuery;}
	public void setIdQuery(String idQuery) {this.idQuery = idQuery;}

	public String getIdCampo() {return idCampo;}
	public void setIdCampo(String idCampo) {this.idCampo = idCampo;}

	public String getValoreCampo() {return valoreCampo;}
	public void setValoreCampo(String valoreCampo) {this.valoreCampo = valoreCampo;}
}
