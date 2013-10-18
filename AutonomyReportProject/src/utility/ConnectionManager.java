package utility;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionManager {

	private static ConnectionManager connectionManager;
	private DataSource ds;
	private DataSource dsPenthao;
	private DataSource dsPenthaoCorp;
	//private DataSource dsStruttura;
//	private Connection connection;
	
	private void loadDriverClass(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	 
	}

	private ConnectionManager() {
	  super();
	  try {
		Context ctx = new InitialContext();
		ds = (DataSource)ctx.lookup("java:comp/env/jdbc/report");
		dsPenthao = (DataSource)ctx.lookup("java:comp/env/jdbc/penthao");
		loadDriverClass();
		//dsPenthaoCorp = (DataSource)ctx.lookup("java:comp/env/jdbc/penthaoCorp");
		//dsStruttura = (DataSource)ctx.lookup("java:comp/env/jdbc/struttura");
		
	  } catch (NamingException e) {
		e.printStackTrace();
	  }	
	}
	
	public static ConnectionManager getInstance(){
		if(connectionManager==null) connectionManager = new ConnectionManager();
		return connectionManager;
	}

	public DataSource getDataSource(){
		return ds;
	}

	public DataSource getDataSourcePenthao() {
		DataSource currentDs = dsPenthao;
		return currentDs;
	}
	
	/*public DataSource getDataSourceStruttura() {
		return dsStruttura;
	}*/
	
	public Connection getConnection(boolean autoCommit) throws Exception{
		Connection connection = getDataSource().getConnection();
		connection.setAutoCommit(autoCommit);
		return connection;
	}

	public Connection getConnectionFromDriverManager(boolean autoCommit) throws Exception{
		String dbConnectionString = PropertiesManager.getMyProperty("dbName");
		String user =  PropertiesManager.getMyProperty("dbUser");
		String pwd =  PropertiesManager.getMyProperty("dbPwd");
		Connection connection = DriverManager.getConnection(dbConnectionString,user, pwd);
		connection.setAutoCommit(autoCommit);
		return connection;
	}

	public void closeConnection(Connection connection) throws Exception{
		if(connection!=null){
			connection.close();
			connection = null;
		}
	}

	public void commit(Connection connection){
		try{
			if(connection!=null && !connection.getAutoCommit()){
				connection.commit();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rollBack(Connection connection){
		try{
			if(connection!=null && !connection.getAutoCommit()){
				connection.rollback();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
