package com.i2i.blooddonor.util;

import com.i2i.blooddonor.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final CustomUserDetailService customUserDetailService;

    public JwtAuthenticationFilter(JwtConfig jwtConfig, CustomUserDetailService customUserDetailService){
        this.jwtConfig=jwtConfig;
        this.customUserDetailService=customUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username=null;
        String jwt=null;
        System.out.println("---authHeader token-a- "+authHeader);
        if(authHeader!=null &&authHeader.startsWith("Bearer")){
            jwt = authHeader.substring(7);
            username = jwtConfig.extractUserName(jwt);
        }

        if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
            if(jwtConfig.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            }

        }
        filterChain.doFilter(request,response);

    }
}
