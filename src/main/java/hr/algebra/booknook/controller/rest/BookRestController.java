package hr.algebra.booknook.controller.rest;

import org.springframework.web.bind.annotation.*;
import hr.algebra.booknook.dto.BookDto;
import hr.algebra.booknook.entity.User;
import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import hr.algebra.booknook.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.NoSuchElementException;

import hr.algebra.booknook.security.SafeUrlValidator;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/books")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Books", description = "Book CRUD and search")
public class BookRestController {

    private final BookService bookService;
    private final SafeUrlValidator safeUrlValidator;

    public BookRestController(BookService bookService, SafeUrlValidator safeUrlValidator) {
        this.bookService = bookService;
        this.safeUrlValidator = safeUrlValidator;
    }

    @GetMapping
    @Operation(summary = "Get all books")
    public ResponseEntity<List<BookDto>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single book by ID")
    public ResponseEntity<BookDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookService.findById(id));
        } catch (NoSuchElementException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by title, author, series, genre, format, or status")
    public ResponseEntity<List<BookDto>> search(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) BookGenre genre,
        @RequestParam(required = false) BookFormat format,
        @RequestParam(required = false) ReadingStatus status
    ) {
        return ResponseEntity.ok(bookService.search(query, genre, format, status));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new book (admin only)")
    public ResponseEntity<BookDto> create(
        @Valid @RequestBody BookDto dto,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(dto, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a book (admin only)")
    public ResponseEntity<BookDto> update(
        @PathVariable Long id,
        @Valid @RequestBody BookDto dto
    ) {
        try {
            return ResponseEntity.ok(bookService.update(id, dto));
        } catch (NoSuchElementException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a book (admin only)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            bookService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException _) {
            return ResponseEntity.notFound().build();
        }
    }


    // VULNERABLE endpoint
    @GetMapping("/preview-vulnerable")
    public ResponseEntity<String> previewVulnerable(@RequestParam String url) {
        return ResponseEntity.ok("Fetching URL: " + url);
    }

    // SECURE endpoint
    @GetMapping("/preview-secure")
    public ResponseEntity<String> previewSecure(@RequestParam String url) {
        try {
            safeUrlValidator.validateOrThrow(url);
            return ResponseEntity.ok("URL is safe: " + url);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body("Blocked: " + ex.getMessage());
        }
    }
}
