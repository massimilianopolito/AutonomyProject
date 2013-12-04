package model;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class OccorrenzePallografoObject implements Serializable {
	private String ID;
	private Timestamp data;
	private long occorrenze;
	private String area;//CONSUMER - CORPORATE
	private String tipo;//CASE - INTERAZIONI
	private String ticket;//FISSO - MOBILE
	
	public String getID() {return ID;}
	public void setID(String iD) {ID = iD;}

	public Timestamp getData() {return data;}
	public void setData(Timestamp data) {this.data = data;}

	public long getOccorrenze() {return occorrenze;}
	public void setOccorrenze(long occorrenze) {this.occorrenze = occorrenze;}

	public String getArea() {return area;}
	public void setArea(String area) {this.area = area;}
	
	public String getTipo() {return tipo;}
	public void setTipo(String tipo) {this.tipo = tipo;}

	public String getTicket() {return ticket;}
	public void setTicket(String ticket) {this.ticket = ticket;}
	
}
