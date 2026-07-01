package hr.algebra.booknook.unit;

import hr.algebra.booknook.TestFixtures;
import hr.algebra.booknook.dto.Dto;
import hr.algebra.booknook.repository.UserRepository;
import hr.algebra.booknook.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_throws_when_username_already_taken() {
        Dto.RegisterRequest request = new Dto.RegisterRequest(
                TestFixtures.ADMIN_USERNAME, "newemail@test.com", "password123");

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_throws_when_email_already_taken() {
        Dto.RegisterRequest request = new Dto.RegisterRequest(
                "newUser", "admin@booknook.hr", "password123");

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_with_new_credentials() {
        Dto.RegisterRequest request = new Dto.RegisterRequest(
                "newUser", "newuser@booknook.hr", "password123");

        authService.register(request);

        assertTrue(userRepository.existsByUsername("newUser"));
    }
}
