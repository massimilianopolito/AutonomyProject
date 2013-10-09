package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import utility.ConnectionManager;
import utility.PropertiesManager;
import model.LogThreadStruttura;

public class LogThreadStrutturaDao extends AbstractDao {

	public void writeLog(LogThreadStruttura currentLog) throws Exception{

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String logInfo = PropertiesManager.getMyProperty("penthao.thread.logInfo");
			if( (logInfo==null || "no".equalsIgnoreCase(logInfo)) && LogThreadStruttura.INFO.equals(currentLog.getLivello())) return;
			
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			String sql ="UPDATE logthreadStruttura SET DataEsecuzione=?, IdQueryEseguita=?, Livello=?, Testo=? WHERE ID = ?";
			
			String ID = currentLog.getID();
			if(ID==null){
				ID = getId(connection, "logthreadStruttura");
				currentLog.setID(ID);
			}
			
			ps = connection.prepareStatement(sql.trim());
			ps.setTimestamp(1, currentLog.getDataEsecuzione());
			ps.setLong(2, Long.parseLong(currentLog.getIdQueryEseguita()));
			ps.setString(3, currentLog.getLivello());
			ps.setString(4, currentLog.getTesto());
			ps.setLong(5, Long.parseLong(ID));

			int i = ps.executeUpdate();

			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	}
}
