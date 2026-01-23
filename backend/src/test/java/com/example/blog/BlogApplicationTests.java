package com.example.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(properties = "spring.profiles.active=test") // use a test profile
class BlogApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures Spring context starts correctly
    }

    // Optional: Provide PasswordEncoder bean for test context
    @Configuration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
