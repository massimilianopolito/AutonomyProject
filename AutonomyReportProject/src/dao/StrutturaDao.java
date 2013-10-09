package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.sql.DataSource;

import Autonomy.DocumentoQueryTO;

import model.PenthaoObject;
import model.QueryObject;
import utility.AppConstants;
import utility.ConnectionManager;
import utility.PropertiesManager;

public class StrutturaDao extends AbstractDao {
	
	private void deleteIntMobile(Connection connection)throws Exception{
		Statement ps = null;
		try{
			String sql = "TRUNCATE autonomy_int_mobile";
			
			ps = connection.createStatement();
			ps.executeUpdate(sql);

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}
	
	private void deleteIntFisso(Connection connection)throws Exception{
		Statement ps = null;
		try{
			String sql = "TRUNCATE autonomy_int_fisso";
			
			ps = connection.createStatement();
			ps.executeUpdate(sql);

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}
	
	private void deleteCaseFisso(Connection connection)throws Exception{
		Statement ps = null;
		try{
			String sql = "TRUNCATE autonomy_case_fisso";
			
			ps = connection.createStatement();
			ps.executeUpdate(sql);

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}
	
	private void deleteCaseMobile(Connection connection)throws Exception{
		Statement ps = null;
		try{
			String sql = "TRUNCATE autonomy_case_mobile";
			
			ps = connection.createStatement();
			ps.executeUpdate(sql);

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}
	
	public Collection<DocumentoQueryTO> getResult(String query,String table) throws Exception
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Collection<DocumentoQueryTO> listResult = null;
		try{
				ConnectionManager connectionManager = ConnectionManager.getInstance();
				DataSource ds = connectionManager.getDataSource();
				connection = ds.getConnection();
					
				String sql = "SELECT * FROM "+ table + " WHERE NOME_QUERY=? ORDER BY RELEVANCE DESC";
				ps = connection.prepareStatement(sql.trim());
				ps.setString(1, query);
				rs = ps.executeQuery();
				listResult = new ArrayList<DocumentoQueryTO>();
				System.out.println("sql: " + sql);
				while(rs.next()){
					
					DocumentoQueryTO currentObj = new DocumentoQueryTO();
					currentObj.setReferenceDoc(rs.getString("DREREFERENCE"));
					currentObj.setTitleDoc(rs.getString("DRTITLE"));
					currentObj.setDataBase(rs.getString("DREDBNAME"));
					currentObj.setCodInterazione(String.valueOf(rs.getInt("COD_INTERAZIONE")));
					currentObj.setSpecifica(rs.getString("SPECIFICA"));
					currentObj.setMotivo(rs.getString("MOTIVO"));
					currentObj.setArgomento(rs.getString("ARGOMENTO"));
					currentObj.setStato(rs.getString("STATO"));
					currentObj.setDataCreazione(rs.getString("DATA_CREAZIONE"));
					currentObj.setTipoCanale(rs.getString("TIPO_CANALE"));
					currentObj.setDirezione(rs.getString("DIREZIONE"));
					currentObj.setCodCliente(rs.getString("COD_CLIENTE"));
					currentObj.setCrmNativo(rs.getString("CRM_NATIVO"));
					currentObj.setConclusioni(rs.getString("CONCLUSIONI"));
					currentObj.setSubConclusioni(rs.getString("SUBCONCLUSIONI"));
					currentObj.setCodCase(String.valueOf(rs.getInt("COD_CASE")));
					currentObj.setSegmento(rs.getString("SEGMENTO"));
					currentObj.setServiceTeam(rs.getString("SERVICE_TEAM"));
					currentObj.setTeamInboxCreaz(rs.getString("TEAM_INBOX_CREAZ"));
					currentObj.setScore(rs.getString("RELEVANCE"));
					currentObj.setSummary(rs.getString("DRECONTENT"));
					currentObj.setNomeQuery(rs.getString("NOME_QUERY"));
					listResult.add(currentObj);
				}
				
				
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
		}finally{
			if(connection!=null) connection.close();
			
		}	
		return listResult;
	}
	
	private void insert(PenthaoObject penthaoObject, Connection connection)throws Exception{
		Statement ps = null;
		try{
			String categoria = AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, penthaoObject.getCategoriaTicket());
			String tableName = penthaoObject.getTable();
			String queryProperty = ("struttura." + categoria).toLowerCase() + "Col";
			String column = PropertiesManager.getMyProperty(queryProperty);
			
			String yesterday = null ;
			Calendar yesterdayDate = GregorianCalendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
			yesterday = dateFormat.format(yesterdayDate.getTime());
			
			String sql = null;
			System.out.println("dentro insert prima di ciclo inserimento");
			if(penthaoObject.getListaDocumenti()!=null){
				for(DocumentoQueryTO currentDoc: penthaoObject.getListaDocumenti()){
					try{
						if(tableName.equalsIgnoreCase("autonomy_int_fisso"))
							sql = "INSERT INTO " + tableName + " ( " + column + " ) VALUES ('"+currentDoc.getReferenceDoc()+"','"+currentDoc.getTitleDoc()+"','"+currentDoc.getDataBase()+"',"+getCod(currentDoc.getCodInterazione())+",'"+currentDoc.getSpecifica()+"','"+currentDoc.getMotivo()+"','"+currentDoc.getArgomento()+"','"+currentDoc.getStato()+"','"+currentDoc.getDataCreazione()+"','"+currentDoc.getTipoCanale()+"','"+currentDoc.getDirezione()+"','"+currentDoc.getCodCliente()+"','"+currentDoc.getCrmNativo()+"','','',"+getCod(currentDoc.getCodCase())+",'"+currentDoc.getSegmento()+"','"+currentDoc.getServiceTeam()+"','"+yesterday+"','"+currentDoc.getTeamInboxCreaz()+"',"+currentDoc.getScore()+",'','"+currentDoc.getNomeQuery()+"',"+0+")";
						if(tableName.equalsIgnoreCase("autonomy_case_fisso"))
							sql = "INSERT INTO " + tableName + " ( " + column + " ) VALUES ('"+currentDoc.getReferenceDoc()+"','"+currentDoc.getTitleDoc()+"',"+getCod(currentDoc.getCodCase())+",'"+currentDoc.getSpecifica()+"','"+currentDoc.getMotivo()+"','"+currentDoc.getArgomento()+"','"+currentDoc.getStato()+"','"+currentDoc.getDataCreazione()+"','"+currentDoc.getFlagWTT()+"','"+currentDoc.getFlagRATT()+"','"+currentDoc.getTeamInboxDest()+"','','','','"+currentDoc.getTeamInboxChiusura()+"','"+currentDoc.getDataChiusura()+"','"+currentDoc.getTeamInboxCreaz()+"','"+currentDoc.getServiceTeam()+"','"+yesterday+"','"+currentDoc.getDataBase()+"','"+currentDoc.getCodCliente()+"',"+currentDoc.getScore()+",'','"+currentDoc.getNomeQuery()+"',"+0+")";
						if(tableName.equalsIgnoreCase("autonomy_int_mobile"))
							sql = "INSERT INTO " + tableName + " ( " + column + " ) VALUES ('"+currentDoc.getReferenceDoc()+"','"+currentDoc.getTitleDoc()+"','"+currentDoc.getDataBase()+"',"+getCod(currentDoc.getCodInterazione())+",'"+currentDoc.getSpecifica()+"','"+currentDoc.getMotivo()+"','"+currentDoc.getArgomento()+"','"+currentDoc.getStato()+"','"+currentDoc.getDataCreazione()+"','"+currentDoc.getTipoCanale()+"','"+currentDoc.getDirezione()+"','"+currentDoc.getCodCliente()+"','"+currentDoc.getCrmNativo()+"','','',"+getCod(currentDoc.getCodCase())+",'"+currentDoc.getSegmento()+"','"+currentDoc.getServiceTeam()+"','"+yesterday+"','"+currentDoc.getTeamInboxCreaz()+"',"+currentDoc.getScore()+",'','"+currentDoc.getNomeQuery()+"',"+0+")";
						if(tableName.equalsIgnoreCase("autonomy_case_mobile"))
							sql = "INSERT INTO " + tableName + " ( " + column + " ) VALUES ('"+currentDoc.getReferenceDoc()+"','"+currentDoc.getTitleDoc()+"',"+getCod(currentDoc.getCodCase())+",'"+currentDoc.getSpecifica()+"','"+currentDoc.getMotivo()+"','"+currentDoc.getArgomento()+"','"+currentDoc.getStato()+"','"+currentDoc.getDataCreazione()+"','"+currentDoc.getFlagWTT()+"','"+currentDoc.getFlagRATT()+"','"+currentDoc.getTeamInboxDest()+"','','','','"+currentDoc.getTeamInboxChiusura()+"','"+currentDoc.getDataChiusura()+"','"+currentDoc.getTeamInboxCreaz()+"','"+currentDoc.getServiceTeam()+"','"+yesterday+"','"+currentDoc.getDataBase()+"','"+currentDoc.getCodCliente()+"',"+currentDoc.getScore()+",'','"+currentDoc.getNomeQuery()+"',"+0+")";
						
						//System.out.println("query: "+sql); 
						ps = connection.createStatement();
						ps.executeUpdate(sql);
					}catch (Exception e) {
						e.printStackTrace();
						throw e;
					}finally{
						if(ps!=null) ps.close();
					}
					
				}
				System.out.println("finito inserimento per query");
			}

		}catch (Exception e) {
			throw e;
		}finally{
		}
	}
	private int getCod(String cod)
	{
		int codice;
		
		if(cod.equals(null)||cod.trim().length()==0)
		{	
			
			codice = -1;
		}
		else
			codice = Integer.parseInt(cod);
		
		
		return codice;
		
	}
	public String manageStrutturaTables(PenthaoObject penthaoObject) throws Exception{
		Connection connection = null;
		String esito = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
		
			connection.setAutoCommit(false);
			
			//delete(penthaoObject, connection);
			insert(penthaoObject, connection);
			
			connection.commit();
			esito = "0";
		}catch (Exception e) {
			connection.rollback();
			esito = "1";
			throw e;
		}finally{
			if(connection!=null) connection.close();
			
		}
		return esito;
	}
	public void truncateTables() throws Exception{
		Connection connection = null;
		System.out.println("dentro a truncateTables");
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			System.out.println("dentro a truncateTables dopo getInstance");
			DataSource ds = connectionManager.getDataSource();
			System.out.println("dentro a truncateTables dopo getDataSourceStruttura");
			if(ds!=null)
				connection = ds.getConnection();
			else
				System.out.println("dataSource null");
			
			System.out.println("dentro a truncateTables dopo getConnection");
			
			connection.setAutoCommit(false);
			System.out.println("dopo connessione, prima di truncate su tabelle");
			deleteCaseFisso(connection);
			deleteCaseMobile(connection);
			deleteIntFisso(connection);
			deleteIntMobile(connection);
			connection.commit();
			
		}catch (Exception e) {
			connection.rollback();
			throw e;
		}finally{
			if(connection!=null) connection.close();
			
		}
		
	}

}
