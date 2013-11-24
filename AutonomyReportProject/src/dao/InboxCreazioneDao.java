package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import model.InboxCreazione;

public class InboxCreazioneDao extends AbstractDao {
	private String SELECT = "SELECT * FROM Inbox_Creazione";

	public InboxCreazioneDao(Connection connection) {
		super();
		this.connection = connection;
	}

	public Collection<InboxCreazione> getValues(InboxCreazione inboxCreazione)throws Exception{
		Collection<InboxCreazione> result = new ArrayList<InboxCreazione>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			logger.debug("----------------------------------------------------------------------");
			String sql = SELECT + " WHERE MERCATO=? OR MERCATO IS NULL ORDER BY ID";
			logger.debug(sql);
			String val = "["+ inboxCreazione.getMercato() + "]";
			logger.debug(val);

			ps = connection.prepareStatement(sql);
			ps.setString(1, inboxCreazione.getMercato());
			rs = ps.executeQuery();
			
			while(rs.next()){
				InboxCreazione current = new InboxCreazione();
				current.setID(rs.getString("ID"));
				current.setMercato(rs.getString("MERCATO"));
				current.setDescrizione(rs.getString("DESCRIZIONE"));
				result.add(current);
			}
			
			logger.debug("----------------------------------------------------------------------");

		}catch (Exception e) {
			throw e;
		}finally{
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
		}
		return result;
	}

}
