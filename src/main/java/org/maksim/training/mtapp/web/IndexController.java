package org.maksim.training.mtapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String getIndexPage() {
        if (true) {
            throw new RuntimeException("Fuck off");
        }
        return "index";
    }

    @GetMapping("/a")
    public String getIndexPage1() {
        return "index";
    }

    @GetMapping("/s")
    public String getEPage1(Model model) {
//        model.addAttribute("message", "From e");
        return "error";
    }
}