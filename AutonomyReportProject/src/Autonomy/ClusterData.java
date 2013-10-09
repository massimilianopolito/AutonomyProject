package Autonomy;

import java.util.ArrayList;
import java.util.Collections;

public class ClusterData {
	
	private ArrayList<String> dataInizioMap = new ArrayList<String>();
	private ArrayList<String> pathMap = new ArrayList<String>();
	private ArrayList<String> dataFineMap = new ArrayList<String>();
	private ArrayList<String> pathSpectro = new ArrayList<String>();
	private ArrayList<String> dataInizioSpectro = new ArrayList<String>();
	private ArrayList<String> dataFineSpectro = new ArrayList<String>();
	private String mapAcro;
	private String spectroAcro;
	
	
	
	public String getMapAcro() {return mapAcro;}
	public void setMapAcro(String mapAcro) {this.mapAcro = mapAcro;}
	
	public String getSpectroAcro() {return spectroAcro;}
	public void setSpectroAcro(String spectroAcro) {this.spectroAcro = spectroAcro;}
	 
	public ArrayList<String> getDataInizioMap() {
		Collections.sort(dataInizioMap, Collections.reverseOrder());
		return dataInizioMap ;
	}
	public void setDataInizio(ArrayList<String> dataInizioMap) {this.dataInizioMap = dataInizioMap;}
	
	public ArrayList<String> getDataFineMap() {
		Collections.sort(dataFineMap, Collections.reverseOrder());
		return dataFineMap;
	}
	public void setDataFine(ArrayList<String> dataFineMap) {this.dataFineMap = dataFineMap;}
	
	public ArrayList<String> getDataInizioSpectro() {
		Collections.sort(dataInizioSpectro, Collections.reverseOrder());
		return dataInizioSpectro;
	}
	public void setDataInizioSpectro(ArrayList<String> dataInizioSpectro) {this.dataInizioSpectro = dataInizioSpectro;}
	
	public ArrayList<String> getDataFineSpectro() {
		Collections.sort(dataFineSpectro, Collections.reverseOrder());
		return dataFineSpectro;
	}
	public void setDataFineSpectro(ArrayList<String> dataFineSpectro) {this.dataFineSpectro = dataFineSpectro;}
	
	public ArrayList<String> getPathMap() {return pathMap;}
	public void setPathMap(ArrayList<String> pathMap) {this.pathMap = pathMap;}
	
	public ArrayList<String> getPathSpectro() {return pathSpectro;}
	public void setPathSpectro(ArrayList<String> pathSpectro) {this.pathSpectro = pathSpectro;}

}
