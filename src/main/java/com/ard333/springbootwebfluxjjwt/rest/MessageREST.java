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
@CrossOrigin

public class MessageREST {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping
    public Flux<Message> getAllMessage (Principal principal){
        System.out.println(principal);
        return messageRepository.findAll();
    }

    @DeleteMapping
    public Mono<Void> deleteAllMessages () {
        return messageRepository.deleteAll();
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

    @DeleteMapping("/delete_message")
    public Mono<User> deleteAllMessageWithComment (Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .map(u -> {
                    u.cleanListMessage();
                    return u;
                });
    }

    @PostMapping("/user")
    public Mono<Message> addMessageToUser (Principal principal, @RequestBody Message message) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Mono<User> userMono = userRepository.findByUsername(principal.getName());
        message.setAutor(principal.getName());
        Mono<Message> messageMono = messageRepository.save(message);
        System.out.println("++++++++++++++" + messageMono);
         userMono.zipWith(messageMono)
                .flatMap(t -> {
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&");
                    User user = t.getT1();
                    System.out.println(t.getT1());
                    System.out.println(t.getT2());
                    user.addMessageToList(t.getT2());
                    System.out.println(user.getMessage());
                    return userRepository.save(user);
                });
         return messageMono;
    }
}
