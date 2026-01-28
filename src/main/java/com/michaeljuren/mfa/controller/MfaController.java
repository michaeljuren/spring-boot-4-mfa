package com.michaeljuren.mfa.controller;


import com.michaeljuren.mfa.service.TotpService;
import com.michaeljuren.mfa.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mfa")
@RequiredArgsConstructor
public class MfaController {

    private final UserService userService;
    private final TotpService totpService;

    @GetMapping("/setup")
    public String setupMfa(Model model, Authentication authentication) {
        String username = authentication.getName();
        String secret = userService.enableMfaForUser(username);
        String qrCodeUri = totpService.generateQrCodeImageUri(secret, username);

        model.addAttribute("qrCode", qrCodeUri);
        model.addAttribute("secret", secret);

        return "mfa-setup";
    }

    @GetMapping("/verify")
    public String verifyMfaPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("mfa_username");
        if (username == null) {
            return "redirect:/login";
        }
        return "mfa-verify";
    }

    @PostMapping("/verify")
    public String verifyMfa(@RequestParam String code,
                            HttpSession session,
                            Model model) {
        String username = (String) session.getAttribute("mfa_username");

        if (username == null) {
            return "redirect:/login";
        }

        if (userService.verifyMfaCode(username, code)) {
            session.setAttribute("mfa_authenticated", true);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid MFA code. Please try again.");
            return "mfa-verify";
        }
    }

    @PostMapping("/disable")
    public String disableMfa(Authentication authentication, HttpSession session) {
        userService.disableMfaForUser(authentication.getName());

        // Clear MFA session attributes
        session.removeAttribute("mfa_username");
        session.removeAttribute("mfa_authenticated");
        return "redirect:/home?mfaDisabled";
    }

}
