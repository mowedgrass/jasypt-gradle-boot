package com.github.mowedgrass.jasyptgradleboot.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Component
    private static class Runner implements CommandLineRunner {

        @Value("${app.secret}")
        private String secret;

        @Override
        public void run(String... args) throws Exception {
            System.out.format("Secret is '%s'", secret);
        }
    }
}
