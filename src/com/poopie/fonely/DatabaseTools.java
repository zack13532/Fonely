package com.poopie.fonely;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class DatabaseTools {

	/**Starts a database connection to localhost: use after connecting to SSH.
	 * Close the connection when you are done with it.
	 * @return Returns a Database Connection reference to call queries and instructions on. 
	 */
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
	
	/**Checks if the given game is over. (Checked by seeing if the current round count surpasses the maximum round count.
	 * @throws Exception 
	 * @param gameId Represents the ID of the Game in the database.
	 * @return boolean Indicating if the Game is over.
	 */
	public static boolean isGameOver(int gameId) throws Exception{
		Connection conn = startConnection();
		String query = "SELECT * FROM Games WHERE game_id = ?";
		PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(query);
		stmt.setInt(1, gameId);
		ResultSet set = stmt.executeQuery();
		
		if (set == null){
			stmt.close();
			conn.close();
			throw new Exception();
		}
		set.next();
		boolean returned = false;
		int roundNo = set.getInt("round_no");
		int roundMax = set.getInt("round_max");
		
		if (roundNo > roundMax)
			returned = true;
		
		set.close();
		stmt.close();
		conn.close();
		return returned;
		
	}
	
	/**Call this method if the game is over. 
	 * This is used for generating the contents of the ending screen for a game.
	 * 
	 * @return Returns an ArrayList containing contents of all of the rounds for a specific game. ("Round_No + \n + user + \n + text")
	 * @param gameId An integer representing the game in the database.
	 * @throws SQLException 
	 *  
	 */
	public static ArrayList<String> retrieveRoundsForGame(int gameId) throws SQLException{
		Connection conn = startConnection();
		String query = "SELECT * FROM Rounds WHERE game_id = ?";
		PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(query);
		stmt.setInt(1, gameId);
		ResultSet set = stmt.executeQuery();
		ArrayList<String> returned = new ArrayList<String>();
		while (set.next()){
			returned.add(set.getString("round_no") + "\n" + set.getString("user") + "\n" + set.getString("text"));
		}
		
		set.close();
		stmt.close();
		conn.close();
		return returned;
		
	}
	
	/**Updates the Users table in the database once a Game has been completed.
	 * Provides any notifications in case that is needed.
	 * @param gameId The gameId of the Game that is completed.
	 * @throws Exception 
	 */
	public static void updateUsers(int gameId) throws Exception{
		
		Connection conn = startConnection();
		String query = "SELECT * FROM Games WHERE game_id = ?";
		PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(query);
		stmt.setInt(1, gameId);
		ResultSet set = stmt.executeQuery();
		
		if (set == null){
			stmt.close();
			conn.close();
			throw new Exception();
		}
		
		set.next();
		
		String userText = set.getString("user_list");
		String[] userArray = userText.split("\n");
		int len = userArray.length;
		int i = 0;
		set.close();
		
		while (i < len){
			query = "SELECT * FROM Users WHERE user = ?";
			stmt = (PreparedStatement) conn.prepareStatement(query);
			stmt.setString(1, userArray[i]);
			set = stmt.executeQuery();
			set.next();
			String gamesIn = set.getString("games_in");
			gamesIn = gamesIn.replaceFirst(gameId + "\n", "");
			String gamesOver = set.getString("games_out");
			gamesOver += gameId + "\n";
			
			int truncate = gamesOver.indexOf("\n");
			if (truncate != gamesOver.length() - 1)
				gamesOver = gamesOver.substring(truncate + 1);
			
			int notif = set.getInt("notifications");
			notif++;
			query = "UPDATE Users SET games_in = ?, games_over = ?, notfications =? where user = ?";
			set.close();
			stmt = (PreparedStatement) conn.prepareStatement(query);
			stmt.setString(1, gamesIn);
			stmt.setString(2, gamesOver);
			stmt.setInt(3, notif);
			stmt.setString(4, userArray[i]);
			stmt.execute();
			stmt.close();
			conn.close();
			return;
		}
		
	}
	
	/**Connect to a server using SSH.
	 * Close the session using Session.disconnect().
	 * @return Returns a SSH session based on the provided credentials.
	 */
	public static Session connectToServer(){
		try{
		      JSch jsch=new JSch();
		 
		      Session session=jsch.getSession("chris", "96.241.159.4");
		      session.setPassword("hotrod");
		      session.connect();
		      return session;

		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	/**Call this whenever the user reaches the main page.
	 * This tells how many Games have been completed since last check.
	 * @return Number of new finished games.
	 * @param The user to check in the database
	 * @throws SQLException 
	 */
	public static int notificationsCheck(String user) throws SQLException{
		Connection conn = startConnection();
		
		String query = "SELECT * from Users where user = ?";
		PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(query);
		stmt.setString(1, user);
		ResultSet set = stmt.executeQuery();
		
		set.next();
		
		int returned = set.getInt("notifications");
		set.close();
		stmt.close();
		conn.close();
		return returned;
	}
	
	/**Run this method whenever the user clicks the "Check Notifications" button.
	 * This removes all notifications.
	 * @param user
	 * @throws SQLException
	 */
	public static void resetNotifications(String user) throws SQLException{
		Connection conn = startConnection();
		
		String query = "UPDATE Users SET notifications = ? where user = ?";
		PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(query);
		
		stmt.setInt(1, 0);
		stmt.setString(2, user);
		stmt.execute();
		stmt.close();
		conn.close();
		
	}
	

}
