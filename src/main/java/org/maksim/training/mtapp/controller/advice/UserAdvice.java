package org.maksim.training.mtapp.controller.advice;

import org.maksim.training.mtapp.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserAdvice {
    @ModelAttribute
    public void populateUser(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            user.setPassword("");
        }
        model.addAttribute("user", user);
    }
}