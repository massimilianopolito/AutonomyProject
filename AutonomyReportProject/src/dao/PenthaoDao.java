package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import Autonomy.DocumentoQueryTO;

import model.PenthaoObject;
import utility.AppConstants;
import utility.ConnectionManager;
import utility.PropertiesManager;

public class PenthaoDao extends AbstractDao {
	
	private void delete(PenthaoObject penthaoObject, Connection connection)throws Exception{
		Statement ps = null;
		try{
			String categoria = AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, penthaoObject.getCategoriaTicket());
			String tableName = (categoria + "_" + penthaoObject.getUser()).toLowerCase();
			String sql = "DELETE FROM " + tableName;
			
			ps = connection.createStatement();
			ps.executeUpdate(sql);

		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
	}
	
	private String[] getValuesByObjectConsumer(DocumentoQueryTO documentoQueryTO, String categoriaTicket){
		String[] values = null;
		
		if(AppConstants.categoriaTicket.CASE.equalsIgnoreCase(categoriaTicket)){
			values = new String[] {documentoQueryTO.getReferenceDoc(),
								   documentoQueryTO.getTitleDocNoTag(),
								   "",//DREDATE
								   documentoQueryTO.getDataBase(),
								   (documentoQueryTO.getCodCase()==null || documentoQueryTO.getCodCase().trim().length()==0)?null:documentoQueryTO.getCodCase(),
								   documentoQueryTO.getSpecifica(),
								   documentoQueryTO.getMotivo(),
								   documentoQueryTO.getArgomento(),
								   documentoQueryTO.getStato(),
								   documentoQueryTO.getDataCreazione(),
								   documentoQueryTO.getFlagWTT(),
								   documentoQueryTO.getFlagRATT(),
								   documentoQueryTO.getTeamInboxDest(),
								   documentoQueryTO.getConclusioni(),
								   documentoQueryTO.getSubConclusioni(),
								   documentoQueryTO.getDescProblema(),
								   documentoQueryTO.getTeamInboxChiusura(),
								   documentoQueryTO.getDataChiusura(),
								   documentoQueryTO.getTeamInboxCreaz(),
								   documentoQueryTO.getServiceTeam(),
								   "",//DATA_IMPORT
								   documentoQueryTO.getSummary(),
								   "1"};
		}else if(AppConstants.categoriaTicket.INTERAZIONI.equalsIgnoreCase(categoriaTicket)){
			values = new String[] {documentoQueryTO.getTitleDoc(),
								   "",//DREDATE
								   documentoQueryTO.getDataBase(),
								   (documentoQueryTO.getCodInterazione()==null|| documentoQueryTO.getCodInterazione().trim().length()==0)?null:documentoQueryTO.getCodInterazione(),
								   documentoQueryTO.getSpecifica(),
								   documentoQueryTO.getMotivo(),
								   documentoQueryTO.getArgomento(),
								   documentoQueryTO.getStato(),
								   documentoQueryTO.getDataCreazione(),
								   documentoQueryTO.getTipoCanale(),
								   documentoQueryTO.getDirezione(),
								   documentoQueryTO.getCodCliente(),
								   documentoQueryTO.getCrmNativo(),
								   documentoQueryTO.getSubConclusioni(),
								   documentoQueryTO.getConclusioni(),
								   documentoQueryTO.getSegmento(),
								   documentoQueryTO.getServiceTeam(),
								   documentoQueryTO.getTeamInboxCreaz(),
								   "",//DATA_IMPORT
								   documentoQueryTO.getSummary(),
								   (documentoQueryTO.getCodCase()==null || documentoQueryTO.getCodCase().trim().length()==0)?null:documentoQueryTO.getCodCase(),
								   "1"};
		}
		
		return values;
	}

