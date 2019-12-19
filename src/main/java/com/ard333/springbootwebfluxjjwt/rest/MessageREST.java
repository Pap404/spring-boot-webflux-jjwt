package com.ard333.springbootwebfluxjjwt.rest;

import com.ard333.springbootwebfluxjjwt.model.Message;
import com.ard333.springbootwebfluxjjwt.model.User;
import com.ard333.springbootwebfluxjjwt.security.repository.MessageRepository;
import com.ard333.springbootwebfluxjjwt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageREST {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping
    public Flux<Message> getAllMessage (){
        return messageRepository.findAll();
    }

    @PostMapping
    public Mono<Message> createMessage (@RequestBody Message message) {
        return messageRepository.save(message);
    }

    @DeleteMapping("/{id}")
    public Mono<String> deleteMessage (@PathVariable String id) {
        return messageRepository.deleteById(id)
                .then(Mono.just("DELETED"));
    }

    @GetMapping("/{id}")
    public Mono<Message> getMessageById (@PathVariable String id) {
        return messageRepository.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<Message> updateMessage (@PathVariable String id, @RequestBody Message message) {
        Mono<Message> messageMono = messageRepository.findById(id);
        return messageMono.flatMap(m -> {
           m.setContent(m.getContent());
           return messageRepository.save(m);
        });
    }

    @GetMapping("/user")
    public Flux<Message> getAllMessageOfUser (Principal principal) {
         return userRepository.findByUsername(principal.getName())
         .map(u -> u.getMessage()).flatMapMany(Flux::fromIterable);
    }

    @PostMapping("/user")
    public Mono<User> addMessageToUser (Principal principal, @RequestBody Message message) {
        Mono<User> userMono = userRepository.findByUsername(principal.getName());
        Mono<Message> messageMono = messageRepository.save(message);
        return userMono.zipWith(messageMono)
                .flatMap(t -> {
                    t.getT1().addMessageToList(t.getT2());
                    return userRepository.save(t.getT1());
                });
    }
}