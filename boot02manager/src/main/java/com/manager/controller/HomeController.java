package com.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.manager.entities.User;
import com.manager.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class HomeController implements IHomeController {

	 @Autowired
	 private UserRepository userRepository;

	 @Autowired
	 private BCryptPasswordEncoder password;
	 
// for home 
	 @Override
	 public String home(Model model) {
		model.addAttribute("title", "Home Manager");
		return "home";
	}
// for example
	 @Override
	public String about(Model model) {
		model.addAttribute("title", "about manager");
		return "about";
	}
// for signup
	 @Override
	public String signUp(Model model) {
		model.addAttribute("title", "Register manager");
		model.addAttribute("user", new User());
		return "signup";
	}
// for signup process
	 @Override
	public String handler(@Valid @ModelAttribute("user") User user, Model model,BindingResult result) {
		try {
			if(result.hasErrors())
			{
				System.out.println("Error "+result.toString());
				model.addAttribute("user",user);
				return "signup";
				
			}
			user.setRole("ROLE_USER");
			user.setPassword(password.encode(user.getPassword()));
		System.out.println("User: " +user);
		
		userRepository.save(user);
		System.out.println("Successfully added.....");
		model.addAttribute("message", "Successfully added..");
		return "signup";
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Something went wrong!!" +e.getMessage());
			return "signup";
		}
	}
// for login
	public String customLogIn(Model model) {
		model.addAttribute("title", "Login Manager");
		return "login";
	}
// for forgot password
	@Override
	public String forgot(Model model) {
		model.addAttribute("title", "Forgot Password");
		return "forgot";
	}
// for process forgot password
	@Override
	public String forgotPassword(@RequestParam("email") String email,
								@RequestParam("name") String name,
								@RequestParam("id") int id,
								@RequestParam("newPassword") String newPassword) {
		User user = userRepository.getUserByUsername(email);
		if(user.getName().equals(name) && user.getId()==id){
			System.out.println(user.getName());
			System.out.println(user.getId());
			user.setPassword(password.encode(newPassword));
			userRepository.save(user);
			return "redirect:/login";
		}
		else{
			return "redirect:/forgot";
		}
	}
}
