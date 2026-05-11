package com.rakesh.smartcity.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

public class JwtFilterChainService extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //  Get Authorization header
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        //Check header format
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // remove "Bearer "
            email = jwtService.extractEmail(token); // extract email
        }

        //If email exists & not authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //STEP 4: Load user from DB
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);


            System.out.println("Authorities: " + userDetails.getAuthorities());

            //STEP 5: Validate token
            if (jwtService.isTokenValid(token, userDetails.getUsername())) {


                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );


                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Continue request
        filterChain.doFilter(request, response);
    }
}
