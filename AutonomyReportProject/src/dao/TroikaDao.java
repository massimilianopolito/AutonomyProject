package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.Troika;

import utility.ConnectionManager;
import utility.PropertiesManager;

public class TroikaDao {
	public static String COLUMN_FIRST_VALUE_TROIKA = "0";
	public static String COLUMN_SECOND_VALUE_TROIKA = "1";
	public static String COLUMN_THIRD_VALUE_TROIKA = "2";
	public static String COLUMN_CUSTOM_TROIKA = "4";
	private int NUM_OF_COL = 10;
	
	private void checkSql(String sql, String properties) throws Exception{
		if(sql==null || sql.trim().length()==0){
			throw new Exception("La definizione relativa alla voce: '" + properties + "' non è presente nel sistema, si verifichi il file di configurazione.");
		}
	}
	
	public synchronized String getKey(String uid,String ambito, String tipologia)throws Exception{
		String key = uid + ".1";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			String suffisso = "." + tipologia + "." + ambito;
			if(tipologia==null) suffisso = "." + ambito;
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql =PropertiesManager.getMyProperty("queryBase" + suffisso);
			checkSql(sql, "queryBase" + suffisso);

			String columnName = PropertiesManager.getMyProperty("colonna.5" + suffisso);
			//String max = "max(" + columnName + ")";
			String where = " AND "  + columnName + " LIKE ?";
			sql = sql.replace("*", columnName) + where;
			
			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, uid + "%");

			rs = ps.executeQuery();
			List<Integer> values = new ArrayList<Integer>();
			while(rs.next()){
				String currentId = rs.getString(1);
				String[] tokens = currentId.split("\\.");
				if(tokens.length>1){
					String lastToken = tokens[tokens.length-1];
					Integer value = Integer.parseInt(lastToken);
					values.add(value);
				}
			}
			
			if(!values.isEmpty()){
				Collections.sort(values);
				key = uid + "." + (values.get(values.size()-1) + 1);
			}
			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
		
