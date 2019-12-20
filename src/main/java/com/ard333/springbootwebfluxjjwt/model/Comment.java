package com.ard333.springbootwebfluxjjwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document

public class Comment {
    @Id
    private String id;
    private String comment;
    private String commentator;

    public Comment(String comment, String commentator) {
        this.comment = comment;
        this.commentator = commentator;
    }
}
