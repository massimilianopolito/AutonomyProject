package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import utility.DateConverter;

public class ManageTableDao extends AbstractDao {
	private String CREATE_TABLE = 	"CREATE TABLE [TBNM_NEW] LIKE [TBNM_OLD]";
	private String SWITCH_TABLE = 	"RENAME TABLE [TBNM] TO [TBNM_STORY], [TBNM_NEW] TO [TBNM]";
	private String RENAME_TABLE = 	"ALTER TABLE [TBNM_NEW] RENAME [TBNM]";
	private String DROP_TABLE = 	"DROP TABLE [TBNM]";
	private String TABLE_NAME =		"SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_name LIKE ?";

	public ManageTableDao(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public Collection<String> getNomiTabelle(String base)throws Exception{
		Collection<String> nomi = new ArrayList<String>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = TABLE_NAME;
			ps = connection.prepareStatement(sql);
			ps.setString(1, base);
			rs = ps.executeQuery();
			
			while(rs.next()){
				String tableName = rs.getString("table_name");
				nomi.add(tableName);
			}
			
							
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
			
		return nomi;
	}
	
	public void createTableLike(String newName, String existName)throws Exception{
		Statement s = null;
		String sql = "";
		try{
			sql = CREATE_TABLE.replace("[TBNM_NEW]", newName).replace("[TBNM_OLD]", existName);
			s = connection.createStatement();
			s.execute(sql);
		}catch (Exception e) {
			throw e;
		}finally{
			if(s!=null) s.close();
		}
	}
	
	public void moveTable(String name, String newName, String historicName)throws Exception{
		Statement s = null;
		String sql = "";
		try{
			s = connection.createStatement();
			if(historicName!=null){
				sql = SWITCH_TABLE.replace("[TBNM_NEW]", newName)
						  .replace("[TBNM]", name)
						  .replace("[TBNM_STORY]", historicName);
				
				s.execute(sql);
			}else{
				sql = DROP_TABLE.replace("[TBNM]", name); 
				s.execute(sql);

				sql = RENAME_TABLE.replace("[TBNM_NEW]", newName).replace("[TBNM]", name);
				s.execute(sql);
			}
			
		}catch (Exception e) {
			throw e;
		}finally{
			if(s!=null) s.close();
		}
		
	}
	

	public void dropTable(String name)throws Exception{
		Statement s = null;
		String sql = "";
		try{
			sql = DROP_TABLE.replace("[TBNM]", name);
			s = connection.createStatement();
			s.execute(sql);
			
		}catch (Exception e) {
			throw e;
		}finally{
			if(s!=null) s.close();
		}
		
	}

}
