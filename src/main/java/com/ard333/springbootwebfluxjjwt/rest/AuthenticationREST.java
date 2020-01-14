package com.ard333.springbootwebfluxjjwt.rest;

import com.ard333.springbootwebfluxjjwt.model.User;
import com.ard333.springbootwebfluxjjwt.security.JWTUtil;
import com.ard333.springbootwebfluxjjwt.security.PBKDF2Encoder;
import com.ard333.springbootwebfluxjjwt.security.model.AuthRequest;
import com.ard333.springbootwebfluxjjwt.security.model.AuthResponse;
import com.ard333.springbootwebfluxjjwt.security.model.Role;
import com.ard333.springbootwebfluxjjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/security")
public class AuthenticationREST {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
		return userService.findByUsername(ar.getUsername()).map((userDetails) -> {
			if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
				return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@PostMapping("/registration")
	public Mono userRegistration(@RequestBody User user) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.addRoleToList(Role.ROLE_USER);
			user.setEnabled(true);
		return Mono.just(user.getUsername())
				.flatMap(userService::findByUsername)
				.flatMap(dbUser -> Mono.error(new Exception("User is already exist")))
		.switchIfEmpty(userService.saveUser(user));
	}
}
