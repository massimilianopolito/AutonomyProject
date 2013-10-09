package Autonomy;

public class CategoryTO {

	private String nome;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String parent;
	private String id;
	private boolean foglia;
	public boolean isFoglia() {
		return foglia;
	}
	public void setFoglia(boolean foglia) {
		this.foglia = foglia;
	}
	
}
