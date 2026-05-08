package hr.algebra.booknook.service;

import hr.algebra.booknook.dto.BookDto;
import hr.algebra.booknook.entity.Book;
import hr.algebra.booknook.entity.User;
import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import hr.algebra.booknook.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDto> findAll() {
        return bookRepository.findAllByOrderByStatusAscAuthorAscTitleAsc()
            .stream()
            .map(BookDto::from)
            .toList();
    }

    public BookDto findById(Long id) {
        return bookRepository.findById(id)
            .map(BookDto::from)
            .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
    }

    public List<BookDto> search(String query, BookGenre genre, BookFormat format, ReadingStatus status) {
        String nq = (query != null && query.isBlank()) ? null : query;
        return bookRepository.search(nq, genre, format, status)
            .stream()
            .map(BookDto::from)
            .toList();
    }

    @Transactional
    public BookDto create(BookDto dto, User creator) {
        Book book = new Book();
        dto.applyTo(book);
        book.setAddedBy(creator);
        return BookDto.from(bookRepository.save(book));
    }

    @Transactional
    public BookDto update(Long id, BookDto dto) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
        dto.applyTo(book);
        return BookDto.from(bookRepository.save(book));
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
    }
}
