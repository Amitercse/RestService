package com.amit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
	 * @param url
	 * @return message list
	 * @throws SQLException 
	 * Get all messages from DB.
	 */
	@Override
	public List<MessageResource> getMessages(String url) throws SQLException {
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from message");
		List<MessageResource> messageList = new ArrayList<>();
		while (resultSet.next()) {
			MessageResource messageResource = new MessageResource();
			messageResource.setId(resultSet.getInt(1));
			messageResource.setMessage(resultSet.getString(2));
			messageResource.setAuthor(resultSet.getString(3));
			messageResource.setPostedDate(resultSet.getTimestamp(4));
			messageResource.addLink(url + resultSet.getInt(1), "self");
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

	/**
	 * @author amit
	 * @param messageResource
	 * @return
	 * @throws SQLException
	 * Insert a new record in message table. 
	 */
	@Override
	public void addMessage(MessageResource messageResource) throws SQLException {
		Connection connection = DBUtil.getConnection();
		String query = "insert into message values (?,?,?,?)";
		java.sql.PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setInt(1, messageResource.getId());
		stmt.setString(2, messageResource.getMessage());
		stmt.setString(3, messageResource.getAuthor());
		Timestamp timestamp = new java.sql.Timestamp(messageResource.getPostedDate().getTime());
		stmt.setTimestamp(4, timestamp);
		stmt.executeUpdate();
		logger.info("message added successfully");
	}

	/**
	 * Update message based on message id
	 * @author amit
	 * @param id
	 * @param messageResource
	 * @throws SQLException 
	 */
	@Override
	public void updateMessage(int id, MessageResource messageResource) throws SQLException {
		Connection connection = DBUtil.getConnection();
		String query = "update message set message=?, author_name=?, posted_date=? where id=?";
		PreparedStatement psmt = connection.prepareStatement(query);
		psmt.setString(1, messageResource.getMessage());
		psmt.setString(2, messageResource.getAuthor());
		Timestamp timestamp = new java.sql.Timestamp(messageResource.getPostedDate().getTime());
		psmt.setTimestamp(3, timestamp);
		psmt.setInt(4, id);
		int execute = psmt.executeUpdate();
		if (execute == 0) {
			logger.info("No message uppdated");
		} else {
			logger.info("message got updated successfully");
		}
	}

	/**
	 * Delete message from DB based on message id
	 * @author amit
	 * @param messageId
	 */
	@Override
	public void deleteMessage(int messageId) throws SQLException {
		Connection connection = DBUtil.getConnection();
		String query = "delete from message where id=?";
		PreparedStatement psmt = connection.prepareStatement(query);
		psmt.setInt(1, messageId);
		psmt.executeUpdate();
		logger.info("Message deleted successfully");
	}

	/**
	 * Get list of messages based on author name
	 * @author amit
	 * @param author
	 * @param url
	 * @return message list
	 */
	@Override
	public List<MessageResource> getMessages(String author, String url) throws SQLException {
		Connection connection = DBUtil.getConnection();
		String query = "select * from message where author_name=?";
		List<MessageResource> messageList = new ArrayList<>();
		PreparedStatement psmt = connection.prepareStatement(query);
		psmt.setString(1, author);
		ResultSet resultSet = psmt.executeQuery();
		while (resultSet.next()) {
			MessageResource messageResource = new MessageResource();
			messageResource.setId(resultSet.getInt(1));
			messageResource.setMessage(resultSet.getString(2));
			messageResource.setAuthor(resultSet.getString(3));
			messageResource.setPostedDate(resultSet.getTimestamp(4));
			messageResource.addLink(url + resultSet.getInt(1), "self");
			messageList.add(messageResource);
		}
		logger.info("Messages list retrieved successfully");
		return messageList;
	}
}
