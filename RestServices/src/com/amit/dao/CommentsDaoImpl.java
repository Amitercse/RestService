package com.amit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amit.model.CommentResource;
import com.amit.util.DBUtil;

public class CommentsDaoImpl implements CommentsDao {
	
	private static Log logger = LogFactory.getLog(CommentsDaoImpl.class);

	/**
	 * Get comments list for message id
	 * @author amit
	 * @param messageId
	 * @return comments list
	 * @throws SQLException
	 */
	@Override
	public List<CommentResource> getComments(int messageId) throws SQLException {
		Connection connection = DBUtil.getConnection();
		List<CommentResource> commentsList = new ArrayList<>();
		String query = "select * from comment where message_id=?";
		PreparedStatement psmt = connection.prepareStatement(query);
		psmt.setInt(1, messageId);
		ResultSet resultSet = psmt.executeQuery();
		while (resultSet.next()) {
			CommentResource commentResource = new CommentResource();
			commentResource.setId(resultSet.getInt(1));
			commentResource.setComment(resultSet.getString(2));
			commentResource.setAuthorName(resultSet.getString(3));
			commentResource.setPostedDate(resultSet.getTimestamp(4));
			commentResource.setMessageId(resultSet.getInt(5));
			commentsList.add(commentResource);
		}
		logger.info("Retrieved comments list successfully");
		return commentsList;
	}

	/**
	 * Get comment based on message id and comment id
	 * @author amit
	 * @param messageId
	 * @param commentId
	 * @return comment
	 * @throws SQLException
	 */
	@Override
	public CommentResource getComment(int messageId, int commentId) throws SQLException {
		Connection connection = DBUtil.getConnection();
		CommentResource commentResource = new CommentResource();
		String query = "select * from comment where message_id=? and comment_id=?";
		PreparedStatement psmt = connection.prepareStatement(query);
		psmt.setInt(1, messageId);
		psmt.setInt(2, commentId);
		ResultSet resultSet = psmt.executeQuery();
		while (resultSet.next()) {
			commentResource.setId(resultSet.getInt(1));
			commentResource.setComment(resultSet.getString(2));
			commentResource.setAuthorName(resultSet.getString(3));
			commentResource.setPostedDate(resultSet.getTimestamp(4));
			commentResource.setMessageId(resultSet.getInt(5));
		}
		logger.info("Retrieved comment successfully");
		return commentResource;
	}
	
	/**
	 * Add a new comment in database
	 * @author amit
	 * @param commentResource
	 */
	@Override
	public void addComment(CommentResource commentResource) throws SQLException {
		Connection connection = DBUtil.getConnection();
		String query = "insert into comment values(?,?,?,?,?)";
		PreparedStatement psmt = connection.prepareStatement(query);
		psmt.setInt(1, commentResource.getId());
		psmt.setString(2, commentResource.getComment());
		psmt.setString(3, commentResource.getAuthorName());
		Timestamp timestamp = new Timestamp(commentResource.getPostedDate().getTime());
		psmt.setTimestamp(4, timestamp);
		psmt.setInt(5, commentResource.getMessageId());
		psmt.executeUpdate();
		logger.info("comment created successfully");
	}

	/**
	 * Update comment based on message id and comment id
	 * @author amit
	 * @param commentId
	 * @param commentResource
	 */
	@Override
	public void updateComment(int commentId, CommentResource commentResource) throws SQLException {
		Connection connection = DBUtil.getConnection();
		String query = "update comment set comment=?, author_name=?, posted_date=? where comment_id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, commentResource.getComment());
		preparedStatement.setString(2, commentResource.getAuthorName());
		Timestamp timestamp = new Timestamp(commentResource.getPostedDate().getTime());
		preparedStatement.setTimestamp(3, timestamp);
		preparedStatement.setInt(4, commentId);
		int execute = preparedStatement.executeUpdate();
		if (execute == 0) {
			logger.info("No comment uppdated");
		} else {
			logger.info("comment got updated successfully");
		}
	}

	/**
	 * Delete comment based on message id and comment id
	 * @author amit
	 * @param messageId
	 * @param commentId
	 */
	@Override
	public void deleteComment(int messageId, int commentId) throws SQLException {
		Connection connection = DBUtil.getConnection();
		String query = "delete from comment where message_id=? and comment_id=?";
		PreparedStatement psmt = connection.prepareStatement(query);
		psmt.setInt(1, messageId);
		psmt.setInt(2, commentId);
		psmt.executeUpdate();
		logger.info("comment deleted successfully");
	}

	/**
	 * Find comment based on comment id and author name
	 * @author amit
	 * @param commentId
	 * @param author
	 * @return comment
	 */
	@Override
	public CommentResource getComment(int commentId, String author) throws SQLException {
		Connection connection = DBUtil.getConnection();
		String query = "select * from comment where comment_id=? and author_name=?";
		PreparedStatement psmt = connection.prepareStatement(query);
		psmt.setInt(1, commentId);
		psmt.setString(2, author);
		ResultSet rs = psmt.executeQuery();
		CommentResource commentResource = new CommentResource();
		while (rs.next()) {
			commentResource.setId(rs.getInt(1));
			commentResource.setComment(rs.getString(2));
			commentResource.setAuthorName(rs.getString(3));
			commentResource.setPostedDate(rs.getTimestamp(4));
			commentResource.setMessageId(rs.getInt(5));
		}
		return commentResource;
	}
}
