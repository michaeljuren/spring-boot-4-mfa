package com.michaeljuren.mfa.controller;

import com.michaeljuren.mfa.entity.User;
import com.michaeljuren.mfa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("username", username);
        model.addAttribute("mfaEnabled", user.isMfaEnabled());

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
