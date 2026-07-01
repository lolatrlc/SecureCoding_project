package hr.algebra.booknook.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SerializationIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void serialize_returns_base64_payload() throws Exception {
        mockMvc.perform(get("/api/serialization/serialize"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullBase64").exists());
    }


    @Test
    void serialize_bad_thing_returns_base64_payload() throws Exception {
        mockMvc.perform(get("/api/serialization/serialize-badthing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullBase64").exists());
    }


    @Test
    void deserialize_rejects_invalid_magic_bytes() throws Exception {
        String fakeBase64 = java.util.Base64.getEncoder()
                .encodeToString("not a real payload".getBytes());

        String body = """
        {"base64":"%s"}
        """.formatted(fakeBase64);

        mockMvc.perform(post("/api/serialization/deserialize-safe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deserialize_accepts_whitelisted_class() throws Exception {
        String serializeResponse = mockMvc.perform(get("/api/serialization/serialize"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String fullBase64 = com.jayway.jsonpath.JsonPath.read(serializeResponse, "$.fullBase64");

        String body = """
        {"base64":"%s"}
        """.formatted(fullBase64);

        mockMvc.perform(post("/api/serialization/deserialize-safe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }





}
