package com.amit.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amit.model.MessageResource;
import com.amit.util.DBUtil;

/**
 * @author amit
 * Implementation class for message dao
 */
public class MessageDaoImpl implements MessageDao {

	private static Log logger = LogFactory.getLog(MessageDaoImpl.class);
	/**
	 * @author amit
	 * @return message list
	 * @throws SQLException 
	 * Get all messages from DB.
	 */
	@Override
	public List<MessageResource> getMessages() throws SQLException {
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from message");
		List<MessageResource> messageList = new ArrayList<MessageResource>();
		while (resultSet.next()) {
			MessageResource messageResource = new MessageResource();
			messageResource.setId(resultSet.getInt(1));
			messageResource.setMessage(resultSet.getString(2));
			messageResource.setAuthor(resultSet.getString(3));
			messageResource.setPostedDate(resultSet.getTimestamp(4));
			messageList.add(messageResource);
		}
		logger.debug("Retrieved messages from DB successfully");
		return messageList;
	}

	/**
	 * @author amit
	 * @param id
	 * @return message
	 * @throws SQLException 
	 * Get message based on message id
	 */
	@Override
	public MessageResource getMessage(int id) throws SQLException {
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from message where id='" + id + "'");
		MessageResource messageResource = null;
		while (resultSet.next()) {
			messageResource = new MessageResource();
			messageResource.setId(resultSet.getInt(1));
			messageResource.setMessage(resultSet.getString(2));
			messageResource.setAuthor(resultSet.getString(3));
			messageResource.setPostedDate(resultSet.getTimestamp(4));
		}
		logger.debug("Retrieved messages from DB based on id");
		return messageResource;
	}

}
