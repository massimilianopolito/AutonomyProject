package model;

import java.util.Collection;

import Autonomy.DocumentoQueryTO;

public class PenthaoObject {
	private Collection<DocumentoQueryTO> listaDocumenti;
	private String user;
	private String categoriaTicket;
	private String table;

	public Collection<DocumentoQueryTO> getListaDocumenti() {return listaDocumenti;}
	public void setListaDocumenti(Collection<DocumentoQueryTO> listaDocumenti) {this.listaDocumenti = listaDocumenti;}
	
	public String getUser() {return user;}
	public void setUser(String user) {this.user = user;}

	public String getCategoriaTicket() {return categoriaTicket;}
	public void setCategoriaTicket(String categoriaTicket) {this.categoriaTicket = categoriaTicket;}
	
	public String getTable() {return table;}
	public void setTable(String table) {this.table = table;}
	
}
