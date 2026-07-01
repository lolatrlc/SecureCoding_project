package hr.algebra.booknook.integration;

import hr.algebra.booknook.TestFixtures;
import hr.algebra.booknook.config.DataInitializer;
import hr.algebra.booknook.entity.Book;
import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import hr.algebra.booknook.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class SecureControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestFixtures testFixtures;

    @Test
    void check_if_anonymous_user_cannot_access_secure_pages() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void check_if_user_gets_403_when_trying_admin_page() throws Exception {
        mockMvc.perform(get("/books/new").with(user("user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void user_cannot_delete_a_book() throws Exception {
        mockMvc.perform(post("/books/delete/3").with(user("user"))
                .with(csrf()))
                .andExpect(status().isForbidden());

        assertTrue(bookRepository.existsById(3L));
    }


    @Test
    void admin_can_delete_a_book() throws Exception {
        Book bookToDelete = new Book();

        bookToDelete.setAuthor("test");
        bookToDelete.setTitle("test");
        bookToDelete.setGenre(BookGenre.FANTASY);
        bookToDelete.setFormat(BookFormat.PAPERBACK);
        bookToDelete.setStatus(ReadingStatus.WANT_TO_READ);
        bookToDelete = bookRepository.save(bookToDelete);

        mockMvc.perform(post("/books/delete/"+bookToDelete.getId()).with(user("admin").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        assertFalse(bookRepository.existsById(bookToDelete.getId()));
    }


    @Test
    void admin_can_create_a_book() throws Exception {
        mockMvc.perform(post("/books/new").with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("title", "test create")
                        .param("author", "test")
                        .param("genre", "FANTASY")
                        .param("format", "PAPERBACK")
                        .param("status", "WANT_TO_READ")
                        .param("ownedCopy", "true")
                        .param("partOfChallenge", "false")
                        .param("reReadable", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        assertTrue(bookRepository.existsByTitle("test create"));
    }



    @Test
    void admin_can_update_a_book() throws Exception {
        Book bookToUpdate = new Book();

        bookToUpdate.setAuthor("test");
        bookToUpdate.setTitle("test");
        bookToUpdate.setGenre(BookGenre.FANTASY);
        bookToUpdate.setFormat(BookFormat.PAPERBACK);
        bookToUpdate.setStatus(ReadingStatus.WANT_TO_READ);
        bookToUpdate = bookRepository.save(bookToUpdate);

        mockMvc.perform(post("/books/edit/"+bookToUpdate.getId()).with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("title", "test update")
                        .param("author", "test update")
                        .param("genre", "FANTASY")
                        .param("format", "PAPERBACK")
                        .param("status", "WANT_TO_READ")
                        .param("ownedCopy", "true")
                        .param("partOfChallenge", "false")
                        .param("reReadable", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        assertTrue(bookRepository.existsByTitle("test update"));
    }



    // on teste la branche result.hasErrors()
    @Test
    void create_with_blank_title_returns_to_form() throws Exception {
        mockMvc.perform(post("/books/new")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "")
                        .param("author", "Some Author")
                        .param("genre", "FANTASY")
                        .param("format", "PAPERBACK")
                        .param("status", "WANT_TO_READ")
                        .param("ownedCopy", "false")
                        .param("partOfChallenge", "false")
                        .param("reReadable", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"));
    }

    // À ajouter dans SecureControllerIT, à côté de create_with_blank_title_returns_to_form

    @Test
    void update_with_blank_title_returns_to_form() throws Exception {
        Book existingBook = new Book();
        existingBook.setAuthor("test");
        existingBook.setTitle("test");
        existingBook.setGenre(BookGenre.FANTASY);
        existingBook.setFormat(BookFormat.PAPERBACK);
        existingBook.setStatus(ReadingStatus.WANT_TO_READ);
        existingBook = bookRepository.save(existingBook);

        mockMvc.perform(post("/books/edit/" + existingBook.getId())
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("id", existingBook.getId().toString())
                        .param("title", "")
                        .param("author", "Some Author")
                        .param("genre", "FANTASY")
                        .param("format", "PAPERBACK")
                        .param("status", "WANT_TO_READ")
                        .param("ownedCopy", "false")
                        .param("partOfChallenge", "false")
                        .param("reReadable", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"));
    }



    @Test
    void detail_redirects_when_book_does_not_exist() throws Exception {
        mockMvc.perform(get("/books/99999")
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void edit_form_redirects_when_book_does_not_exist() throws Exception {
        mockMvc.perform(get("/books/edit/99999")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }



    //MVC ENCORE
    @Test
    void new_form_displays_empty_book_form() throws Exception {
        mockMvc.perform(get("/books/new")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"))
                .andExpect(model().attribute("editMode", false));
    }

    @Test
    void list_with_query_param_uses_search() throws Exception {
        mockMvc.perform(get("/books")
                        .param("query", "Dune")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"));
    }

    @Test
    void list_with_genre_filter_uses_search() throws Exception {
        mockMvc.perform(get("/books")
                        .param("genre", "FANTASY")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void list_with_format_filter_uses_search() throws Exception {
        mockMvc.perform(get("/books")
                        .param("format", "PAPERBACK")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void list_with_status_filter_uses_search() throws Exception {
        mockMvc.perform(get("/books")
                        .param("status", "WANT_TO_READ")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk());
    }
}
