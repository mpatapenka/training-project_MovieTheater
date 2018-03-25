package org.maksim.training.mtapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/util")
public class UtilityController {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtilityController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hash-gen")
    public ResponseEntity<?> createHash(@RequestParam String s) {
        return new ResponseEntity<>(passwordEncoder.encode(s), HttpStatus.OK);
    }
}