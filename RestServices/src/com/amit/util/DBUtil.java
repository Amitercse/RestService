package com.amit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author amit
 * DB util class to get DB connection.
 * This is singleton class to create only one instance of DB connection.
 * Not putting extra restrictions here to avoid violation of singleton 
 * property because it is being used only for test application.
 */
public class DBUtil {

	private static Connection connection;
	private static Log logger = LogFactory.getLog(DBUtil.class);
	
	/**
	 * Private constructor to avoid instance creation of util class.
	 */
	private DBUtil()
	{
		
	}
	
	/**
	 * Method to get DB connection. Since it is singleton class so, 
	 * keeping method as static and creating connection only if it 
	 * is not already available.
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		if (connection == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "root");
			} catch (ClassNotFoundException e) {
				logger.error("Error while loading driver class");
			} catch (SQLException e) {
				logger.error("Error while trying to get DB connection");
				throw new SQLException("Not able to get the connection");
			}
		}
		return connection;
	}
	
}
