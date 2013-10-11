package dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.HTDocumentObject;
import utility.AppConstants;
import Autonomy.DocumentoQueryTO;

public class HTDocumentDao extends AbstractDao {
	private String DELETE_BY_IDQUERY = "DELETE FROM [TBNM] WHERE IdCluster=?";
	private String INSERT = "INSERT INTO [TBNM](IdCluster, Documento, Score, DataElaborazione) VALUES (?, ?, ?, ?)";
	private String SELECT_BY_IDCLUSTER = "SELECT * FROM [TBNM] WHERE IdCluster=? ORDER BY Score DESC";
	private String SELECT_COUNT_BY_IDCLUSTER = "SELECT COUNT(*) FROM [TBNM] WHERE IdCluster=?";

	public HTDocumentDao(Connection connection) {
		super();
		this.connection = connection;
		this.currentTableName = AppConstants.HT_TABLE_DOC;
	}

	public HTDocumentDao(Connection connection, String currentTableName) {
		super();
		this.connection = connection;
		this.currentTableName = currentTableName;
	}

	public String  getCountDocumentByCluster(HTDocumentObject htDocumentObject)throws Exception{
		String number = "0";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = SELECT_COUNT_BY_IDCLUSTER.replace("[TBNM]", currentTableName);
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(htDocumentObject.getIdCluster()));
			rs = ps.executeQuery();
			
			if(rs.next()){
				number = rs.getString(1);
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		
		return number;
	}

	public List<HTDocumentObject> getDocumentByCluster(HTDocumentObject htDocumentObject)throws Exception{
		List<HTDocumentObject> listResult = new ArrayList<HTDocumentObject>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = SELECT_BY_IDCLUSTER.replace("[TBNM]", currentTableName);
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(htDocumentObject.getIdCluster()));
			rs = ps.executeQuery();
			
			while(rs.next()){
				HTDocumentObject currentObj = new HTDocumentObject();
				currentObj.setIdCluster(rs.getString("IdCluster"));
				currentObj.setDataElaborazione(rs.getTimestamp("DataElaborazione"));
				currentObj.setScore(rs.getString("Score"));
	            try {
	            	ByteArrayInputStream bais = new ByteArrayInputStream(rs.getBytes("Documento"));
	            	ObjectInputStream ins = new ObjectInputStream(bais);
	            	DocumentoQueryTO resp =(DocumentoQueryTO)ins.readObject();
	            	ins.close();
	            	currentObj.setDocumento(resp);
	            }
	            catch (Exception e) {
	            	e.printStackTrace();
	            }

				listResult.add(currentObj);
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		
		return listResult;
	}

	public void manageDocument(HTDocumentObject htDocumentObject) throws Exception{
		PreparedStatement ps = null;
		try{
			String sql = INSERT.replace("[TBNM]", currentTableName);;
			ps = connection.prepareStatement(sql.trim());

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(bos);

	        oos.writeObject(htDocumentObject.getDocumento());
	        oos.flush();
	        oos.close();
	        bos.close();

	        byte[] data = bos.toByteArray();

	        ps.setLong(1,  Long.parseLong(htDocumentObject.getIdCluster()));
	        ps.setObject(2, data);
			ps.setString(3, htDocumentObject.getScore());
			ps.setTimestamp(4, htDocumentObject.getDataElaborazione());
			
			int i = ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}

	public void manageDocumentBatch(HTDocumentObject htDocumentObject) throws Exception{
		try{
			String sql = INSERT.replace("[TBNM]", currentTableName);
			if(preparedStatementBatchUpdate==null) createPreparedStatementByUpdate(sql);

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(bos);

	        oos.writeObject(htDocumentObject.getDocumento());
	        oos.flush();
	        oos.close();
	        bos.close();

	        byte[] data = bos.toByteArray();

	        preparedStatementBatchUpdate.setLong(1,  Long.parseLong(htDocumentObject.getIdCluster()));
	        preparedStatementBatchUpdate.setObject(2, data);
	        preparedStatementBatchUpdate.setString(3, htDocumentObject.getScore());
	        preparedStatementBatchUpdate.setTimestamp(4, htDocumentObject.getDataElaborazione());

	        preparedStatementBatchUpdate.addBatch();

		}catch (Exception e) {
			throw e;
		}
	}

}
