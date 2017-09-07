package fiberMonitor.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBConnection {
	 private final static String url="jdbc:mysql://localhost:3306/forms";
	 private final static String user="root";
	 private final static String password="325413";
	 private final static String dbDriver="com.mysql.jdbc.Driver";
	 private static Connection con=null;
	 static{
		 try{
			 Class.forName(dbDriver).newInstance();
		 }catch (Exception e){
			 }
	   }
	 //
	 public static boolean creatConnection(){
		 try{
			 con=DriverManager.getConnection(url,user,password);
			 con.setAutoCommit(true);
		 }catch (SQLException e){
			 //System.out.println(e);
			 return false;
		 }
		 return true;
	}
	 //
	 public boolean executeUpdate(String sql){
		 if(con==null){
			 creatConnection();
		 }
	      try{
	    	  java.sql.Statement stmt=con.createStatement();
	    	  stmt.executeUpdate(sql);//
	    	  //System.out.println(String.valueOf(iCount));
	    	  return true;
	      } catch(SQLException e){
	    	  //System.out.println(e);
	    	  return false;
	      }finally{
	    	    if (con != null) {
	                   try {
						con.close();//
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                   con = null;

	                  }
	      }
	 
	 }
	 //
	 public static ResultSet executeQuery(String sql){
		 ResultSet rs;
		 try{
			 if(con==null){
				creatConnection(); 
			 }
			 java.sql.Statement stmt=con.createStatement();
			 try{
				 //
				 rs=stmt.executeQuery(sql);
			 }catch (SQLException e){
				 //System.out.println("1"+e);
				 return null;
			 }
		 }catch(SQLException e){
			 //System.out.println("2"+e);
			// e.printStackTrace();
			 return null;
		 }
		 return rs;
	 }
	 

}
