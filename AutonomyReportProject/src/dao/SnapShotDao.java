package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import model.SnapShot;

public class SnapShotDao extends AbstractDao {
	private String INSERT = "INSERT INTO SnapShotData(data, snapshot, clustername, numdoc, familyID, position) VALUES (?, ?, ?, ?, ?, ?)";

	public SnapShotDao(Connection connection) {
		super();
		this.connection = connection;
	}

	public void manageDocument(SnapShot snapShot) throws Exception{
		PreparedStatement ps = null;
		try{
			String sql = INSERT;
			ps = connection.prepareStatement(sql.trim());

			ps.setTimestamp(1,  snapShot.getDate());
			ps.setString(2, snapShot.getSnapShot());
			ps.setString(3, snapShot.getClusterName());
			ps.setLong(4, snapShot.getNumDoc());
			ps.setInt(5, snapShot.getKey());
			ps.setInt(6, snapShot.getOrder());
			
			int i = ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}

	public void manageDocumentBatch(SnapShot snapShot) throws Exception{
		try{
			String sql = INSERT;
			if(preparedStatementBatchUpdate==null) createPreparedStatementByUpdate(sql);

	        preparedStatementBatchUpdate.setTimestamp(1,  snapShot.getDate());
	        preparedStatementBatchUpdate.setString(2, snapShot.getSnapShot());
	        preparedStatementBatchUpdate.setString(3, snapShot.getClusterName());
	        preparedStatementBatchUpdate.setLong(4, snapShot.getNumDoc());
	        preparedStatementBatchUpdate.setInt(5, snapShot.getKey());
	        preparedStatementBatchUpdate.setInt(6, snapShot.getOrder());

	        preparedStatementBatchUpdate.addBatch();

		}catch (Exception e) {
			throw e;
		}
	}


}
