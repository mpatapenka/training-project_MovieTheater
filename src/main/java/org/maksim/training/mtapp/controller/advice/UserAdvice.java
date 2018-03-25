package org.maksim.training.mtapp.controller.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class UserAdvice {
    @ModelAttribute
    public void populateUsername(Principal principal, Model model) {
        if (principal != null && principal.getName() != null) {
            model.addAttribute("username", principal.getName());
        }
    }
}