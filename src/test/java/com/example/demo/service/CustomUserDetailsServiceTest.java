package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entity.CustomUser;
import com.example.demo.entity.Role;
import com.example.demo.repository.UserRepository;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_Success() {
        String username = "testuser";
        CustomUser user = new CustomUser();
        ReflectionTestUtils.setField(user, "username", username);
        ReflectionTestUtils.setField(user, "password", "password");
        
        Role role = new Role();
        role.setName("ROLE_USER");
        ReflectionTestUtils.setField(user, "roles", Set.of(role));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
    }

    @Test
    void loadUserByUsername_NotFound() {
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(username))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("User not found");
    }
}
