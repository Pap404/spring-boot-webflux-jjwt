package com.ard333.springbootwebfluxjjwt.security.repository;

import com.ard333.springbootwebfluxjjwt.model.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
 public Mono<Void> deleteAllByCommentator (String commentator);
 public Mono<Void> deleteByCommentator (String commentator);
}
