package org.maksim.training.mtapp.controller.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public String handleAnyException(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}