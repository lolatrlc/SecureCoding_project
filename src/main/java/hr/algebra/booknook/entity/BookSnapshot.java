package hr.algebra.booknook.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class BookSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private String status;
    private Integer rating;
    private transient LocalDate exportedAt;

    public BookSnapshot() {
        this.exportedAt = LocalDate.now();
    }

    public BookSnapshot(String title, String author, String status, Integer rating) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.rating = rating;
        this.exportedAt = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSnapshot that = (BookSnapshot) o;
        return Objects.equals(title, that.title) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    @Override
    public String toString() {
        return "BookSnapshot{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                '}';
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public LocalDate getExportedAt() { return exportedAt; }
    public void setExportedAt(LocalDate exportedAt) { this.exportedAt = exportedAt; }
}