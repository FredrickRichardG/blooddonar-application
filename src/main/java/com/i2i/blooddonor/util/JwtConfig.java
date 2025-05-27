package com.i2i.blooddonor.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
@Configuration
public class JwtConfig {
//
//    @Value("${jwt.secret}")
//    private String secret;
    private String secret="mys^ecT%ret%ke$of@@6@4bytesw@hic_hh#el#psins2ec@@!ur@i@t#y_mys^ecT%ret%ke$of@@6@4bytesw@hic_hh#el#psins2ec@@!ur@i@t#y";

    private final SecretKey signingKey =Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));


    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(signingKey,SignatureAlgorithm.HS512)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaim(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaim(String token){
        System.out.println("---extractAllClaim token-a- "+token);
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);

    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//    private Key getSignKey() {
//        return Keys.hmacShaKeyFor(secretKeyc.getBytes(StandardCharsets.UTF_8));
//    }
}
