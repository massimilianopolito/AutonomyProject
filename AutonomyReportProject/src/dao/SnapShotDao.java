package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.SnapShot;
import utility.DateConverter;

public class SnapShotDao extends AbstractDao {
	private String EXTREME_DATE = "SELECT MIN(data) AS MINDATE, MAX(data) AS MAXDATE FROM SnapShotData WHERE snapshot=?";
	private String INSERT = "INSERT INTO SnapShotData(data, snapshot, clustername, numdoc, familyID, position, nomefile, autonomyDate, idlegame) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private String TRUNCATE = "TRUNCATE TABLE SnapShotData";
	private String SELECT = "SELECT * FROM SnapShotData";

	public SnapShotDao(Connection connection) {
		super();
		this.connection = connection;
	}

	public SnapShot getLink(SnapShot currentSnapShot)throws Exception{
		//Collection<SnapShot> result = new ArrayList<SnapShot>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			logger.debug("----------------------------------------------------------------------");
			String sql = SELECT + " WHERE data=? AND idlegame=? AND snapshot=? ORDER BY data ASC";
			logger.debug(sql);
			String val = "["+ currentSnapShot.getDate() + ", " +  currentSnapShot.getIdLegame() + ", " + currentSnapShot.getSnapShot() + "]";
			logger.debug(val);

			ps = connection.prepareStatement(sql);
			ps.setTimestamp(1, currentSnapShot.getDate());
			ps.setInt(2, currentSnapShot.getIdLegame());
		//	ps.setInt(3, currentSnapShot.getKey());
			ps.setString(3, currentSnapShot.getSnapShot());
			rs = ps.executeQuery();
			
			if(rs.next()){
				currentSnapShot.setID(rs.getString("ID"));
				currentSnapShot.setDate(rs.getTimestamp("data"));
				currentSnapShot.setSnapShot(rs.getString("snapshot"));
				currentSnapShot.setClusterName(rs.getString("clustername"));
				currentSnapShot.setNumDoc(rs.getInt("numdoc"));
				currentSnapShot.setKey(rs.getInt("familyID")); 
				currentSnapShot.setOrder(rs.getInt("position"));
				currentSnapShot.setNomeFile(rs.getString("nomefile"));
				currentSnapShot.setIdLegame(rs.getInt("idlegame"));
				currentSnapShot.setAutonomyDate(rs.getString("autonomyDate"));
			}
			
			logger.debug("----------------------------------------------------------------------");

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		return currentSnapShot;
	}

	public Collection<SnapShot> getRecordByDate(Timestamp dateFrom, Timestamp dateTo, String snapShotName)throws Exception{
		Collection<SnapShot> result = new ArrayList<SnapShot>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = SELECT + " WHERE (data>=? AND data<=?) AND snapshot=? ORDER BY ID";
			logger.debug(sql);

			ps = connection.prepareStatement(sql);
			ps.setTimestamp(1, dateFrom);
			ps.setTimestamp(2, dateTo);
			ps.setString(3, snapShotName);
			rs = ps.executeQuery();

			String val = "["+ dateFrom + ", " +  dateTo + ", " + snapShotName + "]";
			logger.debug(val);

			while(rs.next()){
				SnapShot snapShot = new SnapShot();
				snapShot.setID(rs.getString("ID"));
				snapShot.setDate(rs.getTimestamp("data"));
				snapShot.setSnapShot(rs.getString("snapshot"));
				snapShot.setClusterName(rs.getString("clustername"));
				snapShot.setNumDoc(rs.getInt("numdoc"));
				snapShot.setKey(rs.getInt("familyID")); 
				snapShot.setOrder(rs.getInt("position"));
				snapShot.setNomeFile(rs.getString("nomefile"));
				snapShot.setIdLegame(rs.getInt("idlegame"));
				snapShot.setAutonomyDate(rs.getString("autonomyDate"));
				result.add(snapShot);
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		return result;
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

				if(minDate!=null && maxDate!=null){
					date.add(DateConverter.getDate(minDate, DateConverter.PATTERN_VIEW));
					date.add(DateConverter.getDate(maxDate, DateConverter.PATTERN_VIEW));
				}
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
	        preparedStatementBatchUpdate.setString(8, snapShot.getAutonomyDate());
	        preparedStatementBatchUpdate.setInt(9, snapShot.getIdLegame());

	        preparedStatementBatchUpdate.addBatch();

		}catch (Exception e) {
			throw e;
		}
	}


}
