package model;

import java.sql.Timestamp;

import com.autonomy.aci.AciResponse;

public class HTAciResponseObject {
	private String ID;
	private String IdQuery;
	private AciResponse aciResponse;
	private Timestamp dataElaborazione;

	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}

	public AciResponse getAciResponse() {return aciResponse;}
	public void setAciResponse(AciResponse aciResponse) {this.aciResponse = aciResponse;}
	
	public Timestamp getDataElaborazione() {return dataElaborazione;}
	public void setDataElaborazione(Timestamp dataElaborazione) {this.dataElaborazione = dataElaborazione;}
	
	public String getIdQuery() {return IdQuery;}
	public void setIdQuery(String idQuery) {IdQuery = idQuery;}
	
}
