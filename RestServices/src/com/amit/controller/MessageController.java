package com.amit.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amit.controller.exception.DataNotFoundException;
import com.amit.controller.exception.SystemError;
import com.amit.model.MessageResource;
import com.amit.service.MessageService;
import com.amit.service.MessageServiceImpl;

/**
 * @author amit
 * Rest controller for message service application.
 */
@Path("/messages")
public class MessageController {
	
	private static Log logger = LogFactory.getLog(MessageController.class);
	
	private static final String SYSTEM_ERROR_OCCURRED="System error occurred";
	
	/**
	 * Invoke get request to retrieve all messages and based on author name.
	 * This method also accepts query param also. Because we can't keep two
	 * get requests for same resource URI and query param does not play any
	 * role in URI construction.
	 * @param authorName
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<MessageResource> getMessages(@QueryParam("author") String authorName, @Context UriInfo uriInfo) {
		List<MessageResource> messagesList = new ArrayList<>();
		String urlPath = uriInfo.getAbsolutePath().toString();
		try {
			MessageService messageService = new MessageServiceImpl();
			if (authorName != null) {
				messagesList = messageService.getMessages(authorName, urlPath);
			} else {
				messagesList = messageService.getMessages(urlPath);
			}
			if (messagesList.isEmpty()) {
				throw new DataNotFoundException("No message found");
			}
		} catch (SQLException e) {
			logger.error("error occurred while trying to access message list: " + e.getMessage());
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		logger.debug("Returning message list from controller");
		return messagesList;
	}
	
	/**
	 * Get and return message based on message id
	 * @param id
	 * @param uriInfo
	 * @return
	 */
	@Path("/{messageId}")
	@GET
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getMessage(@PathParam("messageId") int id, @Context UriInfo uriInfo) {
		MessageResource messageResource = null;
		try {
			MessageService messageService = new MessageServiceImpl();
			messageResource = messageService.getMessage(id);
			// Directly checking if message resource is null because if message
			// is not found in DB then DAO layer will return null.
			if (messageResource == null) {
				throw new DataNotFoundException("No data found for message id: " + id);
			}
			String url = uriInfo.getAbsolutePath().toString();
			messageResource.addLink(url, "self");
		} catch (SQLException e) {
			logger.info("Error while retrieved message based on message id: " + e.getMessage());
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.OK).entity(messageResource).build();
	}
	
	/**
	 * Create a new message using rest service.
	 * @param messageResource
	 */
	@POST
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMessage(MessageResource messageResource) {
		try {
			MessageService messageService = new MessageServiceImpl();
			messageService.addMessage(messageResource);
		} catch (SQLException e) {
			logger.error("error occurred while creating a new message: " + e.getMessage());
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.CREATED).build();
	}
	
	/**
	 * Add new message using xml type request
	 * @param messageResource
	 * @return
	 */
	@POST
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes(MediaType.APPLICATION_XML)
	public Response createMessageByXML(MessageResource messageResource) {
		try {
			MessageService messageService = new MessageServiceImpl();
			messageService.addMessage(messageResource);
		} catch (SQLException e) {
			logger.error("error occurred while creating a new message: " + e.getMessage());
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.CREATED).build();
	}
	
	/**
	 * Update message in DB and return the updated message to the user.
	 * @param messageId
	 * @param messageResource
	 * @param uriInfo
	 * @return MessageResource
	 */
	@Path("/{messageId}")
	@PUT
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMessage(@PathParam("messageId") int messageId, MessageResource messageResource,
			@Context UriInfo uriInfo) {
		MessageResource updatedMessageResource = null;
		String url = uriInfo.getAbsolutePath().toString();
		try {
			MessageService messageService = new MessageServiceImpl();
			messageService.updateMessage(messageId, messageResource);
			updatedMessageResource = messageService.getMessage(messageId);
			/*
			 * Directly can check if message is null because if message is not found then
			 * DAO layer will return null.
			 */
			if (updatedMessageResource == null) {
				throw new DataNotFoundException("No data found for id: " + messageId);
			}
			updatedMessageResource.addLink(url, "self");
		} catch (SQLException e) {
			logger.error("error occurred while updating message in DB: " + e.getMessage());
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.OK).entity(updatedMessageResource).build();
	}
	
	/**
	 * Delete message based on message id
	 * @param messageId
	 * @return
	 */
	@Path("/{messageId}")
	@DELETE
	@Produces(value= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response deleteMessage(@PathParam("messageId") int messageId) {
		try {
			MessageService messageService = new MessageServiceImpl();
			messageService.deleteMessage(messageId);
		} catch (SQLException e) {
			logger.error("error occurred while deleting record: " + e.getMessage());
			throw new SystemError(SYSTEM_ERROR_OCCURRED);
		}
		return Response.status(Status.OK).build();
	}
	
	/**
	 * Getting URI info using context
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Path("/context")
	public String getParamUsingContext(@Context UriInfo uriInfo)
	{
		return uriInfo.getAbsolutePath().toString();
	}
}
