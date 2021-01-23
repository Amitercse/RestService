package com.amit.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.amit.dao.MessageDao;
import com.amit.dao.MessageDaoImpl;
import com.amit.model.MessageResource;

/**
 * @author amit
 * Implementation class for message service.
 */
public class MessageServiceImpl implements MessageService {
	
	private static Log logger = LogFactory.getLog(MessageServiceImpl.class);

	/**
	 * Interact with DB and retrieve all messages.
	 * @author amit
	 * @param url
	 * @return message resources
	 * @throws SQLException 
	 */
	@Override
	public List<MessageResource> getMessages(String url) throws SQLException {
		MessageDao messageDao = new MessageDaoImpl();
		List<MessageResource> messageList = messageDao.getMessages(url);
		logger.info("Retrieved total messages: " + messageList.size());
		return messageList;
	}

	/**
	 * Interact with DB and get message based on message id.
	 * @author amit
	 * @param id
	 * @return message
	 * @throws SQLException 
	 */
	@Override
	public MessageResource getMessage(int id) throws SQLException {
		MessageDao messageDao = new MessageDaoImpl();
		return messageDao.getMessage(id);
	}

	/**
	 * @author amit
	 * @param messageResource
	 * @throws SQLException
	 * Add a new message record. 
	 */
	@Override
	public void addMessage(MessageResource messageResource) throws SQLException {
		MessageDao messageDao = new MessageDaoImpl();
		messageDao.addMessage(messageResource);
	}

	/**
	 * Service method to update message in DB.
	 * @author amit
	 * @param id
	 * @param messageResource
	 * @throws SQLException
	 */
	@Override
	public void updateMessage(int id, MessageResource messageResource) throws SQLException {
		MessageDao messageDao = new MessageDaoImpl();
		messageDao.updateMessage(id, messageResource);
	}

	/**
	 * Service method will invoke DAO layer to delete message.
	 * @author amit
	 * @param messageId
	 */
	@Override
	public void deleteMessage(int messageId) throws SQLException {
		MessageDao messageDao = new MessageDaoImpl();
		messageDao.deleteMessage(messageId);
	}

	/**
	 * Service layer method, will interact with DAO layer and get message list.
	 * @author amit
	 * @param author
	 * @param url
	 * @return message list
	 */
	@Override
	public List<MessageResource> getMessages(String author, String url) throws SQLException {
		MessageDao messageDao = new MessageDaoImpl();
		return messageDao.getMessages(author, url);
	}

}
