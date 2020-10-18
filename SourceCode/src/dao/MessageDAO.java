package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Message;
import pojo.Notification;
import util.MySqlDataAccessHelper;
import util.ValueHelper;

public class MessageDAO {
	String connectionString;
	String username;
	String password;
	private Logger log;

	public MessageDAO(String c, String u, String p) {
		this.connectionString = c;
		this.username = u;
		this.password = p;
		this.log = LoggerFactory.getLogger(this.getClass());
	}

	public boolean storeHistory(ArrayList<Message> listMessage) {
		boolean success = true;
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(
				connectionString, username, password);
		helper.open();
		String query = "INSERT INTO message (id_from,id_to,type,time,content,offline) VALUES "
				+ "(?, ?, ?, ?, ?, ?)";
		PreparedStatement prepareStatement;		
			try {
				prepareStatement = helper.getConnection()
						.prepareStatement(query);
				for (Message msg : listMessage) {
					prepareStatement.setString(1, msg.getIdFrom());
					prepareStatement.setString(2, msg.getIdTo());
					prepareStatement.setInt(3, msg.getType());
					prepareStatement.setTimestamp(4, (Timestamp) msg.getTime());
					prepareStatement.setString(5, msg.getContent());
					prepareStatement.setBoolean(6, msg.isOffline());
					if (prepareStatement.executeUpdate() == 0) {
						success = false; 
						break;
					}
					
				}			
			} catch (SQLException e) {
				log.error(e.toString());
				success = false;
				
			}
		helper.close();
		return success;
	}

	public ArrayList<Message> getHistory(String idFrom, String idTo, int index) {
		ArrayList<Message> list = new ArrayList<Message>();	
		String query = "SELECT id_message, id_from, id_to, time, content, type, offline " +
				"FROM message WHERE ((id_from=?  and id_to=?) or (id_from=? and id_to=?)) " +
				"and offline=FALSE " +
				"and id_message < ? " +
				"ORDER BY id_message desc " +
				"LIMIT ?"; 
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(
				connectionString, username, password);
		helper.open();
		try {
			PreparedStatement ps = helper.getConnection().prepareStatement(query);
			ps.setString(1, idFrom);
			ps.setString(2, idTo);
			ps.setString(3, idTo);
			ps.setString(4, idFrom);
			if (index == 0){
				ps.setInt(5, Integer.MAX_VALUE);
			} else {
				ps.setInt(5, index);
			}
			ps.setInt(6, ValueHelper.LIMIT_ROW_HISTORY);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Message msg = new Message();
				msg.setId(rs.getInt("id_message"));
				msg.setIdFrom(rs.getString("id_from"));
				msg.setIdTo(rs.getString("id_to"));
				msg.setTime(rs.getTimestamp("time"));
				msg.setContent(rs.getString("content"));
				msg.setType(rs.getInt("type"));
				msg.setOffline(rs.getBoolean("offline"));
				list.add(msg);
			}
		} catch (SQLException e) {
			 log.error(e.toString());
		}
		helper.close();
		return list;
	}

	public ArrayList<Message> getOfflineMessage(String idFrom, String idTo) {
		ArrayList<Message> list = new ArrayList<Message>();
		String query = "SELECT id_message, id_from, id_to, time, content, type, offline FROM message WHERE (id_from='"
				+ idFrom + "' and id_to='" + idTo + "') and offline=TRUE";
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(
				connectionString, username, password);
		helper.open();
		ResultSet rs = helper.executeQuery(query);
		try {
			while (rs.next()) {
				Message msg = new Message();
				msg.setId(rs.getInt("id_message"));
				msg.setIdFrom(rs.getString("id_from"));
				msg.setIdTo(rs.getString("id_to"));
				msg.setTime(rs.getTimestamp("time"));
				msg.setContent(rs.getString("content"));
				msg.setType(rs.getInt("type"));
				msg.setOffline(rs.getBoolean("offline"));
				list.add(msg);
			}
		} catch (SQLException e) {
			log.error(e.toString());
		}
		helper.close();
		return list;
	}

	public boolean updateOfflineMessage(int[] arrayID) {
		boolean success = true;
		String query = "UPDATE message SET offline=FALSE  WHERE id_message=?"; 
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(
				connectionString, username, password);
		helper.open();
		
		
			try {
				PreparedStatement prepareStatement =  helper.getConnection().prepareStatement(query);
				for (int i : arrayID) {
					prepareStatement.setInt(1, i);
					if (prepareStatement.executeUpdate() == 0){
						success = false;
						break;
					}
				}
			} catch (SQLException e) {
				log.error(e.toString());
				success = false;
			}
			
		
		helper.close();
		return success;
	}

	public ArrayList<Notification> getNotification(String id) {
		ArrayList<Notification> list = new ArrayList<Notification>();
		String query = "SELECT id_from,count(id_from) as count "
				+ "FROM message WHERE id_to = '" + id
				+ "' and offline=TRUE GROUP BY id_from";
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(
				connectionString, username, password);
		helper.open();
		ResultSet rs = helper.executeQuery(query);
		try {
			while (rs.next()) {
				Notification noti = new Notification();
				noti.setIdFrom(rs.getString("id_from"));
				noti.setCount(rs.getInt("count"));
				list.add(noti);
			}
		} catch (SQLException e) {
			log.error(e.toString());
		}
		helper.close();
		return list;
	}
}
