package hr.algebra.booknook.controller.mvc;

import org.springframework.web.bind.annotation.*;
import hr.algebra.booknook.dto.BookDto;
import hr.algebra.booknook.entity.User;
import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import hr.algebra.booknook.service.BookService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/books")
public class BookMvcController {

    private final BookService bookService;

    private static final String GENRES = "genres";
    private static final String FORMATS = "formats";
    private static final String STATUSES = "statuses";
    private static final String REDIRECT_BOOKS = "redirect:/books";
    private static final String EDIT_MODE = "editMode";
    private static final String BOOK_FORM = "books/form";
    private static final String SUCCESS_MESSAGE = "successMessage";

    public BookMvcController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) BookGenre genre,
        @RequestParam(required = false) BookFormat format,
        @RequestParam(required = false) ReadingStatus status,
        Model model
    ) {
        boolean searching = query != null || genre != null || format != null || status != null;
        model.addAttribute("books", searching
            ? bookService.search(query, genre, format, status)
            : bookService.findAll());
        model.addAttribute(GENRES, BookGenre.values());
        model.addAttribute(FORMATS, BookFormat.values());
        model.addAttribute(STATUSES, ReadingStatus.values());
        model.addAttribute("query", query);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedFormat", format);
        model.addAttribute("selectedStatus", status);
        return "books/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("book", bookService.findById(id));
            return "books/detail";
        } catch (NoSuchElementException _) {
            return REDIRECT_BOOKS;
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newForm(Model model) {
        model.addAttribute("book", new BookDto(
            null, "", "", "", null, "", null, "",
                BookGenre.FANTASY, BookFormat.HARDCOVER, ReadingStatus.DNF, null, null, null,
            true, false, false, null, null, null,
            "", "", "", "", "", null, null, null
        ));
        model.addAttribute(GENRES, BookGenre.values());
        model.addAttribute(FORMATS, BookFormat.values());
        model.addAttribute(STATUSES, ReadingStatus.values());
        model.addAttribute(EDIT_MODE, false);
        return BOOK_FORM;
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(
        @Valid @ModelAttribute("book") BookDto dto,
        BindingResult result,
        @AuthenticationPrincipal User currentUser,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute(GENRES, BookGenre.values());
            model.addAttribute(FORMATS, BookFormat.values());
            model.addAttribute(STATUSES, ReadingStatus.values());
            model.addAttribute(EDIT_MODE, false);
            return BOOK_FORM;
        }
        bookService.create(dto, currentUser);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Book added to your library.");
        return REDIRECT_BOOKS;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("book", bookService.findById(id));
            model.addAttribute(GENRES, BookGenre.values());
            model.addAttribute(FORMATS, BookFormat.values());
            model.addAttribute(STATUSES, ReadingStatus.values());
            model.addAttribute(EDIT_MODE, true);
            return BOOK_FORM;
        } catch (NoSuchElementException _) {
            return REDIRECT_BOOKS;
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("book") BookDto dto,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute(GENRES, BookGenre.values());
            model.addAttribute(FORMATS, BookFormat.values());
            model.addAttribute(STATUSES, ReadingStatus.values());
            model.addAttribute(EDIT_MODE, true);
            return BOOK_FORM;
        }
        bookService.update(id, dto);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Book updated.");
        return REDIRECT_BOOKS;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.delete(id);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Book removed.");
        return REDIRECT_BOOKS;
    }
}