	private String[] getValuesByObjectCorporate(DocumentoQueryTO documentoQueryTO, String categoriaTicket){
		String[] values = null;
		
		if(AppConstants.categoriaTicket.CASE.equalsIgnoreCase(categoriaTicket)){
			values = new String[] {documentoQueryTO.getReferenceDoc(),
								   documentoQueryTO.getTitleDocNoTag(),
								   documentoQueryTO.getDataBase(),
								   (documentoQueryTO.getCodCase()==null || documentoQueryTO.getCodCase().trim().length()==0)?null:documentoQueryTO.getCodCase(),
								   documentoQueryTO.getSpecifica(),
								   documentoQueryTO.getMotivo(),
								   documentoQueryTO.getArgomento(),
								   documentoQueryTO.getConclusioni(),
								   documentoQueryTO.getSubConclusioni(),
								   documentoQueryTO.getStato(),
								   documentoQueryTO.getDataCreazione(),
								   documentoQueryTO.getDataChiusura(),
								   documentoQueryTO.getTeamInboxCreaz(),
								   documentoQueryTO.getCodCliente(),
								   documentoQueryTO.getPartitaIva(),
								   documentoQueryTO.getServiceTeam(),
								   documentoQueryTO.getSegmento(),
								   documentoQueryTO.getUfficio(),
								   documentoQueryTO.getScore(),
								   documentoQueryTO.getTipologiaCliente(),
								   documentoQueryTO.getTeamInboxDest(),
								   documentoQueryTO.getTeamInboxChiusura(),
								   documentoQueryTO.getSummary(),
								   "1",
								   ""//DATA_IMPORT
								  };
		}else if(AppConstants.categoriaTicket.INTERAZIONI.equalsIgnoreCase(categoriaTicket)){
			values = new String[] {documentoQueryTO.getReferenceDoc(),
								   documentoQueryTO.getTitleDoc(),
								   documentoQueryTO.getDataBase(),
								   (documentoQueryTO.getCodCase()==null || documentoQueryTO.getCodCase().trim().length()==0)?null:documentoQueryTO.getCodCase(),
								   (documentoQueryTO.getCodInterazione()==null|| documentoQueryTO.getCodInterazione().trim().length()==0)?null:documentoQueryTO.getCodInterazione(),
								   documentoQueryTO.getSpecifica(),
								   documentoQueryTO.getMotivo(),
								   documentoQueryTO.getArgomento(),
								   documentoQueryTO.getConclusioni(),
								   documentoQueryTO.getSubConclusioni(),
								   documentoQueryTO.getStato(),
								   documentoQueryTO.getDataCreazione(),
								   documentoQueryTO.getCodCliente(),
								   documentoQueryTO.getServiceTeam(),
								   documentoQueryTO.getDirezione(),
								   documentoQueryTO.getTipoCanale(),
								   documentoQueryTO.getUfficio(),
								   documentoQueryTO.getScore(),
								   documentoQueryTO.getTipologiaCliente(),
								   documentoQueryTO.getSegmento(),
								   documentoQueryTO.getSummary(),
								   "1",
								   ""
								   };
		}
		
		return values;
	}

	private void insert(PenthaoObject penthaoObject, Connection connection)throws Exception{
		PreparedStatement ps = null;
		try{
			String categoria = AppConstants.getLabelFromIndex(AppConstants.categoriaTicketLabel, penthaoObject.getCategoriaTicket());
			String tableName = (categoria + "_" + penthaoObject.getUser()).toLowerCase();
			String queryProperty = ("penthao." + categoria).toLowerCase() + "Col." + penthaoObject.getArea();
			String column = PropertiesManager.getMyProperty(queryProperty);
			
			String[] columnNames = column.split("\\,");
			int numOfValues = columnNames.length;
			
			String questionMark = "";
			for(int i =0 ; i<numOfValues; i++){
				questionMark = questionMark + "?,";
			}
			String values = "VALUES ( " + questionMark + " )";
			values = values.replace("?, )", "  NOW() )");
			
			String sql = "INSERT INTO " + tableName + " ( " + column + " )" + values;
			
			if(penthaoObject.getListaDocumenti()!=null){
				for(DocumentoQueryTO currentDoc: penthaoObject.getListaDocumenti()){
					try{
						ps = connection.prepareStatement(sql.trim());
						String[] valuesFromObject = null;
						
						if(AppConstants.Ambito.CONSUMER.equalsIgnoreCase(penthaoObject.getArea())){
							valuesFromObject = getValuesByObjectConsumer(currentDoc, penthaoObject.getCategoriaTicket());
						}else{
							valuesFromObject = getValuesByObjectCorporate(currentDoc, penthaoObject.getCategoriaTicket());
						}
						
						for(int i=0; i<numOfValues-1; i++){
							ps.setObject(i+1, valuesFromObject[i]);
						}
						ps.executeUpdate();
					}catch (Exception e) {
						e.printStackTrace();
						throw e;
					}finally{
						if(ps!=null) ps.close();
					}
					
				}
			}

		}catch (Exception e) {
			throw e;
		}finally{
		}
	}
	
	public void managePenthaoTables(PenthaoObject penthaoObject) throws Exception{
		Connection connection = null;
		ConnectionManager connectionManager = ConnectionManager.getInstance();
		try{
			if(penthaoObject.getArea().equalsIgnoreCase(AppConstants.Ambito.CONSUMER)){
				DataSource ds = connectionManager.getDataSourcePenthao();
				connection = ds.getConnection();
				connection.setAutoCommit(false);
			}else{
				connection = connectionManager.getConnectionFromDriverManager(false);
			}
		
			delete(penthaoObject, connection);
			insert(penthaoObject, connection);
			
			connectionManager.commit(connection);
		}catch (Exception e) {
			connectionManager.rollBack(connection);
			throw e;
		}finally{
			connectionManager.closeConnection(connection);;
		}
		
	}

}
