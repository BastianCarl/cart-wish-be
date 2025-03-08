package com.example.demo.controllers;

import com.example.demo.model.JWTToken;
import com.example.demo.model.User;
import com.example.demo.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;


    @GetMapping("hello")
    public String hello(HttpServletRequest request) {
        return "Hello World";
    }

    @PostMapping("/api/login")
    public JWTToken login(@ModelAttribute User user, HttpServletResponse response) throws IOException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            if (authentication.isAuthenticated())
                return new JWTToken(jwtService.generateToken(user.getEmail()));
        } catch (BadCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("bad credentials");
            return null;
        }
        return null;
    }

    @PostMapping("api/register")
    public Object register(@ModelAttribute User user, HttpServletResponse response) throws Exception {
        try {
            userDetailsManager.createUser(new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of()));
            return new JWTToken(jwtService.generateToken(user.getEmail()));
        }catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("a user with that email already exists");
            return null;
        }

    }

}