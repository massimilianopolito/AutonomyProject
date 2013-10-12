package dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import utility.PropertiesManager;
import model.HTAciResponseObject;

import com.autonomy.aci.AciResponse;

public class HTAciResponseManagerDao extends AbstractDao {
	private String SELECT_BY_ID= 	"SELECT * FROM HT_AciResponseQuery WHERE IdQuery =?";
	private String SELECT_DATE= 	"SELECT DISTINCT DataElaborazione FROM HT_AciResponseQuery ORDER BY DataElaborazione ASC";
//	private String UPDATE_BY_ID=	"UPDATE HT_AciResponseQuery SET IdQuery=?, AciResponse=?, DataElaborazione=? WHERE ID = ?";
	private String INSERT=			"INSERT INTO HT_AciResponseQuery(IdQuery, AciResponse, DataElaborazione) VALUES (?, ?, ?)";
	private String DELETE_OLDER=	"DELETE FROM HT_AciResponseQuery WHERE DataElaborazione<DATE_SUB(?, INTERVAL [DAY] DAY)";
	private String SELECT_OLDER=	"SELECT DISTINCT DataElaborazione FROM HT_AciResponseQuery WHERE DataElaborazione<DATE_SUB(?, INTERVAL [DAY] DAY)";
	private String PREVIOUS_DATE = 	"SELECT MAX(DataElaborazione) as PD FROM HT_AciResponseQuery WHERE DataElaborazione<?";

	public HTAciResponseManagerDao(Connection connection) {
		super();
		this.connection = connection;
	}

	public Timestamp getPreviousDate(Timestamp dataElaborazione)throws Exception{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Timestamp ret = null;
		try{
			String sql = PREVIOUS_DATE;
			
			ps = connection.prepareStatement(sql.trim());
			ps.setTimestamp(1, dataElaborazione);
			rs = ps.executeQuery();
			
			if(rs.next()) ret = rs.getTimestamp("PD");
		}catch (Exception e) {
			throw e;
		}finally{
			if(ps!=null) ps.close();
		}
		return ret;
		
	}

	
	public Collection<Timestamp> deleteOlder(HTAciResponseObject aciResponseObject)throws Exception{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Collection<Timestamp> ret = new ArrayList<Timestamp>();
		try{
			String dayDelay = PropertiesManager.getMyProperty("hotTopics.historic.day");
			if(dayDelay==null || dayDelay.isEmpty()) dayDelay = "8";
			
			String sql = SELECT_OLDER.replace("[DAY]", dayDelay);
			ps = connection.prepareStatement(sql.trim());
			ps.setTimestamp(1, aciResponseObject.getDataElaborazione());
			rs = ps.executeQuery();
			while(rs.next()){
				ret.add(rs.getTimestamp("DataElaborazione"));
			}

			sql = DELETE_OLDER.replace("[DAY]", dayDelay);
			ps = connection.prepareStatement(sql.trim());
			ps.setTimestamp(1, aciResponseObject.getDataElaborazione());
			int i = ps.executeUpdate();

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		return ret;
		
	}

	public Collection<Timestamp> getDate() throws Exception{

		Statement ps = null;
		ResultSet rs = null;
		Collection<Timestamp> ret = new ArrayList<Timestamp>();
		try{
			String sql = SELECT_DATE;
			
			ps = connection.createStatement();
	        rs=ps.executeQuery(sql);

	        while(rs.next()){
	        	ret.add(rs.getTimestamp("DataElaborazione"));
	        }

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}

		return ret;
			
	}

	public HTAciResponseObject getAciResponse(HTAciResponseObject aciResponseObject) throws Exception{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql = SELECT_BY_ID;
			
			ps = connection.prepareStatement(sql.trim());
	        ps.setLong(1, Long.parseLong(aciResponseObject.getID()));
	        rs=ps.executeQuery();

	        if(rs.next()){
	            ByteArrayInputStream bais;
	            ObjectInputStream ins;
	            try {
	            	bais = new ByteArrayInputStream(rs.getBytes("AciResponse"));
	            	ins = new ObjectInputStream(bais);
	            	AciResponse resp =(AciResponse)ins.readObject();
	            	ins.close();
	            	aciResponseObject.setAciResponse(resp);
	            }
	            catch (Exception e) {
	            	e.printStackTrace();
	            }
	            aciResponseObject.setIdQuery(rs.getString("IdQuery"));
	            aciResponseObject.setDataElaborazione(rs.getTimestamp("DataElaborazione"));
	        }

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}

		return aciResponseObject;
	}

	public String manageAciResponse(HTAciResponseObject aciResponseObject) throws Exception{

		PreparedStatement ps = null;
		ResultSet rs = null;
		String ID = null;
		System.out.println("Dentro a ManageAciResponse");
		try{
			String sql = INSERT;
//			ID = getId(connection, "HT_AciResponseQuery");
//			aciResponseObject.setID(ID);
			
			ps = connection.prepareStatement(sql.trim(), Statement.RETURN_GENERATED_KEYS);

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(bos);
	        System.out.println("scrittura objectOutputStream");
	        oos.writeObject(aciResponseObject.getAciResponse());
	        oos.flush();
	        oos.close();
	        bos.close();
	        System.out.println("inizio creazione byteArray");
	        byte[] data = bos.toByteArray();
	        System.out.println("fine creazione byteArray");

	        ps.setInt(1,Integer.parseInt(aciResponseObject.getIdQuery()));
	        ps.setObject(2, data);
	        ps.setTimestamp(3, aciResponseObject.getDataElaborazione());
			
			int i = ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			if(rs.next()) ID = rs.getString(1);

			
		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
	
		return ID;
	}

}
