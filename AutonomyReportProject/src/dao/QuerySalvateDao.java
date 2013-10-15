package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import model.DatiQuery;
import model.QueryObject;
import utility.AppConstants;
import utility.ConnectionManager;

public class QuerySalvateDao extends AbstractDao{
	
	public void checkQueryNameByUser(QueryObject queryObject) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT ID FROM querysalvate WHERE NomeUtente =? AND UPPER(NomeQuery) = UPPER(?) AND Ticket =? AND TIPO = ? AND AREA = ?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, queryObject.getNomeUtente());
			ps.setString(2, queryObject.getNomeQuery());
			ps.setString(3, queryObject.getTicket());
			ps.setString(4, queryObject.getTipo());
			ps.setString(5, queryObject.getArea());

			rs = ps.executeQuery();
			if(rs.next()) throw new Exception("Il nome query: '" + queryObject.getNomeQuery() + "' è già in uso per l'utente.");

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	}
	
	public void checkQueryNameByUser(QueryObject queryObject, String nomeUtente) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT ID FROM querysalvate WHERE NomeUtente =? AND UPPER(NomeQuery) = UPPER(?) AND Ticket =? AND TIPO = ? AND AREA = ?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, nomeUtente);
			ps.setString(2, queryObject.getNomeQuery());
			ps.setString(3, queryObject.getTicket());
			ps.setString(4, queryObject.getTipo());
			ps.setString(5, queryObject.getArea());

			rs = ps.executeQuery();
			if(rs.next()) throw new Exception("Il nome query: '" + queryObject.getNomeQuery() + "' è già in uso per l'utente.");

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
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
			String sql = "SELECT * FROM querysalvatepublic WHERE NomeQuery =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, idQ);
			//ps.setLong(1, Long.parseLong(idQ));
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
	public Collection<QueryObject> getQuery(QueryObject queryObject)throws Exception{
		Collection<QueryObject> listResult = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			//String sql = "SELECT * FROM querysalvate";
			String sql = "SELECT * FROM querysalvate WHERE NomeUtente =? [TCKT] AND TIPO = ? AND AREA = ?";
			if(queryObject.getArea().equalsIgnoreCase(AppConstants.Ambito.CONSUMER)){
				sql = sql.replace("[TCKT]", "AND Ticket =?");
			}else{
				sql = sql.replace("[TCKT]", "");
			}
			
			int i = 1;
			ps = connection.prepareStatement(sql.trim());
			ps.setString(i, queryObject.getNomeUtente());
			if(queryObject.getArea().equalsIgnoreCase(AppConstants.Ambito.CONSUMER)){
				ps.setString(++i, queryObject.getTicket());
			}
			ps.setString(++i, queryObject.getTipo());
			ps.setString(++i, queryObject.getArea());
			rs = ps.executeQuery();
			
			while(rs.next()){
				if(listResult == null) listResult = new ArrayList<QueryObject>();
				QueryObject currentObj = new QueryObject();
				currentObj.setID(rs.getString("ID"));
				currentObj.setTicket(rs.getString("Ticket"));
				currentObj.setTipo(rs.getString("Tipo"));
				currentObj.setArea(rs.getString("Area"));
				currentObj.setNomeUtente(rs.getString("NomeUtente"));
				currentObj.setNomeQuery(rs.getString("NomeQuery"));
				currentObj.setTesto(rs.getString("Testo"));
				currentObj.setRelevance(rs.getString("Relevance"));
				currentObj.setNumRisultati(rs.getString("NumRisultati"));
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

	public Collection<QueryObject> getQueryPublic(QueryObject queryObject)throws Exception{
		Collection<QueryObject> listResult = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM querysalvatepublic WHERE AREA = ?";
			//String sql = "SELECT * FROM querysalvatepublic WHERE NomeUtente =? AND Ticket =? AND TIPO = ? AND AREA = ?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, queryObject.getArea());
			/*ps.setString(1, queryObject.getNomeUtente());
			ps.setString(2, queryObject.getTicket());
			ps.setString(3, queryObject.getTipo());
			ps.setString(4, queryObject.getArea());*/
			rs = ps.executeQuery();
			
			while(rs.next()){
				if(listResult == null) listResult = new ArrayList<QueryObject>();
				QueryObject currentObj = new QueryObject();
				currentObj.setID(rs.getString("ID"));
				currentObj.setTicket(rs.getString("Ticket"));
				currentObj.setTipo(rs.getString("Tipo"));
				currentObj.setArea(rs.getString("Area"));
				currentObj.setNomeUtente(rs.getString("NomeUtente"));
				currentObj.setNomeQuery(rs.getString("NomeQuery"));
				currentObj.setTesto(rs.getString("Testo"));
				currentObj.setRelevance(rs.getString("Relevance"));
				currentObj.setNumRisultati(rs.getString("NumRisultati"));
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
	
	public Collection<QueryObject> getAll()throws Exception{
		Collection<QueryObject> listResult = null;
		Connection connection = null;
		Statement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM querysalvate ORDER BY ID DESC";
			
			ps = connection.createStatement();
			rs = ps.executeQuery(sql);
			
			while(rs.next()){
				if(listResult == null) listResult = new ArrayList<QueryObject>();
				QueryObject currentObj = new QueryObject();
				currentObj.setID(rs.getString("ID"));
				currentObj.setTicket(rs.getString("Ticket"));
				currentObj.setTipo(rs.getString("Tipo"));
				currentObj.setArea(rs.getString("Area"));
				currentObj.setNomeUtente(rs.getString("NomeUtente"));
				currentObj.setNomeQuery(rs.getString("NomeQuery"));
				currentObj.setTesto(rs.getString("Testo"));
				currentObj.setRelevance(rs.getString("Relevance"));
				currentObj.setNumRisultati(rs.getString("NumRisultati"));
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
	
	public Collection<QueryObject> getAllPublic()throws Exception{
		Collection<QueryObject> listResult = null;
		Connection connection = null;
		Statement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM querysalvatepublic ORDER BY ID DESC";
			
			ps = connection.createStatement();
			rs = ps.executeQuery(sql);
			
			while(rs.next()){
				if(listResult == null) listResult = new ArrayList<QueryObject>();
				QueryObject currentObj = new QueryObject();
				currentObj.setID(rs.getString("ID"));
				currentObj.setTicket(rs.getString("Ticket"));
				currentObj.setTipo(rs.getString("Tipo"));
				currentObj.setArea(rs.getString("Area"));
				currentObj.setNomeUtente(rs.getString("NomeUtente"));
				currentObj.setNomeQuery(rs.getString("NomeQuery"));
				currentObj.setTesto(rs.getString("Testo"));
				currentObj.setRelevance(rs.getString("Relevance"));
				currentObj.setNumRisultati(rs.getString("NumRisultati"));
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

	public QueryObject getQueryById(QueryObject queryObject)throws Exception{
		QueryObject result = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM querysalvate WHERE ID =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(queryObject.getID()));
			rs = ps.executeQuery();
			
			if(rs.next()){
				if(result == null) result = new QueryObject();
				result.setID(rs.getString("ID"));
				result.setTicket(rs.getString("Ticket"));
				result.setTipo(rs.getString("Tipo"));
				result.setArea(rs.getString("Area"));
				result.setNomeUtente(rs.getString("NomeUtente"));
				result.setNomeQuery(rs.getString("NomeQuery"));
				result.setTesto(rs.getString("Testo"));
				result.setRelevance(rs.getString("Relevance"));
				result.setNumRisultati(rs.getString("NumRisultati"));
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	
		return result;
	}

	public QueryObject getQueryByIdPublic(QueryObject queryObject)throws Exception{
		QueryObject result = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM querysalvatepublic WHERE ID =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(queryObject.getID()));
			rs = ps.executeQuery();
			
			if(rs.next()){
				if(result == null) result = new QueryObject();
				result.setID(rs.getString("ID"));
				result.setTicket(rs.getString("Ticket"));
				result.setTipo(rs.getString("Tipo"));
				result.setArea(rs.getString("Area"));
				result.setNomeUtente(rs.getString("NomeUtente"));
				result.setNomeQuery(rs.getString("NomeQuery"));
				result.setTesto(rs.getString("Testo"));
				result.setRelevance(rs.getString("Relevance"));
				result.setNumRisultati(rs.getString("NumRisultati"));
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	
		return result;
	}
	public QueryObject manageQuerySalvate(QueryObject queryObject, Collection<DatiQuery> listDatiQuery) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			String sql ="UPDATE querysalvate SET Ticket=?, Tipo=?, NomeQuery=?, NomeUtente=?, Testo=?, Relevance=?, NumRisultati=?, Area=? WHERE ID = ?";
			
			String ID = queryObject.getID();
			if(ID==null){
				ID = getId(connection, "querysalvate");
				queryObject.setID(ID);
			}
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, queryObject.getTicket());
			ps.setString(2, queryObject.getTipo());
			ps.setString(3, queryObject.getNomeQuery());
			ps.setString(4, queryObject.getNomeUtente());
			ps.setString(5, queryObject.getTesto());
			ps.setString(6, queryObject.getRelevance());
			ps.setString(7, queryObject.getNumRisultati());
			ps.setString(8, queryObject.getArea());
			ps.setLong(9, Long.parseLong(ID));

			int i = ps.executeUpdate();
			
			QueryDatiDao queryDatiDao = new QueryDatiDao();
			String idQ = queryDatiDao.isExists(ID);
			if(idQ != null)
				queryDatiDao.deleteDatiQueryIns(ID, connection);
			if(listDatiQuery!=null){
				for(DatiQuery currentDataField: listDatiQuery){
					currentDataField.setIdQuery(ID);
					queryDatiDao.manageDatiQueryInsert(currentDataField, connection);
				}
			}

			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
		return queryObject;
	}
	
	public QueryObject insertQuerySalvate(QueryObject queryObject, Collection<DatiQuery> listDatiQuery, String user) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			String sql ="INSERT INTO querysalvate (Ticket, Tipo, NomeQuery, NomeUtente, Testo, Relevance, NumRisultati, Area) VALUES(?,?,?,?,?,?,?,?)";
			
			
			
			ps = connection.prepareStatement(sql.trim(), Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, queryObject.getTicket());
			ps.setString(2, queryObject.getTipo());
			ps.setString(3, queryObject.getNomeQuery());
			ps.setString(4, user);
			ps.setString(5, queryObject.getTesto());
			ps.setString(6, queryObject.getRelevance());
			ps.setString(7, queryObject.getNumRisultati());
			ps.setString(8, queryObject.getArea());
			//ps.setLong(9, Long.parseLong(ID));

			int i = ps.executeUpdate();
			
			String ID = null;
			rs = ps.getGeneratedKeys();
			if(rs.next()) ID = rs.getString(1);
			queryObject.setID(ID);
			
			QueryDatiDao queryDatiDao = new QueryDatiDao();
			if(listDatiQuery!=null){
				for(DatiQuery currentDataField: listDatiQuery){
					currentDataField.setIdQuery(ID);
					queryDatiDao.manageDatiQueryInsert(currentDataField, connection);
				}
			}

			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
		return queryObject;
	}
	
	public QueryObject manageQuerySalvatePublic(QueryObject queryObject, Collection<DatiQuery> listDatiQuery) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			String sql ="UPDATE querysalvatepublic SET Ticket=?, Tipo=?, NomeQuery=?, NomeUtente=?, Testo=?, Relevance=?, NumRisultati=?, Area=? WHERE ID = ?";
			
			String ID = queryObject.getID();
			//if(ID==null){
				ID = getId(connection, "querysalvatepublic");
				queryObject.setID(ID);
			//}
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, queryObject.getTicket());
			ps.setString(2, queryObject.getTipo());
			ps.setString(3, queryObject.getNomeQuery());
			ps.setString(4, queryObject.getNomeUtente());
			ps.setString(5, queryObject.getTesto());
			ps.setString(6, queryObject.getRelevance());
			ps.setString(7, queryObject.getNumRisultati());
			ps.setString(8, queryObject.getArea());
			ps.setLong(9, Long.parseLong(ID));

			int i = ps.executeUpdate();
			
			QueryDatiDao queryDatiDao = new QueryDatiDao();
			String idQ = queryDatiDao.isExistsPub(ID);
			if(idQ != null)
				queryDatiDao.deleteDatiQueryInsPub(ID, connection);
			if(listDatiQuery!=null){
				for(DatiQuery currentDataField: listDatiQuery){
					currentDataField.setIdQuery(ID);
					queryDatiDao.manageDatiQueryPublic(currentDataField, connection);
				}
			}

			connection.commit();
		}catch (Exception e) {
			connection.rollback();
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
		return queryObject;
	}
	
	public void deleteQuerySalvate(QueryObject queryObject) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			String sql = "DELETE FROM querysalvate WHERE ID =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(queryObject.getID()));

			ps.executeUpdate();
			
			QueryDatiDao queryDatiDao = new QueryDatiDao();
			DatiQuery datiQuery = new DatiQuery();
			datiQuery.setIdQuery(queryObject.getID());
			queryDatiDao.deleteDatiQuery(datiQuery, connection);

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
	
	public void deleteQuerySalvatePublic(QueryObject queryObject) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			String sql = "DELETE FROM querysalvatepublic WHERE ID =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setLong(1, Long.parseLong(queryObject.getID()));

			ps.executeUpdate();
			
			QueryDatiDao queryDatiDao = new QueryDatiDao();
			DatiQuery datiQuery = new DatiQuery();
			datiQuery.setIdQuery(queryObject.getID());
			queryDatiDao.deleteDatiQueryPub(datiQuery, connection);

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
