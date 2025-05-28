package com.i2i.blooddonor.config;

import org.springframework.core.convert.converter.Converter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration

public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // basic
//
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/blooddonors/findAllMemberDetail","/blooddonors/updateLastDonatedIsToday","/blooddonors/findSpecificBloodGroupMember","/blooddonors/eligibleMemberRegToBloodGroup","/blooddonors/findMemberByIdEntity","/blooddonors/eligibleMember","/blooddonors/findAllWithPaginator","/blooddonors/findAll","/blooddonors/findMemberById").permitAll() // Allow public APIs
//                        .requestMatchers("/blooddonors/welcome", "/blooddonors/newMember","/blooddonors/patchMember","/blooddonors/deleteMemberById","/blooddonors/updateMember").hasRole("admin")
//                        .anyRequest().authenticated()
//                ).httpBasic(Customizer.withDefaults());


        // SSO

//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/blooddonors/findAllMemberDetail","/blooddonors/updateLastDonatedIsToday","/blooddonors/findSpecificBloodGroupMember","/blooddonors/eligibleMemberRegToBloodGroup","/blooddonors/findMemberByIdEntity","/blooddonors/eligibleMember","/blooddonors/findAllWithPaginator","/blooddonors/findAll","/blooddonors/findMemberById").permitAll() // Allow public APIs
//                        .requestMatchers("/blooddonors/welcome", "/blooddonors/newMember","/blooddonors/patchMember","/blooddonors/deleteMemberById","/blooddonors/updateMember").hasRole("admin")
//                        .anyRequest().authenticated()
//                ).with(oauth2Login -> oauth2Login
//                                .loginPage("/oauth2/authorization/keycloak")
//                                );


        // oauth2.0

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/blooddonors/findAllMemberDetail","/blooddonors/updateLastDonatedIsToday","/blooddonors/findSpecificBloodGroupMember","/blooddonors/eligibleMemberRegToBloodGroup","/blooddonors/findMemberByIdEntity","/blooddonors/eligibleMember","/blooddonors/findAllWithPaginator","/blooddonors/findAll","/blooddonors/findMemberById").permitAll() // Allow public APIs
                        .requestMatchers("/blooddonors/welcome", "/blooddonors/newMember","/blooddonors/patchMember","/blooddonors/deleteMemberById","/blooddonors/updateMember").hasRole("admin")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .oauth2ResourceServer(oauth2->oauth2.jwt(Customizer.withDefaults()))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                );

        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<String> roles = ((Collection<String>) jwt.getClaimAsMap("realm_access").get("roles"));
            return roles.stream()
                    .map(role -> "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return converter;

    }


    // Basic

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user = User.builder().username("").password().
//
//    }

}