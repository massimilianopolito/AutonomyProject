package Autonomy;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Bean2DMapTO implements Serializable{
	
	private int x;
	private int y;
	private String path;
	private ArrayList<DocumentoTO> resultList = new ArrayList<DocumentoTO>();
	private String mapName; 
	private String timeSpan;
	private String clusterName;
	private String souceJobName;
	private int numDocs;
	private String numCluster;
	private String clusterScore;
	
	public int getX() {return x;}
	public void setX(int x) {this.x = x;}
	
	public int getY() {return y;}
	public void setY(int y) {this.y = y;}
	
	public String getPath() {return path;}
	public void setPath(String path) {this.path = path;}
	
	public ArrayList<DocumentoTO> getResultList() {return resultList;}
	public void setResultList(ArrayList<DocumentoTO> resultList) {this.resultList = resultList;}
	
	public String getClusterName() {return clusterName;}
	public void setClusterName(String clusterName) {this.clusterName = clusterName;}
	
	public int getNumDocs() {return numDocs;}
	public void setNumDocs(int numDocs) {this.numDocs = numDocs;}
	
	public String getClusterScore() {return clusterScore;}
	public void setClusterScore(String clusterScore) {this.clusterScore = clusterScore;}
	
	public String getMapName() {return mapName;}
	public void setMapName(String mapName) {this.mapName = mapName;}
	
	public String getTimeSpan() {return timeSpan;}
	public void setTimeSpan(String timeSpan) {this.timeSpan = timeSpan;}
	
	public String getSouceJobName() {return souceJobName;}
	public void setSouceJobName(String souceJobName) {this.souceJobName = souceJobName;}
	
	public String getNumCluster() {return numCluster;}
	public void setNumCluster(String numCluster) {this.numCluster = numCluster;}
	

}
