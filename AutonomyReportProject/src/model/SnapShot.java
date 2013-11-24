package model;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class SnapShot  implements Serializable{
	private String ID;
	private Timestamp date;
	private String autonomyDate;
	private String snapShot;
	private String clusterName;
	private long numDoc;
	private int key;
	private int order;
	private String nomeFile;
	private int idLegame;
	
	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}

	public Timestamp getDate() {return date;}
	public void setDate(Timestamp date) {this.date = date;}

	public String getSnapShot() {return snapShot;}
	public void setSnapShot(String snapShot) {this.snapShot = snapShot;}

	public String getClusterName() {return clusterName;}
	public void setClusterName(String clusterName) {this.clusterName = clusterName;}

	public long getNumDoc() {return numDoc;}
	public void setNumDoc(long numDoc) {this.numDoc = numDoc;}

	public int getKey() {return key;}
	public void setKey(int key) {this.key = key;}

	public int getOrder() {return order;}
	public void setOrder(int order) {this.order = order;}

	public String getNomeFile() {return nomeFile;}
	public void setNomeFile(String nomeFile) {this.nomeFile = nomeFile;}

	public String getAutonomyDate() {return autonomyDate;}
	public void setAutonomyDate(String autonomyDate) {this.autonomyDate = autonomyDate;}
	
	public int getIdLegame() {return idLegame;}
	public void setIdLegame(int idLegame) {this.idLegame = idLegame;}
	
}
