package com.gcm.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnector {
	private static DataBaseConnector connector;

	private DataBaseConnector() {
		// TODO Auto-generated constructor stub
	}
	public static DataBaseConnector getDataBaseConnector(){
		if(connector==null)
			connector=new DataBaseConnector();
		return connector;
	}

	private Connection getDataBaseConnection(){
		Connection conn=null;
		try{
			String myDriver = "com.mysql.jdbc.Driver";
			String myUrl = "jdbc:mysql://localhost:3306/GCM";
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, "root", "majoka");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Exception is :"+e);
		}
		return conn;
	}
	public boolean insert(String mobile_number,String regId){
		try{
			Connection con=getDataBaseConnection();
			String instetString="insert into Registered_Id(mobile_number,registered_id) values(?,?);";
			PreparedStatement ps = con.prepareStatement(instetString);  
			ps.setString(1, String.valueOf(mobile_number));  
			ps.setString(2, String.valueOf(regId)); 
			ps.executeUpdate();  
			ps.close();  
			System.out.println("Storing Mobile Number and RegId is Done!");  
			return true;
		}catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Exception is :"+e);
			e.printStackTrace();
			return false;
		}

	}
	public List<String> getRegId(String mobile_number){
		try{
			Connection con=getDataBaseConnection();
			String select="select * from Registered_Id where mobile_number=?";
			PreparedStatement ps = con.prepareStatement(select);  
			ps.setString(1, mobile_number);
			ResultSet rs=ps.executeQuery();
			rs.beforeFirst();
			ArrayList<String> list=new ArrayList<String>();
			while(rs.next()){
				String str=rs.getString("registered_id");
				list.add(str);
			}
			return list;
		}catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Exception is :"+e);
			e.printStackTrace();
			return null;
		}
	}
}
