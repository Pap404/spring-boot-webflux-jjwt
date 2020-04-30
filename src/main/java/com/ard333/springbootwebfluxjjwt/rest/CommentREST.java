package com.ard333.springbootwebfluxjjwt.rest;

import com.ard333.springbootwebfluxjjwt.model.Comment;
import com.ard333.springbootwebfluxjjwt.model.Message;
import com.ard333.springbootwebfluxjjwt.model.User;
import com.ard333.springbootwebfluxjjwt.security.repository.CommentRepository;
import com.ard333.springbootwebfluxjjwt.security.repository.MessageRepository;
import com.ard333.springbootwebfluxjjwt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentREST {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public Flux<Comment> getAllComment () {
        return commentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Comment> getCommentById (@PathVariable String id) {
        return commentRepository.findById(id);
    }

    @PostMapping
    public Mono<Comment> createComment (@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }

    @DeleteMapping("/{id}")
    public Mono<String> deleteCommentById (@PathVariable String id) {
        return commentRepository.deleteById(id)
                .then(Mono.just("DELETED"));
    }

    @PutMapping("/{id}")
    public Mono<Comment> updateComment (@PathVariable String id, @RequestBody Comment comment) {
        Mono<Comment> commentMono = commentRepository.findById(id);
        return commentMono.flatMap(c -> {
            c.setComment(c.getComment());
            return commentRepository.save(c);
        });
    }

    @GetMapping("/user")
    public Flux<Comment> getAllCommentOfUser (Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .map(c -> c.getComment()).flatMapMany(Flux::fromIterable);
    }

    @PostMapping("/user/{messageId}")
    public Mono<User> addCommentToMessage (@PathVariable String messageId, @RequestBody Comment comment, Principal principal) {
        comment.setCommentator(principal.getName());
        Mono<User> user = userRepository.findByUsername(principal.getName());
        Mono<Comment> commentMono = commentRepository.save(comment);
       return user.zipWith(commentMono)
                .flatMap( t -> {
                    User user1 = t.getT1();
                    List<Message> messages = user1.getMessage();
                    for (Message mes : messages) {
                        if(messageId.equals(mes.getId())){
                            mes.addCommentToList(comment);
                        }
                    }
                    user1.setMessage(messages);
                    return userRepository.save(user1);
                });
//        Mono<Message> messageMono = messageRepository.findById(messageId);
//        comment.setCommentator(principal.getName());
//        Mono<Comment> commentMono = commentRepository.save(comment);
//        messageMono.zipWith(commentMono)
//                .flatMap(t -> {
//                    t.getT1().addCommentToList(t.getT2());
//                    return messageRepository.save(t.getT1());
//                });
//        return commentMono;
    }

    @GetMapping("/user/deleteAll")
    public Mono<Void> deleteAllCommentByName (Principal principal) {
        System.out.println(principal.getName());
        return commentRepository.deleteAllByCommentator(principal.getName());
    }

    @GetMapping("/user/delete")
    public Mono<Void> deleteCommentByName (Principal principal) {
        return commentRepository.deleteByCommentator(principal.getName());
    }

 }
