package com.kantares.store.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Kantares Store");
        model.addAttribute("name", "Kantares");
        return "index";
    }
}
