package com.ard333.springbootwebfluxjjwt.rest;

import com.ard333.springbootwebfluxjjwt.model.User;
import com.ard333.springbootwebfluxjjwt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

@RequestMapping("/api/users")
public class UsersRest {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public Mono<User> getUser(Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }

    @GetMapping
    public Flux<User> findAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public Mono<User> findUserById(@PathVariable String userId) {
        return userRepository.findById(userId);
    }

    @PostMapping
    public Mono<User> createUSer(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{userId}")
    public Mono<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        return userRepository.findById(userId).flatMap(existingUser -> {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setEnabled(user.getEnabled());
            existingUser.setRoles(user.getRoles());
            return userRepository.save(existingUser);
        });
    }

    @DeleteMapping("/{userId}")
    public Mono<String> deleteUser(@PathVariable String userId) {
        return userRepository.findById(userId).flatMap(user -> userRepository.delete(user)).then(Mono.just("Deleted"));
    }
}
