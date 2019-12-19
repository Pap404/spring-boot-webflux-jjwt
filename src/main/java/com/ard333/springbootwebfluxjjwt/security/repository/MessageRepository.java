package com.ard333.springbootwebfluxjjwt.security.repository;

import com.ard333.springbootwebfluxjjwt.model.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {

}
