package org.maksim.training.mtapp.controller;

import com.google.common.collect.Lists;
import org.maksim.training.mtapp.model.User;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadUsers(@RequestBody List<User> users) {
        try {
            List<String> failedEmails = Lists.newArrayList();
            for (User user : users) {
                if (userService.save(user) == null) {
                    failedEmails.add(user.getEmail());
                }
            }
            return failedEmails.isEmpty()
                    ? new ResponseEntity<>(users, HttpStatus.CREATED)
                    : new ResponseEntity<>("Following emails already registered: " + failedEmails, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Creation failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}