package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import utility.ConnectionManager;
import utility.PropertiesManager;

import model.ConfigurationObject;

public class ConfigurazioneQueryDao {

	public Collection<ConfigurationObject> getConfig(String ticket, String tipo) throws Exception{
		Collection<ConfigurationObject> onPage = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConfigurationObject currentObject = null;

		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM configurazionequery WHERE Ticket =? AND TIPO = ? ORDER BY POSITION ASC";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setInt(1, Integer.parseInt(ticket));
			ps.setInt(2, Integer.parseInt(tipo));

			rs = ps.executeQuery();
			while(rs.next()){
				if(onPage == null) onPage = new ArrayList<ConfigurationObject>();
				currentObject = new ConfigurationObject();
				currentObject.setID(rs.getString("ID"));
				currentObject.setTicket(rs.getString("Ticket"));
				currentObject.setTipo(rs.getString("Tipo"));
				currentObject.setLabel(rs.getString("Label"));
				currentObject.setNomeColonna(rs.getString("NomeColonna"));
				currentObject.setTipoCampo(rs.getString("TipoCampo"));
				currentObject.setValore(rs.getString("Valore"));
				currentObject.setColonnaPagina(rs.getInt("ColonnaPagina"));
				
				onPage.add(currentObject);
			}

			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}

		return onPage;
	}

	public ConfigurationObject getSingleConfig(ConfigurationObject configurationObject) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConfigurationObject currentObject = null;

		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql = "SELECT * FROM configurazionequery WHERE ID =?";
			
			ps = connection.prepareStatement(sql.trim());
			ps.setInt(1, Integer.parseInt(configurationObject.getID()));

			rs = ps.executeQuery();
			if(rs.next()){
				currentObject = new ConfigurationObject();
				currentObject.setID(rs.getString("ID"));
				currentObject.setTicket(rs.getString("Ticket"));
				currentObject.setTipo(rs.getString("Tipo"));
				currentObject.setLabel(rs.getString("Label"));
				currentObject.setNomeColonna(rs.getString("NomeColonna"));
				currentObject.setTipoCampo(rs.getString("TipoCampo"));
				currentObject.setValore(rs.getString("Valore"));
				currentObject.setColonnaPagina(rs.getInt("ColonnaPagina"));
				
			}

			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}

		return currentObject;
	}

}
