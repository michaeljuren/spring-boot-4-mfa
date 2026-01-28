package com.michaeljuren.mfa.security;

import com.michaeljuren.mfa.entity.User;
import com.michaeljuren.mfa.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MfaAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user.isMfaEnabled()) {
            // Store username in session for MFA verification
            HttpSession session = request.getSession();
            session.setAttribute("mfa_username", username);
            session.setAttribute("mfa_authenticated", false);

            // Redirect to MFA verification page
            response.sendRedirect("/mfa/verify");
        } else {
            // No MFA required, redirect to home
            response.sendRedirect("/home");
        }
    }
}
