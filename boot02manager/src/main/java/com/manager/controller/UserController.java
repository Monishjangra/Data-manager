package com.manager.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manager.entities.Contact;
import com.manager.entities.User;
import com.manager.repository.ContactRepository;
import com.manager.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController implements IUserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data
	@Override
	public void addCommonData(Model model, Principal principal) {
		String name = principal.getName();
		// System.out.println(name);
		User user = userRepository.getUserByUsername(name);
		// System.out.println(user);
		model.addAttribute("user", user);
	}

	// home dashboard page
	@Override
	public String DashBoard(Model model, Principal principal) {
		model.addAttribute("title", "Dashboard");
		return "normal/user-dashboard";
	}

	// add contact form
	@Override
	public String addContact(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_data";
	}

	// processing of adding data
	public String processContact(@ModelAttribute Contact contact, Principal principal, Model model) {
		// System.out.println("Data: " + contact);
		String name = principal.getName();
		User user = userRepository.getUserByUsername(name);
		contact.setUser(user);
		user.getContacts().add(contact);
		userRepository.save(user);
		model.addAttribute("message", "successfully added");
		System.out.println("Data added in database.....");
		return "normal/add_data";
	}

	// for view contacts
	@Override
	public String showContact(@PathVariable Integer page, Model model, Principal principal) {
		model.addAttribute("title", "View contact");
		String name = principal.getName();

		Pageable pageable = PageRequest.of(page, 2);
		User user = userRepository.getUserByUsername(name);
		Page<Contact> contacts = contactRepository.findContactsByUser(user.getId(), pageable);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/show_contact";
	}

	// for particular contact
	@Override
	public String contactDetail(@PathVariable Integer id, Model model, Principal principal) {
		Optional<Contact> contactId = contactRepository.findById(id);
		Contact contact = contactId.get();

		String name = principal.getName();
		User user = userRepository.getUserByUsername(name);
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", user.getName());
		}
		return "normal/user-detail";
	}

	// for delete contact
	@Override
	public String deleteContact(Integer cid, Model model, Principal principal) {
		Contact contact = contactRepository.findById(cid).get();
		User user = userRepository.getUserByUsername(principal.getName());
		if (user.getId() == contact.getUser().getId()) {
			contact.setUser(null);
			contactRepository.deleteById(cid);
			System.out.println("deleted.....");
		}
		model.addAttribute("message", "contact deleted");
		return "redirect:/user/show-contacts/0";
	}

	// for update contact
	@Override
	public String updateContact(@PathVariable("cid") Integer id, Model model) {
		model.addAttribute("title", "Update Contact");
		Contact contact = contactRepository.findById(id).get();
		model.addAttribute("contact", contact);
		return "normal/update-contact";
	}

	// for update process
	@Override
	public String updateProcess(@ModelAttribute Contact contact, Principal principal, Model model) {
		User user = userRepository.getUserByUsername(principal.getName());
		contact.setUser(user);
		contactRepository.save(contact);
		model.addAttribute("message", "Successfully updated");
		return "redirect:/user/contact-detail/" + contact.getcId();
	}

	// for profile
	@Override
	public String profile(Model model, Principal principal) {
		model.addAttribute("title", "Profile");
		return "normal/profile";
	}

	// for-setting
	@Override
	public String setting(Model model) {
		model.addAttribute("title", "Setting");
		return "normal/setting";
	}

	// for change password
	@Autowired
	private BCryptPasswordEncoder bEncoder;

	@Override
	public String changePassword(@RequestParam String oldPassword,
			@RequestParam String newPassword,
			Principal principal) {
		User user = userRepository.getUserByUsername(principal.getName());

		if (bEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(bEncoder.encode(newPassword));
			userRepository.save(user);
			return "redirect:/user/dashboard";
		} else {
			return "redirect:/user/setting";
		}
	}

}