package hr.algebra.booknook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestFixtures {
    public static final String ADMIN_USERNAME = "admin";
    public static final String USER_USERNAME = "user";

    @Value("${app.init.admin-password}")
    private String adminPassword;

    @Value("${app.init.user-password}")
    private String userPassword;

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
