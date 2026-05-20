package com.javaapp.finance.service;

import com.javaapp.finance.repository.UserRepository;
import com.javaapp.finance.security.AuthUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.javaapp.finance.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthUserDetailsService authUserDetailsService;

    @Test
    void loadUserByUsername_whenExists_shouldReturnAuthUser() {
        when(userRepository.findByEmail(USER_EMAIL))
                .thenReturn(Optional.of(createUser()));

        var result = authUserDetailsService.loadUserByUsername(USER_EMAIL);

        assertThat(result).isInstanceOf(AuthUser.class);
        assertThat(result.getUsername()).isEqualTo(USER_EMAIL);
        assertThat(result.getPassword()).isEqualTo(USER_ENCODED_PASSWORD);
    }

    @Test
    void loadUserByUsername_whenNotFound_shouldThrow() {
        when(userRepository.findByEmail("x@x.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                authUserDetailsService.loadUserByUsername("x@x.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found: x@x.com");
    }

    @Test
    void loadUserByUsername_shouldHaveRoleUser() {
        when(userRepository.findByEmail(USER_EMAIL))
                .thenReturn(Optional.of(createUser()));

        var result = authUserDetailsService.loadUserByUsername(USER_EMAIL);

        assertThat(result.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
    }
}
