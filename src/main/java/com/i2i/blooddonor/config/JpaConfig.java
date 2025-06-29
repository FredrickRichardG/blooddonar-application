package com.i2i.blooddonor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class JpaConfig {

    public AuditorAwareImpl auditorAware(){
        return new AuditorAwareImpl();
    }
}
