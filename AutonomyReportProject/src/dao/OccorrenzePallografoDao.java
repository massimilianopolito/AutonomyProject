package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import model.OccorrenzePallografoObject;

public class OccorrenzePallografoDao extends AbstractDao {
	private String SELECT_OCCORRENZE = "SELECT SUM(occorrenze) AS TOT FROM snapshotoccorrenze WHERE (DATE(data) BETWEEN DATE(?) AND DATE(?)) AND area=? AND tipo=? AND (ticket=? OR ticket IS NULL)";
	private String INSERT = "INSERT INTO snapshotoccorrenze(data, occorrenze, area, tipo, ticket) VALUES (?,?,?,?,?)";
	private String SELECT_ISEXIST="SELECT data FROM snapshotoccorrenze WHERE DATE(data)=DATE(?) AND area=? AND tipo=? AND (ticket=? OR ticket IS NULL)";
	
	public OccorrenzePallografoDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public long getNumOccorrenze(Timestamp dateFrom, Timestamp dateTo, OccorrenzePallografoObject occorrenzePallografoObject) throws Exception{
		long tot = -1;
		PreparedStatement ps = null;
		ResultSet rs =null;
		try{
			String sql = SELECT_OCCORRENZE;
			ps = connection.prepareStatement(sql.trim());
			logger.debug(sql);
			logger.debug("val:["+ dateFrom +", " 
								+ dateTo +", "
								+ occorrenzePallografoObject.getArea() +", "
								+ occorrenzePallografoObject.getTipo() +", "
								+ occorrenzePallografoObject.getTicket() + "]");

			ps.setTimestamp(1, dateFrom);
			ps.setTimestamp(2, dateTo);
			ps.setString(3, occorrenzePallografoObject.getArea());
			ps.setString(4, occorrenzePallografoObject.getTipo());
			ps.setString(5, occorrenzePallografoObject.getTicket());
			
			rs = ps.executeQuery();
			if(rs.next()) tot = rs.getLong("TOT");

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
			if(rs!=null) rs.close();
		}

		return tot;
	}
	
	public boolean isExistData(OccorrenzePallografoObject occorrenzePallografoObject) throws Exception{
		boolean isExist=false;
		PreparedStatement ps = null;
		ResultSet rs =null;
		try{
			String sql = SELECT_ISEXIST;
			ps = connection.prepareStatement(sql.trim());
			logger.debug(sql);
			logger.debug("val:["+ occorrenzePallografoObject.getData() +", " 
								+ occorrenzePallografoObject.getArea() +", "
								+ occorrenzePallografoObject.getTipo() +", "
								+ occorrenzePallografoObject.getTicket() + "]");

			ps.setTimestamp(1,  occorrenzePallografoObject.getData());
			ps.setString(2, occorrenzePallografoObject.getArea());
			ps.setString(3, occorrenzePallografoObject.getTipo());
			ps.setString(4, occorrenzePallografoObject.getTicket());
			
			rs = ps.executeQuery();
			if(rs.next()) isExist = true;

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
			if(rs!=null) rs.close();
		}
		
		return isExist;
	}

	public void insertOccorrenze(OccorrenzePallografoObject occorrenzePallografoObject) throws Exception{
		PreparedStatement ps = null;
		try{
			String sql = INSERT;
			ps = connection.prepareStatement(sql.trim());
			logger.debug(sql);
			logger.debug("val:["+ occorrenzePallografoObject.getData() +", " 
								+ occorrenzePallografoObject.getOccorrenze() +", " 
								+ occorrenzePallografoObject.getArea() +", "
								+ occorrenzePallografoObject.getTipo() +", "
								+ occorrenzePallografoObject.getTicket() + "]");

			ps.setTimestamp(1,  occorrenzePallografoObject.getData());
	        ps.setLong(2, occorrenzePallografoObject.getOccorrenze());
			ps.setString(3, occorrenzePallografoObject.getArea());
			ps.setString(4, occorrenzePallografoObject.getTipo());
			ps.setString(5, occorrenzePallografoObject.getTicket());
			
			int i = ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}
}
