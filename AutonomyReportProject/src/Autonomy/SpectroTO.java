package Autonomy;

import java.util.ArrayList;

public class SpectroTO {
	
	private String clusterId;
	private String jobName;
	private String clusterTitle;
	private String fromDate;
	private String toDate;
	private String numDocs;
	private String x1;
	private String x2;
	private String y1;
	private String y2;
	private String radiusFrom;
	private String radiusTo;
	private ArrayList<DocumentoTO> docList = new ArrayList<DocumentoTO>();
	private String dataSetName;
//	private String startDate;
//	private String endDate;
	private String path;
	private String clusterScore;
		
	public String getClusterId() {return clusterId;}
	public void setClusterId(String clusterId) {this.clusterId = clusterId;}
	
	public String getJobName() {return jobName;}
	public void setJobName(String jobName) {this.jobName = jobName;}
	
	public String getClusterTitle() {return clusterTitle;}
	public void setClusterTitle(String clusterTitle) {this.clusterTitle = clusterTitle;}
	
	public String getFromDate() {return fromDate;}
	public void setFromDate(String fromDate) {this.fromDate = fromDate;}
	
	public String getToDate() {return toDate;}
	public void setToDate(String toDate) {this.toDate = toDate;}
	
	public String getNumDocs() {return numDocs;}
	public void setNumDocs(String numDocs) {this.numDocs = numDocs;}
	
	public String getX1() {return x1;}
	public void setX1(String x1) {this.x1 = x1;}
	
	public String getX2() {return x2;}
	public void setX2(String x2) {this.x2 = x2;}
	
	public String getY1() {return y1;}
	public void setY1(String y1) {this.y1 = y1;}
	
	public String getY2() {return y2;}
	public void setY2(String y2) {this.y2 = y2;}
	
	public String getRadiusFrom() {return radiusFrom;}
	public void setRadiusFrom(String radiusFrom) {this.radiusFrom = radiusFrom;}
	
	public String getRadiusTo() {return radiusTo;}
	public void setRadiusTo(String radiusTo) {this.radiusTo = radiusTo;}
	
	public ArrayList<DocumentoTO> getDocList() {return docList;}
	public void setDocList(ArrayList<DocumentoTO> docList) {this.docList = docList;}
	
	public String getDataSetName() {return dataSetName;}
	public void setDataSetName(String dataSetName) {this.dataSetName = dataSetName;}
	
/*	public String getStartDate() {return startDate;}
	public void setStartDate(String startDate) {this.startDate = startDate;}
	
	public String getEndDate() {return endDate;}
	public void setEndDate(String endDate) {this.endDate = endDate;}
*/	
	public String getPath() {return path;}
	public void setPath(String path) {this.path = path;}
	
	public String getClusterScore() {return clusterScore;}
	public void setClusterScore(String clusterScore) {this.clusterScore = clusterScore;}

}
