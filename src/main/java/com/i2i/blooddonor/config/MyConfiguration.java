package com.i2i.blooddonor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;



@Component
@RefreshScope
public class MyConfiguration {

    @Value("${db.username}")
    public String username;
    @Value("${db.password}")
    public String password;

}
