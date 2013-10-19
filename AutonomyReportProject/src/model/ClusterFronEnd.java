package model;


public class ClusterFronEnd extends HTClusterObject {
	private boolean linked = true;
	private String shortName;

	public ClusterFronEnd() {
		super();
	}
	
	public ClusterFronEnd(HTClusterObject htClusterObject) {
		super();
		setID(htClusterObject.getID());
		setNome(htClusterObject.getNome());
		setIdQuery(htClusterObject.getIdQuery());
		setDataElaborazione(htClusterObject.getDataElaborazione());
		setNumberOfDocs(htClusterObject.getNumberOfDocs());
	}	

	public boolean isLinked() {return linked;}
	public void setLinked(boolean linked) {this.linked = linked;}

	public String getShortName() {
		shortName = getNome();
		if(getNome()!=null && getNome().trim().length()>100) shortName = getNome().substring(0,99)+"...";
		return shortName;
	}
	public void setShortName(String shortName) {this.shortName = shortName;}
	
	

}
