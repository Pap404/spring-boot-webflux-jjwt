package com.ard333.springbootwebfluxjjwt.service;

import com.ard333.springbootwebfluxjjwt.model.User;
import com.ard333.springbootwebfluxjjwt.security.model.Role;
import java.util.Arrays;

import com.ard333.springbootwebfluxjjwt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 *
 * @author ard333
 */
@Service
public class UserService {
	
	// this is just an example, you can load the user from the database from the repository
	@Autowired
	UserRepository userRepository;
	//username:passwowrd -> user:user
//	private final String userUsername = "user";// password: user
//	private final User user = new User(userUsername, "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", true, Arrays.asList(Role.ROLE_USER));
//
//	//username:passwowrd -> admin:admin
//	private final String adminUsername = "admin";// password: admin
//	private final User admin = new User(adminUsername, "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=", true, Arrays.asList(Role.ROLE_ADMIN));
	
	public Mono<User> findByUsername(String username) {
		return userRepository.findByUsername(username).switchIfEmpty(Mono.empty());
	}

	public Mono<User> saveUser(User user) {
		if(findByUsername(user.getUsername()) != null){
			try {
				throw new Exception("User");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return userRepository.save(user);
	}
	
}
