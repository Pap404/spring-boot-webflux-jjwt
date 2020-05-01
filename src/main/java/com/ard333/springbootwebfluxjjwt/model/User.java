package com.ard333.springbootwebfluxjjwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ard333.springbootwebfluxjjwt.security.model.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author ard333
 */
@ToString @AllArgsConstructor @NoArgsConstructor @Document
public class User implements UserDetails {
	@Id
	@Getter @Setter
	private String id;

	private String username;

	private String password;

	@Getter @Setter
	private Boolean enabled;

	@Getter @Setter
	private List<Role> roles = new ArrayList<>();

	@Getter @Setter
	private List<Message> message = new ArrayList<>();

//	@Getter
//	@Setter
//	private List<Comment> comment = new ArrayList<>();

	public void addMessageToList(Message message) {
		this.message.add(message);
	}

	public void addRoleToList(Role role) {
		this.roles.add(role);
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

}
