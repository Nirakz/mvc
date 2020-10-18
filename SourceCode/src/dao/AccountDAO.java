package dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Account;
import util.MySqlDataAccessHelper;

public class AccountDAO {
	private String connectionString;
	private String username;
	private String password;
	private Logger log;
	public AccountDAO(String c,String u,String p) {
		this.connectionString = c;
		this.username = u;
		this.password = p;
		this.log = LoggerFactory.getLogger(this.getClass());
	}
	/*public Account getUser (String name){
		Account user = null;
		String query = "select * from account  where username='" + name+"'";
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(connectionString,username,password);
		helper.open();
		ResultSet rs = helper.executeQuery(query);
		try {
			while (rs.next()) {
				user = new Account();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setAvatar(rs.getString("avatar"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		helper.close();
		return user;
	}*/
	public ArrayList<Account> getListAccount (){
		ArrayList<Account> list = new ArrayList<Account>();
		String query = "select id_account, username, password, avatar, full_name from Account " ;
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(connectionString,username,password);
		
		helper.open();
		ResultSet rs = helper.executeQuery(query);
		try {
			while (rs.next()) {
				Account user = new Account();
				user.setId(rs.getString("id_account"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setAvatar(rs.getString("avatar"));
				user.setFullName(rs.getString("full_name"));
				list.add(user);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			log.error(e.toString());
		} 
		helper.close();
		return list;
	}
	
	//get list of users ( not include password);
	public ArrayList<Account> getListAccountForAdmin (){
		ArrayList<Account> list = new ArrayList<Account>();
		String query = "select id_account, username, password, avatar, full_name from Account " ;
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(connectionString,username,password);
		
		helper.open();
		ResultSet rs = helper.executeQuery(query);
		try {
			while (rs.next()) {
				Account user = new Account();
				user.setId(rs.getString("id_account"));
				user.setUsername(rs.getString("username"));
				user.setAvatar(rs.getString("avatar"));
				user.setFullName(rs.getString("full_name"));
				list.add(user);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			log.error(e.toString());
		} 
		helper.close();
		return list;
	}
	//add row in table for an account
	public void addAccount( Account anAccount)
	{
		
		String sqlString = "INSERT INTO Account VALUES(?,?,?,?,?)";
		
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(connectionString,username,password);
		
		helper.open();
		
       PreparedStatement updateAccount= helper.createPrepareStatement(sqlString);
		
		if (updateAccount!=null){
			try {
				updateAccount.setString(1, anAccount.getId());
				updateAccount.setString(2, anAccount.getUsername());
				updateAccount.setString(3, anAccount.getPassword());
				updateAccount.setString(4, anAccount.getAvatar());
				updateAccount.setString(5, anAccount.getFullName());
				
				updateAccount.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		helper.close();
		
	}
	
	public void updateAccount( Account anAccount)
	{
		
		String sqlString = "UPDATE account SET " 
		         +"username = ?,"
		         +"avatar = ?,"
		         +"full_name = ? "
		         +"WHERE id_account = ?";
		
		System.out.println("update string "+sqlString);
		
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(connectionString,username,password);	
		
		helper.open();
		
		PreparedStatement updateAccount= helper.createPrepareStatement(sqlString);
		
		if (updateAccount!=null){
			try {
				updateAccount.setString(1, anAccount.getUsername());
				updateAccount.setString(2, anAccount.getAvatar());
				updateAccount.setString(3, anAccount.getFullName());
				updateAccount.setString(4, anAccount.getId());
				
				updateAccount.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
	
		helper.close();
		
	}
	
	public void deleteAccount (String accountId)
	{
		//first delete all messages related to this account
		String deleteMessageFrom = "DELETE FROM message WHERE id_from=?";
		
		String deleteMessageTo = "DELETE FROM message WHERE id_to=?";
		
		String sqlString = "DELETE FROM account WHERE id_account=?";
		
		System.out.println("delete string "+sqlString);
		
		MySqlDataAccessHelper helper = new MySqlDataAccessHelper(connectionString,username,password);
		
		helper.open();

		PreparedStatement updateAccount= helper.createPrepareStatement(deleteMessageFrom);
		
		try {
			if (updateAccount!=null){
					updateAccount.setString(1, accountId);
					updateAccount.executeUpdate();
			}
			
			updateAccount= helper.createPrepareStatement(deleteMessageTo);
			
			if (updateAccount!=null){
					updateAccount.setString(1, accountId);
					updateAccount.executeUpdate();
			}
			
			updateAccount= helper.createPrepareStatement(sqlString);
			
			if (updateAccount!=null){
					updateAccount.setString(1, accountId);
					updateAccount.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		helper.close();
	}

}
