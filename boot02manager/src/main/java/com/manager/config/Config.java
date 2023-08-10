package com.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config {

	@Bean
	UserDetailsService getUserDetailService() {
		return new UserDetailService();
	}

	@Bean
	BCryptPasswordEncoder password() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(this.getUserDetailService());
		provider.setPasswordEncoder(password());
		return provider;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/user/**").hasRole("USER")
						.requestMatchers("/**").permitAll())
				.formLogin(login -> login.loginPage("/login")
						.loginProcessingUrl("/dologin")
						.defaultSuccessUrl("/user/dashboard"))
				.csrf(csrf -> csrf.disable());

		http.authenticationProvider(authenticationProvider());
		return http.build();
	}

}
