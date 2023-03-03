package com.olymp_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAspectJAutoProxy
@EnableJpaRepositories
@SpringBootApplication
public class OlympProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OlympProjectApplication.class, args);
    }

}
