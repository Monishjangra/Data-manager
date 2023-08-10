package com.manager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.manager.entities.Contact;
import com.manager.entities.User;
import com.manager.repository.ContactRepository;
import com.manager.repository.UserRepository;

@RestController
public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable String query, Principal principal) {

        System.out.println(query);
        User user = userRepository.getUserByUsername(principal.getName());
        List<Contact> contacts = contactRepository.findByNameContainingAndUser(query, user);
        return ResponseEntity.ok(contacts);
    }
}
