package hr.algebra.booknook.unit;

import hr.algebra.booknook.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void findById_throws_when_book_does_not_exist() {
        assertThrows(NoSuchElementException.class, () -> bookService.findById(99999L));
    }
}