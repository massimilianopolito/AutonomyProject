package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import model.ThreadTracer;
import utility.ConnectionManager;

public class ThreadTracerDao extends AbstractDao {
	private String INSERT = "INSERT INTO threadTracer(stato, dataAggiornamento, note, IdThread) VALUES(?,NOW(),?,?)";
	private String UPDATE = "UPDATE threadTracer SET stato=?, DataAggiornamento=NOW(), note=? WHERE IdThread=?";
	private String SELECT = "SELECT * FROM threadTracer WHERE IdThread=?";
	
	public ThreadTracer manageCurrentThread(ThreadTracer currentThread) throws Exception{

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			
			String sql = null;
			
			ThreadTracer search = new ThreadTracer();
			search.setIdThread(currentThread.getIdThread());
			search = getCurrentThread(search);
			if(search.getDataAggiornamento()!=null){
				sql = UPDATE;
			}else{
				sql = INSERT;
			}
			
			logger.debug("IdThread: " + currentThread.getIdThread());
			logger.debug("Stato: " + currentThread.getStato());
			logger.debug("Note: " + currentThread.getNote());
			logger.debug("ESEGUE: " + sql + "\n");
			ps = connection.prepareStatement(sql.trim());
			ps.setInt(1, currentThread.getStato());
			ps.setString(2, currentThread.getNote());
			ps.setInt(3, currentThread.getIdThread());

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
			
		return currentThread;
	}
	
	public ThreadTracer getCurrentThread(ThreadTracer currentThread) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			
			String sql =SELECT;
			ps = connection.prepareStatement(sql.trim());
			ps.setInt(1, currentThread.getIdThread());
			rs = ps.executeQuery();

			if(rs.next()){
				currentThread.setDataAggiornamento(rs.getTimestamp("DataAggiornamento"));
				currentThread.setStato(rs.getInt("Stato"));
				currentThread.setNote(rs.getString("Note"));
			}
			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}

		return currentThread;
	}

}
