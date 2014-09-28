package com.poopie.fonely;

import com.jcraft.jsch.*;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class DatabaseTest {

	public static Connection startConnection(){
		try {
			 String driverName = "com.mysql.jdbc.Driver";
			    Class.forName(driverName);
			    String serverName = "localhost";
			    String mydatabase = "HackUMBC";
			    String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 

			    String username = "root";
			    String password = "password";
			    Connection conn = (Connection) DriverManager.getConnection(url, username, password);

	            return conn;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	            
	        }

	}
	
	public static void connectToServer(){
		try{
		      JSch jsch=new JSch();
		 
		      Session session=jsch.getSession("chris", "96.241.159.4");
		      session.setPassword("hotrod");
		      session.connect();
		      
		      
		        
	} catch(Exception e){
		}
	}
}
