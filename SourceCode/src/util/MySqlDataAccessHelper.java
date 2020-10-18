
package util;

import java.sql.*;



public class MySqlDataAccessHelper  {
	String connectionString;
	String username;
	String password;
  
   private Connection connection;

   public MySqlDataAccessHelper(String connect,String username,String password) {
	   this.connectionString = connect;
	   this.username = username;
	   this.password = password;
}
   public Connection getConnection() {
      return connection;
   }


   public void open() {
      try {
    	 Class.forName("com.mysql.jdbc.Driver");
        // String connectionString = "jdbc:mysql://localhost:3306/group_chat";
         this.connection = DriverManager.getConnection(connectionString,username, password);
      } catch (SQLException ex) {
         System.out.println(ex.getMessage());
      } catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
   }

  
   public void close() {
      try {
         this.connection.close();
      } catch (SQLException ex) {
         ex.printStackTrace();
      }
   }

  public PreparedStatement createPrepareStatement(String statement)
  {
	  try {
		return this.connection.prepareStatement(statement);
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
  }
   public ResultSet executeQuery(String sql) {
      ResultSet rs = null;
      try {
         Statement sm = this.connection.createStatement();
         rs = sm.executeQuery(sql);
      } catch (SQLException ex) {
         System.out.println(ex.getMessage());
      }
      return rs;
   }

 
   public int executeUpdate(String sql) {
      int num = -1;
      try {
         Statement sm = this.connection.createStatement();

         num = sm.executeUpdate(sql);
      } catch (SQLException ex) {
        System.out.println(ex.getMessage());
      }
      return num;
   }
   
   
}
