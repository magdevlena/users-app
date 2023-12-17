package com.github.magdevlena.usersservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class UsersServiceApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(UsersServiceApplication.class, args);
        log.info("Application started on port " + context.getEnvironment().getProperty("server.port"));
    }

}
