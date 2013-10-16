package utility;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import model.HTAciResponseObject;
import model.HTClusterObject;
import model.HTDocumentObject;
import utility.AppConstants;
import utility.ConnectionManager;
import utility.DateConverter;
import utility.PropertiesManager;
import Autonomy.D2Map;
import Autonomy.DocumentoQueryTO;

import com.autonomy.aci.AciResponse;

import dao.HTAciResponseManagerDao;
import dao.HTClusterDao;
import dao.HTDocumentDao;
import dao.ManageTableDao;

public class HotTopicsManageTable  {
	private Logger logger = ReportLogger.getLog("hotTopics");
	private String clusterTableName = AppConstants.HT_TABLE_CLUSTER + "_NEW";
	private String docTableName = AppConstants.HT_TABLE_DOC + "_NEW";

	private void moveTable(String tableName, String pd, Connection connection)throws Exception{
		ManageTableDao manageTableDao = new ManageTableDao(connection);
		String historicName = null;
		if(pd!=null) historicName = tableName + "_" + pd;
		manageTableDao.moveTable(tableName, tableName + "_NEW", historicName);
	}
	
	public void clear(Timestamp dataElaborazione)throws Exception{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = cm.getConnection(false);
		try{
			String pd = null;

			HTAciResponseManagerDao aciResponseManagerDao = new HTAciResponseManagerDao(connection);
			Timestamp previousDate = aciResponseManagerDao.getPreviousDate(dataElaborazione);
			if(previousDate!=null){
				pd = DateConverter.getDate(previousDate, DateConverter.PATTERN_DB);
			}

			logger.debug("prima di moveTable Cluster");
			moveTable(AppConstants.HT_TABLE_CLUSTER, pd, connection);
			logger.debug("dopo moveTable Cluster");
			logger.debug("prima di moveTable doc");
			moveTable(AppConstants.HT_TABLE_DOC, pd, connection);
			logger.debug("dopo moveTable doc");
			cm.commit(connection);
		}catch (Exception e) {
			cm.rollBack(connection);
			throw e;
		}finally{
			cm.closeConnection(connection);
		}
	}
	
	public void saveAciResponse(int queryIndex, AciResponse aciResponse, Timestamp dataElaborazione) throws Exception{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = null;
		try{
			connection = cm.getConnection(false);
			HTAciResponseManagerDao aciResponseManagerDao = new HTAciResponseManagerDao(connection);

			HTAciResponseObject aciResponseObject = new HTAciResponseObject();
			aciResponseObject.setIdQuery(queryIndex+"");
			aciResponseObject.setAciResponse(aciResponse);
			aciResponseObject.setDataElaborazione(dataElaborazione);
			aciResponseManagerDao.manageAciResponse(aciResponseObject);
			
			cm.commit(connection);
		}catch (Exception e) {
			cm.rollBack(connection);
			throw e;
		}finally{
			cm.closeConnection(connection);
		}
	}

	public void deleteOlderAciResponse(Timestamp dataElaborazione) throws Exception{
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = cm.getConnection(false);
		try{
			HTAciResponseManagerDao aciResponseManagerDao = new HTAciResponseManagerDao(connection);

			HTAciResponseObject aciResponseObject = new HTAciResponseObject();
			aciResponseObject.setDataElaborazione(dataElaborazione);
			Collection<Timestamp> older = aciResponseManagerDao.deleteOlder(aciResponseObject);
			
			if(older!=null && !older.isEmpty()){
				ManageTableDao manageTableDao = new ManageTableDao(connection);
				for(Timestamp thisOlder: older){
					String suffix = "%_" + DateConverter.getDate(thisOlder, DateConverter.PATTERN_DB);
					Collection<String> nomi = manageTableDao.getNomiTabelle(suffix);
					for(String tableName: nomi){
						manageTableDao.dropTable(tableName);
					}
					
				}
			}
			
			
			cm.commit(connection);
		}catch (Exception e) {
			cm.rollBack(connection);
			throw e;
		}finally{
			cm.closeConnection(connection);
		}
	}

