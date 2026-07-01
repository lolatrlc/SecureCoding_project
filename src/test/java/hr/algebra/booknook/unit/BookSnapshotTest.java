package hr.algebra.booknook.unit;

import hr.algebra.booknook.entity.BookSnapshot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookSnapshotTest {

    @Test
    void constructor_sets_all_fields() {
        BookSnapshot snapshot = new BookSnapshot("Dune", "Frank Herbert", "FINISHED", 5);

        assertEquals("Dune", snapshot.getTitle());
        assertEquals("Frank Herbert", snapshot.getAuthor());
        assertEquals("FINISHED", snapshot.getStatus());
        assertEquals(5, snapshot.getRating());
    }

    @Test
    void empty_constructor_sets_exportedAt() {
        BookSnapshot snapshot = new BookSnapshot();

        assertNotNull(snapshot.getExportedAt());
    }

    // on teste 2 pas égaux
    @Test
    void equals_returns_false_for_different_title() {
        BookSnapshot a = new BookSnapshot("Dune", "Frank Herbert", "FINISHED", 5);
        BookSnapshot b = new BookSnapshot("Foundation", "Frank Herbert", "FINISHED", 5);

        assertNotEquals(a, b);
    }

    // Teste hashCode() — cohérent avec equals()
    @Test
    void hashCode_is_consistent_with_equals() {
        BookSnapshot a = new BookSnapshot("Dune", "Frank Herbert", "FINISHED", 5);
        BookSnapshot b = new BookSnapshot("Dune", "Frank Herbert", "WANT_TO_READ", 3);

        assertEquals(a.hashCode(), b.hashCode());
    }

    // Teste toString() — contient bien les champs principaux
    @Test
    void toString_contains_title_and_author() {
        BookSnapshot snapshot = new BookSnapshot("Dune", "Frank Herbert", "FINISHED", 5);

        assertTrue(snapshot.toString().contains("Dune"));
        assertTrue(snapshot.toString().contains("Frank Herbert"));
    }
}