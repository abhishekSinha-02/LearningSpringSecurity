package com.abhishek.learningSecurity.LearningSpringSecurity.controller;

import com.abhishek.learningSecurity.LearningSpringSecurity.dto.LoginDto;
import com.abhishek.learningSecurity.LearningSpringSecurity.dto.LoginResponseDto;
import com.abhishek.learningSecurity.LearningSpringSecurity.dto.SignUpDto;
import com.abhishek.learningSecurity.LearningSpringSecurity.dto.UserDto;
import com.abhishek.learningSecurity.LearningSpringSecurity.service.impl.AuthService;
import com.abhishek.learningSecurity.LearningSpringSecurity.service.impl.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto){
        UserDto userDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
                                                  HttpServletResponse response){
        LoginResponseDto dto = authService.login(loginDto);

        /**
         * This is HTTP only cookie, it means only this server can access these cookies and no javascript can accept these cookies
         * Hence, attackers can get hold of the "access token" but not get hold of the "Refresh Token".
         *
         * i.e. now, our web browsers will make sure that this "cookie" doesn't get exposed so, no one can access this cookie
         * (no accessors attacks can access my cookie), only the user who is logged in can access this cookie
         */
        Cookie cookie = new Cookie("refreshToken",dto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        return ResponseEntity.ok(dto);
    }

    /**
     *Your frontend (e.g., React, Angular, etc.) should:
     *
     * 1. Detect expired access token:
     *          When a request fails with 401 Unauthorized (due to expired token), automatically call /auth/refresh.
     *
     * 2. Call /auth/refresh silently:
     *          The cookie is sent automatically by the browser.
     *          You get a new access token.
     *          Retry the original request.
     *
     * 3. Auto-refresh before it expires (optional but smoother):
     *          Decode the JWT in frontend (not mandatory, but useful).
     *          Track the exp (expiry).
     *          Set a timer to refresh 1 minute before expiry.
     */


    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){

//        Cookie[] cookies = request.getCookies();

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie-> cookie.getValue())
                .orElseThrow(()-> new AuthenticationServiceException("Refresh token not found inside the Cookie"));

        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);
    }
}
