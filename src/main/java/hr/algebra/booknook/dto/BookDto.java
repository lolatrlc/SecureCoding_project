package hr.algebra.booknook.dto;

import jakarta.validation.constraints.*;
import hr.algebra.booknook.entity.Book;
import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Book data transfer object")
public record BookDto(

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @NotBlank @Size(max = 250)
    @Schema(description = "Book title", example = "The Seven Husbands of Evelyn Hugo")
    String title,

    @NotBlank @Size(max = 200)
    @Schema(description = "Author name", example = "Taylor Jenkins Reid")
    String author,

    @Size(max = 200)
    @Schema(description = "Series name (if part of one)", example = "Throne of Glass")
    String seriesName,

    @Min(0) @Max(50)
    @Schema(description = "Number in series", example = "1")
    Integer seriesNumber,

    @Size(max = 100)
    @Schema(description = "Publisher", example = "Atria Books")
    String publisher,

    @Min(1000) @Max(2100)
    @Schema(description = "Publication year", example = "2017")
    Integer publicationYear,

    @Size(max = 30)
    @Schema(description = "ISBN", example = "978-1501161933")
    String isbn,

    @NotNull
    @Schema(description = "Genre")
    BookGenre genre,

    @NotNull
    @Schema(description = "Format")
    BookFormat format,

    @NotNull
    @Schema(description = "Reading status")
    ReadingStatus status,

    @Min(1) @Max(10000)
    @Schema(description = "Total page count", example = "400")
    Integer pageCount,

    @Min(0) @Max(10000)
    @Schema(description = "Current page (for currently reading)", example = "127")
    Integer currentPage,

    @Min(1) @Max(5)
    @Schema(description = "Personal rating 1-5 stars", example = "5")
    Integer rating,

    @Schema(description = "Do you own a physical or digital copy")
    boolean ownedCopy,

    @Schema(description = "Part of a yearly reading challenge")
    boolean partOfChallenge,

    @Schema(description = "Worth re-reading someday")
    boolean reReadable,

    @Schema(description = "Date you started reading")
    LocalDate startedReading,

    @Schema(description = "Date you finished")
    LocalDate finishedReading,

    @Schema(description = "Date you added the book")
    LocalDate addedDate,

    @Size(max = 200)
    @Schema(description = "Mood / atmosphere of the book", example = "Cozy autumn vibes, dark academia")
    String moodAtmosphere,

    @Size(max = 300)
    @Schema(description = "Trigger warnings / content notes")
    String triggerWarnings,

    @Size(max = 500)
    @Schema(description = "Favorite quote from the book")
    String favoriteQuote,

    @Size(max = 2000)
    @Schema(description = "Personal review")
    String review,

    @Size(max = 2000)
    @Schema(description = "Private notes (annotations, thoughts)")
    String personalNotes,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    String addedBy,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime createdAt,

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime updatedAt
) {
    public static BookDto from(Book b) {
        return new BookDto(
            b.getId(),
            b.getTitle(),
            b.getAuthor(),
            b.getSeriesName(),
            b.getSeriesNumber(),
            b.getPublisher(),
            b.getPublicationYear(),
            b.getIsbn(),
            b.getGenre(),
            b.getFormat(),
            b.getStatus(),
            b.getPageCount(),
            b.getCurrentPage(),
            b.getRating(),
            b.isOwnedCopy(),
            b.isPartOfChallenge(),
            b.isReReadable(),
            b.getStartedReading(),
            b.getFinishedReading(),
            b.getAddedDate(),
            b.getMoodAtmosphere(),
            b.getTriggerWarnings(),
            b.getFavoriteQuote(),
            b.getReview(),
            b.getPersonalNotes(),
            b.getAddedBy() != null ? b.getAddedBy().getUsername() : null,
            b.getCreatedAt(),
            b.getUpdatedAt()
        );
    }

    public void applyTo(Book b) {
        b.setTitle(title);
        b.setAuthor(author);
        b.setSeriesName(seriesName);
        b.setSeriesNumber(seriesNumber);
        b.setPublisher(publisher);
        b.setPublicationYear(publicationYear);
        b.setIsbn(isbn);
        b.setGenre(genre);
        b.setFormat(format);
        b.setStatus(status);
        b.setPageCount(pageCount);
        b.setCurrentPage(currentPage);
        b.setRating(rating);
        b.setOwnedCopy(ownedCopy);
        b.setPartOfChallenge(partOfChallenge);
        b.setReReadable(reReadable);
        b.setStartedReading(startedReading);
        b.setFinishedReading(finishedReading);
        b.setAddedDate(addedDate);
        b.setMoodAtmosphere(moodAtmosphere);
        b.setTriggerWarnings(triggerWarnings);
        b.setFavoriteQuote(favoriteQuote);
        b.setReview(review);
        b.setPersonalNotes(personalNotes);
    }
}
