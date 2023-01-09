package com.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.manager.entities.User;
import com.manager.repository.UserRepository;

public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user= userRepository.getUserByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("Could not found user!!");
		}
		CustomUserDetails details= new CustomUserDetails(user);
		
		return details;
	}

}
