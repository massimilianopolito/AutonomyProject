package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import Autonomy.ClusterData;

/**
 * @author MassimilianoPolito
 *
 */
/**
 * @author MassimilianoPolito
 *
 */
public class JobDataDescr {
	private String ambito;
	private String radiceJob;
	private String suffissoJob;
	private String rappresentazione;
	private String tipoTicket;
	private String actionName;
	private String dataInizioSelected;
	private String dataFineSelected;
	private String operation;
	private String classeReport;
	//private String amministrazione;
	private ArrayList<ClusterData> list;
	private List<String> extremeDate;
	private Collection<CategoryFrontEnd> categoryList;
	private Collection<ClusterFronEnd> clusterList;
	private Collection<String> comboValues;
	private Collection<String> comboCustomValues;
	private Map<Timestamp,  Map<Integer, Collection<SnapShot>>> snapByDate;
	
	public String getRappresentazione() {return rappresentazione;}
	public void setRappresentazione(String rappresentazione) {this.rappresentazione = rappresentazione;}

	public String getTipoTicket() {return tipoTicket==null?"":tipoTicket;}
	public void setTipoTicket(String tipoTicket) {this.tipoTicket = tipoTicket;}

	public String getDataInizioSelected() {return dataInizioSelected;}
	public void setDataInizioSelected(String dataInizioSelected) {this.dataInizioSelected = dataInizioSelected;}

	public String getDataFineSelected() {return dataFineSelected;}
	public void setDataFineSelected(String dataFineSelected) {this.dataFineSelected = dataFineSelected;}

	public ArrayList<ClusterData> getList() {return list;}
	public void setList(ArrayList<ClusterData> list) {this.list = list;}

	public String getActionName() {return actionName;}
	public void setActionName(String actionName) {this.actionName = actionName;}

	public Collection<CategoryFrontEnd> getCategoryList() {return categoryList;}
	public void setCategoryList(Collection<CategoryFrontEnd> categoryList) {this.categoryList = categoryList;}
	
	public String getAmbito() {return ambito;}
	public void setAmbito(String ambito) {this.ambito = ambito;}

	public String getRadiceJob() {return radiceJob;}
	public void setRadiceJob(String radiceJob) {this.radiceJob = radiceJob;}

	public String getSuffissoJob() {return suffissoJob;}
	public void setSuffissoJob(String suffissoJob) {this.suffissoJob = suffissoJob;}
	
	public String getOperation() {return operation;}
	public void setOperation(String operation) {this.operation = operation;}

	public Collection<String> getComboValues() {return comboValues;}
	public void setComboValues(Collection<String> comboValues) {this.comboValues = comboValues;}
	
	public String getClasseReport() {return classeReport==null?"":classeReport;}
	public void setClasseReport(String classeReport) {this.classeReport = classeReport;}
	
	public Collection<ClusterFronEnd> getClusterList() {return clusterList;}
	public void setClusterList(Collection<ClusterFronEnd> clusterList) {this.clusterList = clusterList;}
	
	public Collection<String> getComboCustomValues() {return comboCustomValues;}
	public void setComboCustomValues(Collection<String> comboCustomValues) {this.comboCustomValues = comboCustomValues;}
	
	public List<String> getExtremeDate() {return extremeDate;}
	public void setExtremeDate(List<String> extremeDate) {this.extremeDate = extremeDate;}
	
	public Map<Timestamp, Map<Integer, Collection<SnapShot>>> getSnapByDate() {return snapByDate;}
	public void setSnapByDate(Map<Timestamp, Map<Integer, Collection<SnapShot>>> snapByDate) {this.snapByDate = snapByDate;}
	
	
/*	public String getAmministrazione() {return amministrazione;}
	public void setAmministrazione(String amministrazione) {this.amministrazione = amministrazione;}
*/	
}
