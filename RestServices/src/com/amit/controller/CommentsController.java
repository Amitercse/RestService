package com.amit.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amit.controller.exception.DataNotFoundException;
import com.amit.controller.exception.SystemError;
import com.amit.model.CommentParams;
import com.amit.model.CommentResource;
import com.amit.service.CommentsService;
import com.amit.service.CommentsServiceImpl;

@Path("/messages")
public class CommentsController {
	
	private static Log logger = LogFactory.getLog(CommentsController.class);

	private static final String SYSTEM_ERROR_OCCURRED= "System error occurred";
	
	/**
	 * Rest end point to get list of comments for a message
	 * @author amit
	 * @param messageId comments list
	 * @return
	 */
	@Path("/{messageId}/comments")
	@GET
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<CommentResource> getComments(@PathParam("messageId") int messageId, @BeanParam CommentParams params) {
		List<CommentResource> commentsList = new ArrayList<>();
		try {
			CommentsService commentsService = new CommentsServiceImpl();
			// By default bean param will have default values of parameters.
			if (params != null && params.getAuthor() != null) {
				CommentResource commentResource = commentsService.getComment(params.getCommentId(), params.getAuthor());
				commentsList.add(commentResource);
			} else {
				commentsList = commentsService.getComments(messageId);
			}
			if (commentsList.isEmpty()) {
				throw new DataNotFoundException("Comment list not found");
			}
		} catch (SQLException e) {
			logger.error("Error occurred while retrieving comments");
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return commentsList;
	}
	
	/**
	 * Rest end point to get comment based on message id and comment id
	 * @author amit
	 * @param messageId
	 * @param commentId
	 * @return comment
	 */
	@Path("/{messageId}/comments/{commentId}")
	@GET
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getComments(@PathParam("messageId") int messageId, @PathParam("commentId") int commentId) {
		CommentResource commentResource = new CommentResource();
		try {
			CommentsService commentsService = new CommentsServiceImpl();
			commentResource = commentsService.getComment(messageId, commentId);
			if (commentResource.getComment() == null) {
				throw new DataNotFoundException("No comment found for comment id: " + commentId);
			}
		} catch (SQLException e) {
			logger.error("Error occurred while retrieving comments");
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.OK).entity(commentResource).build();
	}
	
	/**
	 * Rest end point to create new comment
	 * @param commentResource
	 * @return response
	 */
	@POST
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{messageId}/comments")
	public Response addComment(CommentResource commentResource) {
		try {
			CommentsService commentService = new CommentsServiceImpl();
			commentService.addComment(commentResource);
		} catch (SQLException e) {
			logger.error("error occurred while adding new comment");
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.CREATED).build();
	}
	
	/**
	 * Rest endpoint to update comment
	 * @author amit
	 * @param commentId
	 * @param commentResource
	 * @return comment resource
	 */
	@PUT
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/{messageId}/comments/{commentId}")
	public Response updateComment(@PathParam("messageId") int messageId, @PathParam("commentId") int commentId,
			CommentResource commentResource) {
		CommentResource updatedcommentResource = new CommentResource();
		try {
			CommentsService commentService = new CommentsServiceImpl();
			commentService.updateComment(commentId, commentResource);
			updatedcommentResource = commentService.getComment(messageId, commentId);
			if (updatedcommentResource.getComment() == null) {
				throw new DataNotFoundException("No comment found for comment id: " + commentId);
			}
		} catch (SQLException e) {
			logger.error("error occurred while updating the comment");
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.OK).entity(updatedcommentResource).build();
	}
	
	/**
	 * Rest end point to delete comment
	 * @param messageId
	 * @param commentId
	 * @return response
	 */
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@DELETE
	@Path("/{messageId}/comments/{commentId}")
	public Response deleteComment(@PathParam("messageId") int messageId, @PathParam("commentId") int commentId) {
		try {
			CommentsService commentService = new CommentsServiceImpl();
			commentService.deleteComment(messageId, commentId);
		} catch (SQLException e) {
			logger.error("error occurred while deleting record");
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.OK).build();
	}
}
