package org.maksim.training.mtapp.rest;

import lombok.extern.slf4j.Slf4j;
import org.maksim.training.mtapp.entity.User;
import org.maksim.training.mtapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rest/users")
public class UsersResource {
    private final UserService userService;

    @Autowired
    public UsersResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable long userId) {
        User user = userService.getById(userId);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>("User not found.", HttpStatus.CONFLICT);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            return savedUser != null
                    ? new ResponseEntity<>(savedUser, HttpStatus.CREATED)
                    : new ResponseEntity<>("User with email '" + user.getEmail() + "' already exists.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Unhandled exception occur.", e);
            return new ResponseEntity<>("Creation failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable long userId, @RequestBody User user) {
        User originalUser = userService.getById(userId);
        if (originalUser != null) {
            user.setId(userId);
            User updated = userService.save(user);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found.", HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<?> removeUser(@PathVariable long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            User removed = userService.remove(user);
            return new ResponseEntity<>(removed, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found.", HttpStatus.CONFLICT);
    }
}