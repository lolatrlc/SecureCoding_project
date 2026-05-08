package hr.algebra.booknook.entity;

import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 250)
    @Column(nullable = false, length = 250)
    private String title;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String author;

    @Size(max = 200)
    private String seriesName;

    @Min(0) @Max(50)
    private Integer seriesNumber;

    @Size(max = 100)
    private String publisher;

    @Min(1000) @Max(2100)
    private Integer publicationYear;

    @Size(max = 30)
    private String isbn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookGenre genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookFormat format;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus status;

    @Min(1) @Max(10000)
    private Integer pageCount;

    @Min(0) @Max(10000)
    private Integer currentPage;

    @Min(1) @Max(5)
    private Integer rating;

    @Column(nullable = false)
    private boolean ownedCopy = true;

    @Column(nullable = false)
    private boolean partOfChallenge = false;

    @Column(nullable = false)
    private boolean reReadable = false;

    private LocalDate startedReading;
    private LocalDate finishedReading;
    private LocalDate addedDate;

    @Size(max = 200)
    private String moodAtmosphere;

    @Size(max = 300)
    @Column(length = 300)
    private String triggerWarnings;

    @Size(max = 500)
    @Column(length = 500)
    private String favoriteQuote;

    @Size(max = 2000)
    @Column(length = 2000)
    private String review;

    @Size(max = 2000)
    @Column(length = 2000)
    private String personalNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_id")
    private User addedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (addedDate == null) addedDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }
    public String getTitle()                         { return title; }
    public void setTitle(String title)               { this.title = title; }
    public String getAuthor()                        { return author; }
    public void setAuthor(String author)             { this.author = author; }
    public String getSeriesName()                    { return seriesName; }
    public void setSeriesName(String seriesName)     { this.seriesName = seriesName; }
    public Integer getSeriesNumber()                 { return seriesNumber; }
    public void setSeriesNumber(Integer n)           { this.seriesNumber = n; }
    public String getPublisher()                     { return publisher; }
    public void setPublisher(String publisher)       { this.publisher = publisher; }
    public Integer getPublicationYear()              { return publicationYear; }
    public void setPublicationYear(Integer year)     { this.publicationYear = year; }
    public String getIsbn()                          { return isbn; }
    public void setIsbn(String isbn)                 { this.isbn = isbn; }
    public BookGenre getGenre()                      { return genre; }
    public void setGenre(BookGenre genre)            { this.genre = genre; }
    public BookFormat getFormat()                    { return format; }
    public void setFormat(BookFormat format)         { this.format = format; }
    public ReadingStatus getStatus()                 { return status; }
    public void setStatus(ReadingStatus status)      { this.status = status; }
    public Integer getPageCount()                    { return pageCount; }
    public void setPageCount(Integer pageCount)      { this.pageCount = pageCount; }
    public Integer getCurrentPage()                  { return currentPage; }
    public void setCurrentPage(Integer currentPage)  { this.currentPage = currentPage; }
    public Integer getRating()                       { return rating; }
    public void setRating(Integer rating)            { this.rating = rating; }
    public boolean isOwnedCopy()                     { return ownedCopy; }
    public void setOwnedCopy(boolean ownedCopy)      { this.ownedCopy = ownedCopy; }
    public boolean isPartOfChallenge()               { return partOfChallenge; }
    public void setPartOfChallenge(boolean p)        { this.partOfChallenge = p; }
    public boolean isReReadable()                    { return reReadable; }
    public void setReReadable(boolean reReadable)    { this.reReadable = reReadable; }
    public LocalDate getStartedReading()             { return startedReading; }
    public void setStartedReading(LocalDate d)       { this.startedReading = d; }
    public LocalDate getFinishedReading()            { return finishedReading; }
    public void setFinishedReading(LocalDate d)      { this.finishedReading = d; }
    public LocalDate getAddedDate()                  { return addedDate; }
    public void setAddedDate(LocalDate d)            { this.addedDate = d; }
    public String getMoodAtmosphere()                { return moodAtmosphere; }
    public void setMoodAtmosphere(String m)          { this.moodAtmosphere = m; }
    public String getTriggerWarnings()               { return triggerWarnings; }
    public void setTriggerWarnings(String t)         { this.triggerWarnings = t; }
    public String getFavoriteQuote()                 { return favoriteQuote; }
    public void setFavoriteQuote(String f)           { this.favoriteQuote = f; }
    public String getReview()                        { return review; }
    public void setReview(String review)             { this.review = review; }
    public String getPersonalNotes()                 { return personalNotes; }
    public void setPersonalNotes(String p)           { this.personalNotes = p; }
    public User getAddedBy()                         { return addedBy; }
    public void setAddedBy(User addedBy)             { this.addedBy = addedBy; }
    public LocalDateTime getCreatedAt()              { return createdAt; }
    public void setCreatedAt(LocalDateTime t)        { this.createdAt = t; }
    public LocalDateTime getUpdatedAt()              { return updatedAt; }
    public void setUpdatedAt(LocalDateTime t)        { this.updatedAt = t; }
}
