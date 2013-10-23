package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import utility.ConnectionManager;

public class UserDao extends AbstractDao {
	private String UPDATE_PWD = 				"UPDATE user_auth SET PASSWORD=? WHERE USERNAME=?";
	private String SELECT = 					"SELECT * FROM user_auth";
	private String SELECT_BY_USER = 			"SELECT * FROM user_auth WHERE USERNAME=?";
	private String AUTHCOMBO_BY_PROFILE =   	"SELECT * FROM ProfiloCombo WHERE PROFILO=?";
	
	public Map<String, Collection<String>> getAuthComboByProfile(String userProfile)throws Exception{
		Map<String, Collection<String>> authByCombo = new HashMap<String, Collection<String>>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			connection = connectionManager.getConnection(true);
			
			String sql = AUTHCOMBO_BY_PROFILE;
			logger.debug("Eseguo: " + sql);

			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, userProfile);
			
			rs = ps.executeQuery();
			while(rs.next()){
				String nomeCombo = rs.getString("NOMECOMBO");
				String valoreCombo = rs.getString("VALORECOMBO");
				
				if(!authByCombo.containsKey(nomeCombo)){
					authByCombo.put(nomeCombo, new ArrayList<String>());
				}
				
				authByCombo.get(nomeCombo).add(valoreCombo);
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
		return authByCombo;

	}

	public String getMyProfile(String userName)throws Exception{
		String profile = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			connection = connectionManager.getConnection(true);
			
			String sql = SELECT_BY_USER;
			logger.debug("Eseguo: " + sql);

			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, userName);
			
			rs = ps.executeQuery();
			if(rs.next()){
				profile = rs.getString("PROFILO");
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
		return profile;
	}
	
	public boolean isPwdCorrect(String userName, String pwd)throws Exception{
		boolean isCorrect = true;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			connection = connectionManager.getConnection(true);
			
			String sql = SELECT_BY_USER;
			logger.debug("Eseguo: " + sql);

			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, userName);
			
			rs = ps.executeQuery();
			if(rs.next()){
				if(!rs.getString("PASSWORD").equalsIgnoreCase(pwd)) isCorrect = false;
			}

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
		return isCorrect;
	}

	public void changePwd(String username, String newPwd)throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			connection = connectionManager.getConnection(true);
			
			String sql = UPDATE_PWD;
			logger.debug("Eseguo: " + sql);

			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, newPwd);
			ps.setString(2, username);

			int res = ps.executeUpdate();
		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
			if(connection !=null) connection.close();
		}
	}
}
