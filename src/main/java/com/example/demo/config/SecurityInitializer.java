package com.example.demo.config;

import com.example.demo.entity.CustomUser;
import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

@Configuration
public class SecurityInitializer {

    @Value("${INITIAL_USER:user}")
    private String initialUser;

    @Value("${INITIAL_PASSWORD:password}")
    private String initialPassword;

    @Bean
    CommandLineRunner initSecurity(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create Roles if not exist
            Role roleUser = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
                Role r = new Role(); r.setName("ROLE_USER"); return roleRepository.save(r);
            });
            Role roleMonitoring = roleRepository.findByName("ROLE_MONITORING").orElseGet(() -> {
                Role r = new Role(); r.setName("ROLE_MONITORING"); return roleRepository.save(r);
            });

            // Create Initial User if not exist
            if (userRepository.findByUsername(initialUser).isEmpty()) {
                CustomUser user = new CustomUser();
                user.setUsername(initialUser);
                user.setPassword(passwordEncoder.encode(initialPassword));
                user.setRoles(new HashSet<>(Collections.singletonList(roleUser)));
                userRepository.save(user);
            }

            // Create Monitoring User if not exist
            if (userRepository.findByUsername("monitoring").isEmpty()) {
                CustomUser monitoring = new CustomUser();
                monitoring.setUsername("monitoring");
                monitoring.setPassword(passwordEncoder.encode(initialPassword));
                monitoring.setRoles(new HashSet<>(Collections.singletonList(roleMonitoring)));
                userRepository.save(monitoring);
            }
        };
    }
}
