package model;

import java.sql.Timestamp;

public class ThreadTracer {
	private int IdThread;
	private int stato;
	private Timestamp dataAggiornamento;
	private String note;

	public ThreadTracer() {
		super();
	}

	public ThreadTracer(int idThread, int stato) {
		super();
		this.IdThread = idThread;
		this.stato = stato;
	}

	public int getIdThread() {return IdThread;}
	public void setIdThread(int idThread) {IdThread = idThread;}

	public int getStato() {return stato;}
	public void setStato(int stato) {this.stato = stato;}

	public Timestamp getDataAggiornamento() {return dataAggiornamento;}
	public void setDataAggiornamento(Timestamp dataAggiornamento) {this.dataAggiornamento = dataAggiornamento;}

	public String getNote() {return note;}
	public void setNote(String note) {this.note = note;}
	
}
