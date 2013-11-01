package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utility.DateConverter;
import model.SnapShot;

public class SnapShotDao extends AbstractDao {
	private String EXTREME_DATE = "SELECT MIN(data) AS MINDATE, MAX(data) AS MAXDATE FROM SnapShotData WHERE snapshot=?";
	private String INSERT = "INSERT INTO SnapShotData(data, snapshot, clustername, numdoc, familyID, position, nomefile) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private String TRUNCATE = "TRUNCATE TABLE SnapShotData";

	public SnapShotDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public List<String> getDate(SnapShot snapShot) throws Exception{
		List<String> date = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = EXTREME_DATE;
			logger.debug(sql);
			ps = connection.prepareStatement(sql);
			ps.setString(1, snapShot.getSnapShot());
			rs = ps.executeQuery();
			
			if(rs.next()){
				date = new ArrayList<String>();
				Timestamp minDate = rs.getTimestamp("MINDATE");
				Timestamp maxDate = rs.getTimestamp("MAXDATE");

				date.add(DateConverter.getDate(minDate, DateConverter.PATTERN_VIEW));
				date.add(DateConverter.getDate(maxDate, DateConverter.PATTERN_VIEW));
			}
			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		
		return date;
	}
	
	public void truncateTable()throws Exception{
		Statement ps = null;
		try{
			ps = connection.createStatement();
			ps.execute(TRUNCATE);
		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
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
			ps.setString(7, snapShot.getNomeFile());
			
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
	        preparedStatementBatchUpdate.setString(7, snapShot.getNomeFile());

	        preparedStatementBatchUpdate.addBatch();

		}catch (Exception e) {
			throw e;
		}
	}


}
