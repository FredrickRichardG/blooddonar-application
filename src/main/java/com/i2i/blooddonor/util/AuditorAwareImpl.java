package com.i2i.blooddonor.util;

import org.springframework.data.domain.AuditorAware;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("System");
//        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(Authentication::getName);
    }

}
