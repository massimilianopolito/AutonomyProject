package model;

import java.sql.Timestamp;

public class SnapShot {
	private String ID;
	private Timestamp date;
	private String snapShot;
	private String clusterName;
	private long numDoc;
	private int key;
	private int order;
	private String nomeFile;
	
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
	
}
