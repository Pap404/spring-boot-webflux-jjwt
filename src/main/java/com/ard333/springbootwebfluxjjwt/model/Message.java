package com.ard333.springbootwebfluxjjwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author ard333
 */
@Data @NoArgsConstructor @AllArgsConstructor @ToString @Document
public class Message {
	@Id
	private String id;
	private String content;

	public Message(String content) {
		this.content = content;
	}
}
