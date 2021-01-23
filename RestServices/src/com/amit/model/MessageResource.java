package com.amit.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author amit
 * Bean class to maintain all messages details.
 */
@XmlRootElement
@JsonPropertyOrder({ "id", "message", "author", "postedDate", "links" })
public class MessageResource {

	private int id;
	private String message;
	private String author;
	private Date postedDate;
	List<Link> links= new ArrayList<>();
	List<CommentResource> commentsList;
	/**
	 * Default constructor
	 */
	public MessageResource() {
		super();
	}
	
	/**
	 * Constructor using fields
	 * @param id
	 * @param message
	 */
	public MessageResource(int id, String message, String author)
	{
		this.id= id;
		this.message= message;
		this.author= author;
		this.postedDate= new Date();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the postedDate
	 */
	public Date getPostedDate() {
		return postedDate;
	}

	/**
	 * @param postedDate the postedDate to set
	 */
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	/**
	 * @return the links
	 */
	public List<Link> getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public void addLink(String url, String rel)
	{
		Link link= new Link(url, rel);
		links.add(link);
	}

	/**
	 * @return the commentsList
	 */
	@XmlTransient
	public List<CommentResource> getCommentsList() {
		return commentsList;
	}

	/**
	 * @param commentsList the commentsList to set
	 */
	public void setCommentsList(List<CommentResource> commentsList) {
		this.commentsList = commentsList;
	}
}
