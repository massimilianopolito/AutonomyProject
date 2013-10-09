package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import model.DatiQuery;
import model.QueryObject;
import utility.ConnectionManager;

public class QueryDatiDao extends AbstractDao{

	public Collection<DatiQuery> getQuery(QueryObject queryObject)throws Exception{
		Collection<DatiQuery> listResult = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM datiquery WHERE IdQuery =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(queryObject.getID()));
			rs = ps.executeQuery();
			
			while(rs.next()){
				if(listResult == null) listResult = new ArrayList<DatiQuery>();
				DatiQuery currentObj = new DatiQuery();
				currentObj.setID(rs.getString("ID"));
				currentObj.setIdQuery(rs.getString("IdQuery"));
				currentObj.setIdCampo(rs.getString("NomeCampo"));
				currentObj.setValoreCampo(rs.getString("Valore"));
				listResult.add(currentObj);
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	
		return listResult;
	}

	public Collection<DatiQuery> getQueryPublic(QueryObject queryObject)throws Exception{
		Collection<DatiQuery> listResult = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM datiquerypublic WHERE IdQuery =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(queryObject.getID()));
			rs = ps.executeQuery();
			
			while(rs.next()){
				if(listResult == null) listResult = new ArrayList<DatiQuery>();
				DatiQuery currentObj = new DatiQuery();
				currentObj.setID(rs.getString("ID"));
				currentObj.setIdQuery(rs.getString("IdQuery"));
				currentObj.setIdCampo(rs.getString("NomeCampo"));
				currentObj.setValoreCampo(rs.getString("Valore"));
				listResult.add(currentObj);
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	
		return listResult;
	}
	public String isExists(String idQ)throws Exception{
		String id = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM datiquery WHERE IdQuery =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(idQ));
			//ps.setLong(2, Long.parseLong(datiQuery.getIdCampo()));
			rs = ps.executeQuery();

			if(rs.next()) id = rs.getString("ID");

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	
		return id;
	}
	
	public String isExistsPub(String idQ)throws Exception{
		String id = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM datiquerypublic WHERE IdQuery =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(idQ));
			//ps.setLong(2, Long.parseLong(datiQuery.getIdCampo()));
			rs = ps.executeQuery();

			if(rs.next()) id = rs.getString("ID");

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	
		return id;
	}

	public void manageDatiQuery(DatiQuery datiQuery) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			String sql ="UPDATE datiquery SET IdQuery=?, IdCampo=?, Valore=? WHERE ID = ?";
			
			String ID = isExists(datiQuery.getIdQuery());
			if(ID == null){
				ID = getId(connection, "datiquery");
			}
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, datiQuery.getIdQuery());
			ps.setString(2, datiQuery.getIdCampo());
			ps.setString(3, datiQuery.getValoreCampo());
			ps.setLong(4, Long.parseLong(ID));

			int i = ps.executeUpdate();

			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw e;
		}finally{
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	}

	public void manageDatiQuery(DatiQuery datiQuery, Connection connection) throws Exception{
		PreparedStatement ps = null;
		try{
			String sql ="UPDATE datiquery SET IdQuery=?, NomeCampo=?, Valore=? WHERE IdQuery = ?";
			//String sql ="UPDATE datiquery SET IdQuery=?, NomeCampo=?, Valore=? WHERE ID = ?";
			
			String ID = datiQuery.getID();
			if(ID==null){
				ID = getId(connection, "datiquery");
			}
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, datiQuery.getIdQuery());
			ps.setString(2, datiQuery.getIdCampo());
			ps.setString(3, datiQuery.getValoreCampo());
			ps.setString(4, datiQuery.getIdQuery());
			//ps.setLong(4, Long.parseLong(ID));

			int i = ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}
	
	public void manageDatiQueryInsert(DatiQuery datiQuery, Connection connection) throws Exception{
		PreparedStatement ps = null;
		try{
			String sql ="INSERT INTO datiquery(IdQuery,NomeCampo,Valore) VALUES(?,?,?)";
			//String sql ="UPDATE datiquery SET IdQuery=?, NomeCampo=?, Valore=? WHERE ID = ?";
			
			
			//if(ID==null){
			//	ID = getId(connection, "datiquery");
			//}
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(datiQuery.getIdQuery()));
			//ps.setString(1, datiQuery.getIdQuery());
			ps.setString(2, datiQuery.getIdCampo());
			ps.setString(3, datiQuery.getValoreCampo());
			//ps.setString(4, datiQuery.getIdQuery());
			//ps.setLong(4, Long.parseLong(ID));
			
			int i = ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}
	
	public void manageDatiQueryPublic(DatiQuery datiQuery, Connection connection) throws Exception{
		PreparedStatement ps = null;
		try{
			String sql ="INSERT INTO datiquerypublic(IdQuery,NomeCampo,Valore) VALUES(?,?,?)";
			//String sql ="UPDATE datiquery SET IdQuery=?, NomeCampo=?, Valore=? WHERE ID = ?";
			
			
			//if(ID==null){
			//	ID = getId(connection, "datiquery");
			//}
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(datiQuery.getIdQuery()));
			//ps.setString(1, datiQuery.getIdQuery());
			ps.setString(2, datiQuery.getIdCampo());
			ps.setString(3, datiQuery.getValoreCampo());
			//ps.setString(4, datiQuery.getIdQuery());
			//ps.setLong(4, Long.parseLong(ID));
			
			int i = ps.executeUpdate();


		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}

	public void deleteDatiQuery(DatiQuery datiQuery) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "DELETE FROM datiquery WHERE ID =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(datiQuery.getID()));

			ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	}

		public void deleteDatiQuery(DatiQuery datiQuery, Connection connection) throws Exception{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "DELETE FROM datiquery WHERE IdQuery =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(datiQuery.getIdQuery()));

			ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
	}

	public void deleteDatiQueryPub(DatiQuery datiQuery, Connection connection) throws Exception{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "DELETE FROM datiquerypublic WHERE IdQuery =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(datiQuery.getIdQuery()));

			ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
	}

public void deleteDatiQueryIns(String IdQ, Connection connection) throws Exception{
	PreparedStatement ps = null;
	ResultSet rs = null;
	try{
		ConnectionManager connectionManager = ConnectionManager.getInstance();
		DataSource ds = connectionManager.getDataSource();
		connection = ds.getConnection();
		String sql = "DELETE FROM datiquery WHERE IdQuery =?";
		
		ps = connection.prepareStatement(sql.trim());
		ps.setLong(1, Long.parseLong(IdQ));

		ps.executeUpdate();

	}catch (Exception e) {
		throw e;
	}finally{
		if(rs!=null) rs.close();
		if(ps!=null) ps.close();
	}
}

public void deleteDatiQueryInsPub(String IdQ, Connection connection) throws Exception{
	PreparedStatement ps = null;
	ResultSet rs = null;
	try{
		ConnectionManager connectionManager = ConnectionManager.getInstance();
		DataSource ds = connectionManager.getDataSource();
		connection = ds.getConnection();
		String sql = "DELETE FROM datiquerypublic WHERE IdQuery =?";
		
		ps = connection.prepareStatement(sql.trim());
		ps.setLong(1, Long.parseLong(IdQ));

		ps.executeUpdate();

	}catch (Exception e) {
		throw e;
	}finally{
		if(rs!=null) rs.close();
		if(ps!=null) ps.close();
	}
}
}
