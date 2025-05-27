package com.i2i.blooddonor.controller;

import com.i2i.blooddonor.util.JwtConfig;
import com.i2i.blooddonor.model.AuthResponse;
import com.i2i.blooddonor.requestModel.AuthRequest;
import com.i2i.blooddonor.service.CustomUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mapping")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtConfig jwtConfig;
    private final CustomUserDetailService customUserDetailService;

    public AuthController(AuthenticationManager authenticationManager,JwtConfig jwtConfig,CustomUserDetailService customUserDetailService){
        this.authenticationManager=authenticationManager;
        this.jwtConfig=jwtConfig;
        this.customUserDetailService=customUserDetailService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?>generateToken(@RequestBody AuthRequest authRequest){

        try{
            log.info("-----authenticTE----x" + authRequest.getUsername() + authRequest.getPassword());
            Authentication authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),authRequest.getPassword()
                    )
            );
            log.info("-----authenticTE COMPLETE----x");


        final UserDetails userDetails = customUserDetailService.loadUserByUsername(authRequest.getUsername());

        final String token = jwtConfig.generateToken(userDetails.getUsername());
        log.info("-----token----x", token);
        return ResponseEntity.ok(new AuthResponse(token));

        }catch (AuthenticationException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credential");
        }

    }

}
