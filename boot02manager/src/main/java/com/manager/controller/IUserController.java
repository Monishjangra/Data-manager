package com.manager.controller;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manager.entities.Contact;

public interface IUserController {
	@ModelAttribute
	public void addCommonData(Model model, Principal principal);

	@GetMapping("/dashboard")
	public String DashBoard(Model model, Principal principal);

	@GetMapping("/add-contact")
	public String addContact(Model model);

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, Principal principal, Model model);

	@GetMapping("/show-contacts/{page}")
	public String showContact(@PathVariable("page") Integer page, Model model, Principal principal);

	@GetMapping("/contact-detail/{id}")
	public String contactDetail(@PathVariable("id") Integer id,Model model, Principal principal);

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid,Model model, Principal principal);

	@PostMapping("/update/{cid}")
	public String updateContact(@PathVariable("cid") Integer id, Model model);

	@PostMapping("/update-process")
	public String updateProcess(@ModelAttribute Contact contact, Principal principal, Model model);

	@GetMapping("/profile")
	public String profile(Model model, Principal principal);

	@GetMapping("/setting")
	public String setting(Model model);

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal);
}