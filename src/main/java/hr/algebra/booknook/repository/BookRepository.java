package hr.algebra.booknook.repository;

import hr.algebra.booknook.entity.Book;
import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
        SELECT b FROM Book b
        WHERE (:query IS NULL OR LOWER(b.title)      LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(b.author)     LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(b.seriesName) LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:genre IS NULL OR b.genre = :genre)
          AND (:format IS NULL OR b.format = :format)
          AND (:status IS NULL OR b.status = :status)
        ORDER BY b.status ASC, b.author ASC, b.title ASC
        """)
    List<Book> search(
        @Param("query") String query,
        @Param("genre") BookGenre genre,
        @Param("format") BookFormat format,
        @Param("status") ReadingStatus status
    );

    List<Book> findAllByOrderByStatusAscAuthorAscTitleAsc();
}
