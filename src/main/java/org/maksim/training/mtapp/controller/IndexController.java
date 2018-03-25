package org.maksim.training.mtapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
            Model model) {
        model.addAttribute("isError", error != null);
        model.addAttribute("isLogout", logout != null);
        return "login";
    }
}