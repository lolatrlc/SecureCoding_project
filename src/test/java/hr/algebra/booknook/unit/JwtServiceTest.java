package hr.algebra.booknook.unit;

import hr.algebra.booknook.TestFixtures;
import hr.algebra.booknook.entity.User;
import hr.algebra.booknook.repository.UserRepository;
import hr.algebra.booknook.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void isValid_returns_false_for_garbage_token() {
        User admin = userRepository.findByUsername(TestFixtures.ADMIN_USERNAME).orElseThrow();

        boolean result = jwtService.isValid("not.a.valid.token", admin);

        assertFalse(result);
    }

    @Test
    void isValid_returns_false_when_username_does_not_match() {
        User admin = userRepository.findByUsername(TestFixtures.ADMIN_USERNAME).orElseThrow();
        User user = userRepository.findByUsername(TestFixtures.USER_USERNAME).orElseThrow();

        String tokenForAdmin = jwtService.generateAccessToken(admin);

        boolean result = jwtService.isValid(tokenForAdmin, user);

        assertFalse(result);
    }
}