		return key;
	}
	
	public void deleteCustom(Troika troika, String ambito, String tipologia)throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		try{
			String suffisso = "." + tipologia + "." + ambito;
			if(tipologia==null) suffisso = "." + ambito;
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();

			String sql =PropertiesManager.getMyProperty("deleteBase" + suffisso);
			checkSql(sql, "deleteBase" + suffisso);

			String id = troika.getID();
			String cond = PropertiesManager.getMyProperty("colonna.5" + suffisso) + "=?";
			sql = sql.replace("[COND]", cond);

			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, id);

			int res = ps.executeUpdate();
		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	}

	public void insertDescrizioneRecord(Troika troika, String ambito, String tipologia) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			String suffisso = "." + tipologia + "." + ambito;
			if(tipologia==null) suffisso = "." + ambito;
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();

			String sql =PropertiesManager.getMyProperty("insertBase" + suffisso);
			checkSql(sql, "insertBase" + suffisso);

			String columnNames = "";
			String columnValues = "";
			for(int i=0; i<NUM_OF_COL; i++){
				String colName = PropertiesManager.getMyProperty("colonna." + i + suffisso);
				columnNames = columnNames + colName + ", ";
				columnValues = columnValues + "?, ";
			}
			columnNames = columnNames + "]";
			columnNames = columnNames.replace(", ]", "");
			sql = sql.replace("[CAMPI]", " " + columnNames + " ");

			columnValues = columnValues + "]";
			columnValues = columnValues.replace(", ]", "");
			sql = sql.replace("[VALORI]", columnValues);
			
			ps = connection.prepareStatement(sql.trim());

			ps.setString(1, troika.getFirstValue());
			ps.setString(2, troika.getSecondValue());
			ps.setString(3, troika.getThirdValue());
			ps.setString(4, troika.getDescrizione());
			ps.setString(5, troika.getCustom());
			ps.setString(6, troika.getID());
			ps.setString(7, troika.getCodTripletta());
			ps.setString(8, troika.getAttiva());
			ps.setString(9, troika.getRisposta());
			ps.setString(10, troika.getCaratterizzazione());
			
			int res = ps.executeUpdate();

			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}

	}
	
	private String getUpdateSql(String suffisso, String[] colPropName, boolean withLike) throws Exception{
			String sql =PropertiesManager.getMyProperty("updateBase" + suffisso);
			checkSql(sql, "updateBase" + suffisso);

			String colName = "";
			for(int i=0; i<colPropName.length; i++){
				colName += PropertiesManager.getMyProperty(colPropName[i]) + "=?, ";
			}
			colName = (colName + "[]").replace(", []", "");
			
			sql = sql.replace("[CAMPI]", colName);
			
			String where = null;
			where = " WHERE " + PropertiesManager.getMyProperty("colonna.5"+ suffisso) + (withLike?" LIKE ?": "=?");

			sql = sql + where;
			return sql;
	}

	public void updateCampiRecord(Troika troika, String ambito, String tipologia) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			String suffisso = "." + tipologia + "." + ambito;
			if(tipologia==null) suffisso = "." + ambito;
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			
			String sql = getUpdateSql(suffisso, new String[]{"colonna.3"+ suffisso, "colonna.9"+ suffisso}, false);
			ps = connection.prepareStatement(sql.trim());
			
			ps.setString(1, troika.getDescrizione());
			ps.setString(2, troika.getCaratterizzazione());
			ps.setString(3, troika.getID());
			
			int res = ps.executeUpdate();
			
			if(troika.isFather()){
				sql =  getUpdateSql(suffisso, new String[]{"colonna.9"+ suffisso}, true);
				ps = connection.prepareStatement(sql.trim());
				ps.setString(1, troika.getCaratterizzazione());
				ps.setString(2, troika.getID()+".%");
				
				res = ps.executeUpdate();
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	}

	public void updateRispostaRecord(Troika troika, String ambito, String tipologia) throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			String suffisso = "." + tipologia + "." + ambito;
			if(tipologia==null) suffisso = "." + ambito;
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			
			String sql = getUpdateSql(suffisso, new String[]{"colonna.8"+ suffisso}, false);
			ps = connection.prepareStatement(sql.trim());
			
			ps.setString(1, troika.getRisposta());
			ps.setString(2, troika.getID());
			
			int res = ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	}

	public Collection<Troika> getAllColumnValues(Map<String, String> numColValue, String ambito, String tipologia) throws Exception{
		Collection<Troika> values = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			String suffisso = "." + tipologia + "." + ambito;
			if(tipologia==null) suffisso = "." + ambito;
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql =PropertiesManager.getMyProperty("queryBase" + suffisso);
			checkSql(sql, "queryBase" + suffisso);

			String columnNames = "";

			for(int i=0; i<NUM_OF_COL; i++){
				String colName = PropertiesManager.getMyProperty("colonna." + i + suffisso);
				columnNames = columnNames + colName + ", ";
			}
			columnNames = columnNames + "]";
			columnNames = columnNames.replace(", ]", "");
			sql = sql.replace("*", " " + columnNames + " ");
			

			if(numColValue!=null){
				for(String index: numColValue.keySet()){
					String colName = PropertiesManager.getMyProperty("colonna." + index + suffisso);
					sql = sql + " AND " + colName + "=? ";
				}
			}

			sql = sql + " ORDER BY " + PropertiesManager.getMyProperty("colonna.5"+ suffisso) + " ASC ";
			
			ps = connection.prepareStatement(sql.trim());
			
			int i = 0;
			if(numColValue!=null){
				for(String index: numColValue.keySet()){
					i++;
					ps.setString(i, numColValue.get(index));
				}
			}

			rs = ps.executeQuery();
			while(rs.next()){
				if(values == null) values = new ArrayList<Troika>();
				Troika troika = new Troika();
				troika.setFirstValue(rs.getString(1));
				troika.setSecondValue(rs.getString(2));
				troika.setThirdValue(rs.getString(3));
				troika.setDescrizione(rs.getString(4));
				troika.setCustom(rs.getString(5));
				troika.setID(rs.getString(6));
				troika.setCodTripletta(rs.getString(7));
				troika.setAttiva(rs.getString(8));
				troika.setRisposta(rs.getString(9)==null?"":rs.getString(9));
				troika.setCaratterizzazione(rs.getString(10));
				values.add(troika);
			}
			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}

		return values;
	}

	public Collection<String> getColumnValues(String[] troika, String ambito, String tipologia) throws Exception{
		return getColumnValues(troika, ambito, tipologia, COLUMN_FIRST_VALUE_TROIKA);
	}

	public Collection<String> getColumnValues(String[] troika, String ambito, String tipologia, String columnNumber) throws Exception{
		Collection<String> columnValues = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			String suffisso = "." + tipologia + "." + ambito;
			if(tipologia==null) suffisso = "." + ambito;

			ConnectionManager connectionManager = ConnectionManager.getInstance();
			DataSource ds = connectionManager.getDataSource();
			connection = ds.getConnection();
			String sql =PropertiesManager.getMyProperty("queryBase" + suffisso);
			checkSql(sql, "queryBase" + suffisso);
			String columnName = "";
			
			if(troika==null){
				/*Recupero tutte i valori che si riferiscono al primo elemento della tripletta
				 * dalla tabella che corrisponde alla Tipologia.
				 */
				columnName = PropertiesManager.getMyProperty("colonna." + columnNumber + suffisso);
			}else{
				for(int i=0; i<troika.length; i++){
					String colName = PropertiesManager.getMyProperty("colonna." + i + suffisso);
					sql = sql + " AND UPPER(" + colName + ")=UPPER(?) ";
				}
				columnName = PropertiesManager.getMyProperty("colonna." + troika.length + suffisso);
			}

			sql = sql + " ORDER BY " + columnName;
//			System.out.println("Eseguo la query: " + sql);
			ps = connection.prepareStatement(sql.trim());
			
			if(troika!=null){
				for(int i=0; i<troika.length; i++){
					ps.setString(i+1, troika[i].trim());
					//System.out.println(PropertiesManager.getMyProperty("colonna." + i + suffisso) + "= '" + troika[i] + "'");
				}
			}

			rs = ps.executeQuery();
			while(rs.next()){
				if(columnValues == null) columnValues = new ArrayList<String>();
				String value = rs.getString(columnName.trim());
				if(value!=null){
					value = value.trim();
					if(!columnValues.contains(value.toUpperCase())) columnValues.add(value.toUpperCase());
				}
			}
			
			//if(columnValues!=null) System.out.println("Sono state indivduate: " + columnValues.size() + " occorenze.");
			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
		
		return columnValues;
	}

}
