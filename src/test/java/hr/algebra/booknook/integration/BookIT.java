package hr.algebra.booknook.integration;

import hr.algebra.booknook.entity.Book;
import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import hr.algebra.booknook.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void get_all_books_returns_array_with_expected_fields() throws Exception {
        mockMvc.perform(get("/api/books")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].author").exists());
    }


    @Test
    void admin_can_create_a_book_via_rest() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {"title":"test rest create","author":"Test Author","genre":"FANTASY",
                                    "format":"PAPERBACK","status":"WANT_TO_READ","pageCount":200,
                                    "ownedCopy":true,"partOfChallenge":false,"reReadable":false}
                                    """)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("test rest create"));
    }


    @Test
    void admin_can_update_a_book_via_rest() throws Exception {
        Book bookToUpdate = new Book();

        bookToUpdate.setAuthor("test");
        bookToUpdate.setTitle("test");
        bookToUpdate.setGenre(BookGenre.FANTASY);
        bookToUpdate.setFormat(BookFormat.PAPERBACK);
        bookToUpdate.setStatus(ReadingStatus.WANT_TO_READ);
        bookToUpdate = bookRepository.save(bookToUpdate);

        mockMvc.perform(put("/api/books/"+bookToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {"title":"test rest update","author":"Test Author","genre":"FANTASY",
                                    "format":"PAPERBACK","status":"WANT_TO_READ","pageCount":200,
                                    "ownedCopy":true,"partOfChallenge":false,"reReadable":false}
                                    """)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test rest update"));
    }

    @Test
    void admin_cannot_update_an_inexistant_book() throws Exception {
        mockMvc.perform(put("/api/books/95464684")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {"title":"test rest update unexistant","author":"Test Author","genre":"FANTASY",
                                    "format":"PAPERBACK","status":"WANT_TO_READ","pageCount":200,
                                    "ownedCopy":true,"partOfChallenge":false,"reReadable":false}
                                    """)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    void admin_can_delete_a_book_via_rest() throws Exception {
        Book bookToDelete = new Book();

        bookToDelete.setAuthor("test delete");
        bookToDelete.setTitle("test");
        bookToDelete.setGenre(BookGenre.FANTASY);
        bookToDelete.setFormat(BookFormat.PAPERBACK);
        bookToDelete.setStatus(ReadingStatus.WANT_TO_READ);
        bookToDelete = bookRepository.save(bookToDelete);

        mockMvc.perform(delete("/api/books/"+bookToDelete.getId())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }


    @Test
    void admin_cannot_delete_an_inexistant_book() throws Exception {
        mockMvc.perform(delete("/api/books/95464684")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }


    @Test
    void access_books_with_real_valid_jwt_token_succeeds() throws Exception {
        String loginBody = """
        {"username":"admin","password":"admin123"}
        """;

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String accessToken = com.jayway.jsonpath.JsonPath.read(loginResponse, "$.accessToken");

        mockMvc.perform(get("/api/books")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

}
