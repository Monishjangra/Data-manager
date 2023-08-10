package com.manager.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manager.entities.User;

import jakarta.validation.Valid;

public interface IHomeController {

	@GetMapping("/home")
	public String home(Model model);

	@GetMapping("/about")
	public String about(Model model);

	@GetMapping("/signup")
	public String signUp(Model model);

	@PostMapping("/do_register")
	public String handler(@Valid @ModelAttribute User user, Model model, BindingResult result);

	@GetMapping("/login")
	public String customLogIn(Model model);

	@GetMapping("/forgot")
	public String forgot(Model model);

	@PostMapping("/password-changed")
	public String forgotPassword(@RequestParam String email, @RequestParam String name, @RequestParam int id,
			@RequestParam String newPassword);

}
