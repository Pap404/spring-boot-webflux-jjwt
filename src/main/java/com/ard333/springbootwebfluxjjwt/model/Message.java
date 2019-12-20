package com.ard333.springbootwebfluxjjwt.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ard333
 */
@Data @NoArgsConstructor @AllArgsConstructor @ToString @Document
public class Message {
	@Id
	private String id;
	private String content;
	private String autor;

	public Message(String content) {
		this.content = content;
	}


	private List<Comment> comment = new ArrayList<>();

	public void addCommentToList(Comment comment) { this.comment.add(comment); }
}
