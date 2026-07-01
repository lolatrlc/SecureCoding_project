package hr.algebra.booknook.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SafeUrlIT {

    @Autowired
    private MockMvc mockMvc;

    // Teste la branche : url vide ou blanche -> rejetée
    @Test
    void preview_secure_rejects_blank_url() throws Exception {
        mockMvc.perform(get("/api/books/preview-secure").param("url", ""))
                .andExpect(status().isBadRequest());
    }

    // Teste la branche : scheme non-https -> rejetée
    @Test
    void preview_secure_rejects_non_https_url() throws Exception {
        mockMvc.perform(get("/api/books/preview-secure")
                        .param("url", "http://images.unsplash.com/photo.jpg"))
                .andExpect(status().isBadRequest());
    }

    // Teste la branche : host pas dans la whitelist -> rejetée
    @Test
    void preview_secure_rejects_non_whitelisted_host() throws Exception {
        mockMvc.perform(get("/api/books/preview-secure")
                        .param("url", "https://evil-site.com/malicious.jpg"))
                .andExpect(status().isBadRequest());
    }

    // Teste la branche : url valide et whitelistée -> acceptée
    @Test
    void preview_secure_accepts_valid_whitelisted_url() throws Exception {
        mockMvc.perform(get("/api/books/preview-secure")
                        .param("url", "https://raw.githubusercontent.com/test/repo/image.png"))
                .andExpect(status().isOk());
    }
}