	public boolean tableMaker(){
		ConnectionManager cm = ConnectionManager.getInstance();
		boolean esito = false;
		Connection connection = null;
		try{
			connection = cm.getConnection(false);
			
			ManageTableDao manageTableDao = new ManageTableDao(connection);
			
			try{
				manageTableDao.dropTable(clusterTableName);
				
			}catch (Exception e) {
				e.printStackTrace();
				logger.debug(e.getMessage());
			}
			
			try{
				manageTableDao.dropTable(docTableName);
			}catch (Exception e) {
				e.printStackTrace();
				logger.debug(e.getMessage());
			}
			
			manageTableDao.createTableLike(clusterTableName, AppConstants.HT_TABLE_CLUSTER);
			manageTableDao.createTableLike(docTableName, AppConstants.HT_TABLE_DOC);
			
			cm.commit(connection);
			esito = true;
		}catch (Exception e) {
			cm.rollBack(connection);
			e.printStackTrace();
		}finally{
			try{
				cm.closeConnection(connection);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return esito;
	}

	public void makeData(int queryNum, AciResponse aciResponse, Timestamp dataElaborazione) throws Exception{
		List<DocumentoQueryTO> result = new ArrayList<DocumentoQueryTO>();
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connection = null;
		try{
			//Recupero i docs
			if(!"max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){
				D2Map d2Map = new D2Map();
				//CASE = dispari, INT = pari
				if(queryNum%2==0){
					result = d2Map.getHotTopicsIntResults(aciResponse, queryNum+"");
					logger.debug("fuori da getHotTopicsIntResults");
				}else{
					result = d2Map.getHotTopicsCaseResults(aciResponse, queryNum+"");
				}
			}else{
				String nome = aciResponse.getName();
				logger.debug(nome);
				for(long i=1; i<=100001; i++){
					DocumentoQueryTO doc = new DocumentoQueryTO();
					doc.setTitleDoc(nome + " Titolo " + i);
					doc.setSummary("Summary " + i);
					doc.setScore(i+"");
					
					String nomeCluster = nome + "Cluster NA";
					if(i%2==0) nomeCluster = nome + "Cluster 2";
					if(i%3==0) nomeCluster = nome + "Cluster 3";
					if(i%5==0) nomeCluster = nome + "Cluster 5";
					if(i%7==0) nomeCluster = nome + "Cluster 7";
					if(i%11==0) nomeCluster = nome + "Cluster 11";
					if(i%13==0) nomeCluster = nome + "Cluster 13";
					if(i%17==0) nomeCluster = nome + "Cluster 17";
					if(i%23==0) nomeCluster = nome + "Cluster 23";
					if(i%29==0) nomeCluster = nome + "Cluster 29";
					
					doc.setNomeCluster(nomeCluster);
					result.add(doc);
				}
			}
			
			connection = cm.getConnection(true);
			HTClusterDao htClusterDao = new HTClusterDao(connection, clusterTableName);
			HTDocumentDao htDocumentDao = new HTDocumentDao(connection, docTableName);
			
			long count = 0;
			for(DocumentoQueryTO currentDoc: result){
				String nomeCluster = currentDoc.getNomeCluster();
				HTClusterObject htClusterObject = new HTClusterObject();
				htClusterObject.setIdQuery(queryNum+"");
				htClusterObject.setNome(nomeCluster);
				htClusterObject.setDataElaborazione(dataElaborazione);
				String ID = htClusterDao.manageCluster(htClusterObject);
				
				HTDocumentObject htDocumentObject = new HTDocumentObject();
				htDocumentObject.setIdCluster(ID);
				htDocumentObject.setDocumento(currentDoc);
				htDocumentObject.setScore(currentDoc.getScore());
				htDocumentObject.setDataElaborazione(dataElaborazione);
				htDocumentDao.manageDocumentBatch(htDocumentObject);

				count++;
				if(count%10000==0){
					htClusterDao.executeBatchByUpdate();
					htDocumentDao.executeBatchByUpdate();
					logger.debug("Aggiorno il batch: " + count);
				}
			}

			htClusterDao.executeBatchByUpdate();
			htDocumentDao.executeBatchByUpdate();
			
/*			logger.debug("prima del commit in makeData");
			cm.commit(connection);
			logger.debug("dopo del commit in makeData");
*/		}catch (Exception e) {
			//cm.rollBack(connection);
			e.printStackTrace();
			throw e;
		}finally{
			cm.closeConnection(connection);
		}
	}
	

}
