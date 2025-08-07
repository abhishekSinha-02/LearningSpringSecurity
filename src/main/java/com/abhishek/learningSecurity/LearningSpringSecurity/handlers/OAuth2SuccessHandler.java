package com.abhishek.learningSecurity.LearningSpringSecurity.handlers;

import com.abhishek.learningSecurity.LearningSpringSecurity.entities.User;
import com.abhishek.learningSecurity.LearningSpringSecurity.service.impl.JwtService;
import com.abhishek.learningSecurity.LearningSpringSecurity.service.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Value("${deploy.env}")
    private String deployEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException{

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        /**
         * we can get the principal from the token, and this principal is going to give me the "OAuth2User" and
         * "OAuth2User" is basically a interface and this interface is being implemented by "DefaultOAuth2User"
         * and in "DefaultOAuth2User" we have "attributes" as well
         * so, from these "attributes" we can get the "email", get the "name" and all the other information about this user
         */
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();
        //log.info(oAuth2User.getAttribute("email"));

        String email = oAuth2User.getAttribute("email");

        User user = userService.getUserByEmail(email);

        if (user == null){
            User newUser = User
                    .builder()
                    .name(oAuth2User.getAttribute("name"))
                    .email(email)
                    .build();

            user = userService.save(newUser);
        }

        String accessToken =  jwtService.generateAccessToken(user);
        String refreshToken =  jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;

        /**
         * this method is used to redirect to any particular URL
         * or, we can use this also - response.sendRedirect(frontEndUrl);
         */

        //getRedirectStrategy().sendRedirect(request,response,frontEndUrl);

        response.sendRedirect(frontEndUrl);
    }
}
