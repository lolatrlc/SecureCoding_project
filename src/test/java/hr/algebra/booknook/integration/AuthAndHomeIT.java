package hr.algebra.booknook.integration;

import hr.algebra.booknook.TestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthAndHomeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestFixtures testFixtures;

    @Test
    @DisplayName("/ is publicly accessible and redirects to /books")
    void index_isPublicAndRedirects() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    @DisplayName("the login page is public")
    void loginPage_isPublic() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void chech_if_user_is_logged_in() throws Exception {
        mockMvc.perform(formLogin("/auth/login").user("user").password(testFixtures.getUserPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void chech_if_admin_is_logged_in() throws Exception {
        mockMvc.perform(formLogin("/auth/login").user("admin").password(testFixtures.getAdminPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void chech_if_the_logged_in_fails_with_wrong_password() throws Exception {
        mockMvc.perform(formLogin("/auth/login").user("user").password("wrongPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?error=true"));
    }


    @Test
    void login_ok() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {"username":"admin","password":"admin123"}
                                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").exists());
    }

    @Test
    void refresh_with_invalid_token_returns_401() throws Exception {
        String body = """
        {"refreshToken":"this-token-does-not-exist"}
        """;

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void register_via_rest_succeeds() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {"username":"brandNewRestUser","email":"newrestuser@test.com","password":"password123"}
                                    """))
                .andExpect(status().isOk());
    }


    @Test
    void logout_via_rest_succeeds() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {"refreshToken":"any-token-value"}
                                    """))
                .andExpect(status().isOk());
    }

    @Test
    void unauthenticated_returns_401() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void request_with_non_bearer_auth_header_is_unauthorized() throws Exception {
        mockMvc.perform(get("/api/books")
                        .header("Authorization", "Basic "))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void register_via_mvc_with_valid_data_redirects_to_login() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "mvcNewUser1")
                        .param("email", "mvcnewuser1@test.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }


    @Test
    void register_via_mvc_with_blank_username_returns_to_form() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "")
                        .param("email", "validemail@test.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }


    @Test
    void register_via_mvc_with_duplicate_username_returns_to_form_with_error() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "admin")
                        .param("email", "completely-new-email@test.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}
