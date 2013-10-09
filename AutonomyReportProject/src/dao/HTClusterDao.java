package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.HTClusterObject;
import model.HTDocumentObject;
import utility.AppConstants;
import utility.HotTopicsComparator;

public class HTClusterDao extends AbstractDao {
	private String DELETE_BY_IDQUERY = "DELETE FROM [TBNM] WHERE IdQuery=?";
	private String UPDATE_BY_ID = "UPDATE [TBNM] SET IdQuery=?, Nome=?, DataElaborazione=? WHERE ID=?";
	private String SELECT_BY_ID = "SELECT * FROM [TBNM] WHERE ID=?";
	private String SELECT_BY_IDQUERY_NOME = "SELECT * FROM [TBNM] WHERE IdQuery IS NOT NULL AND IdQuery=? AND Nome=?";
	private String SELECT_BY_IDQUERY = "SELECT * FROM [TBNM] WHERE IdQuery=? ORDER BY Nome ASC";

	public HTClusterDao(Connection connection) {
		super();
		this.connection = connection;
		this.currentTableName = AppConstants.HT_TABLE_CLUSTER;
	}

	public HTClusterDao(Connection connection, String currentTableName) {
		super();
		this.connection = connection;
		this.currentTableName = currentTableName;
	}
	
	public List<HTClusterObject> getClusterByQuery(HTClusterObject htClusterObject)throws Exception{
		List<HTClusterObject> listResult = new ArrayList<HTClusterObject>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = SELECT_BY_IDQUERY.replace("[TBNM]", currentTableName);
			
			ps = connection.prepareStatement(sql.trim());
			System.out.println("idQuery :" + htClusterObject.getIdQuery());
			ps.setLong(1, Long.parseLong(htClusterObject.getIdQuery()));
			System.out.println("sql :" + sql);
			rs = ps.executeQuery();

			String tableDocName = currentTableName.replace(AppConstants.HT_TABLE_CLUSTER, AppConstants.HT_TABLE_DOC);
			HTDocumentDao htDocumentDao = new HTDocumentDao(connection, tableDocName);

			while(rs.next()){
				HTClusterObject currentObj = new HTClusterObject();

				currentObj.setID(rs.getString("ID"));
				currentObj.setIdQuery(rs.getString("IdQuery"));
				currentObj.setNome(rs.getString("Nome"));
				currentObj.setDataElaborazione(rs.getTimestamp("DataElaborazione"));
				
				HTDocumentObject htDocumentObject = new HTDocumentObject();
				htDocumentObject.setIdCluster(currentObj.getID());
				currentObj.setNumberOfDocs(htDocumentDao.getCountDocumentByCluster(htDocumentObject));

				listResult.add(currentObj);
			}

			Collections.sort(listResult, Collections.reverseOrder(new HotTopicsComparator()));
			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		
		return listResult;
	}

	private String isExists(HTClusterObject htClusterObject)throws Exception{
		String id = null;

		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = SELECT_BY_IDQUERY_NOME.replace("[TBNM]", currentTableName);
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(htClusterObject.getIdQuery()));
			ps.setString(2, htClusterObject.getNome());
			rs = ps.executeQuery();

			if(rs.next()) id = rs.getString("ID");

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		
		return id;
	}

	public String manageCluster(HTClusterObject htClusterObject) throws Exception{
		PreparedStatement ps = null;
		String ID = null;
		try{
			String sql = UPDATE_BY_ID.replace("[TBNM]", currentTableName);;
			
			ID = isExists(htClusterObject);
			if(ID==null){
				ID =  getId(connection, currentTableName);

				ps = connection.prepareStatement(sql.trim());

		        ps.setLong(1,  Long.parseLong(htClusterObject.getIdQuery()));
		        ps.setString(2, htClusterObject.getNome());
		        ps.setTimestamp(3, htClusterObject.getDataElaborazione());
		        ps.setLong(4,  Long.parseLong(ID));
				
		        int i = ps.executeUpdate();
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
		
		return ID;
	}
	
	public void deleteCluster(HTClusterObject htClusterObject) throws Exception{
		PreparedStatement ps = null;

		try{
			String sql = DELETE_BY_IDQUERY.replace("[TBNM]", currentTableName);;
			
			ps = connection.prepareStatement(sql.trim());
	        ps.setLong(1,  Long.parseLong(htClusterObject.getIdQuery()));
			
			ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
		
	}
	
}
