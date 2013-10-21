package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import utility.ConnectionManager;

public class UserDao extends AbstractDao {
	private String UPDATE_PWD = 		"UPDATE user_auth SET PASSWORD=? WHERE USERNAME=?";
	private String SELECT = 			"SELECT * FROM user_auth";
	private String SELECT_BY_USER = 	"SELECT * FROM user_auth WHERE USERNAME=?";

	public void changePwd(String username, String pwd)throws Exception{
		Connection connection = null;
		PreparedStatement ps = null;
		try{
			ConnectionManager connectionManager = ConnectionManager.getInstance();
			connection = connectionManager.getConnection(true);

			String sql = UPDATE_PWD;
			logger.debug("Eseguo: " + sql);

			ps = connection.prepareStatement(sql.trim());
			ps.setString(1, pwd);
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
