package model;


public class ClusterFronEnd extends HTClusterObject {
	private boolean linked = true;

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

}
