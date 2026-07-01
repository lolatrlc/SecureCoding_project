package hr.algebra.booknook.config;

import hr.algebra.booknook.entity.Book;
import hr.algebra.booknook.entity.User;
import hr.algebra.booknook.enums.BookFormat;
import hr.algebra.booknook.enums.BookGenre;
import hr.algebra.booknook.enums.ReadingStatus;
import hr.algebra.booknook.enums.Role;
import hr.algebra.booknook.repository.BookRepository;
import hr.algebra.booknook.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.init.admin-password}")
    private String adminPassword;

    @Value("${app.init.user-password}")
    private String userPassword;

    public DataInitializer(
        UserRepository userRepository,
        BookRepository bookRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@booknook.hr");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin = userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@booknook.hr");
        user.setPassword(passwordEncoder.encode(userPassword));
        user.setRole(Role.USER);
        userRepository.save(user);

        createBook("The Seven Husbands of Evelyn Hugo", "Taylor Jenkins Reid",
            null, null, "Atria Books", 2017, "978-1501161933",
            BookGenre.HISTORICAL_FICTION, BookFormat.PAPERBACK, ReadingStatus.FINISHED,
            400, 400, 5, true, true, true,
            LocalDate.of(2024, 11, 14), LocalDate.of(2024, 11, 21), LocalDate.of(2024, 10, 30),
            "Old Hollywood glamour, queer love story, slow burn",
            "Homophobia, alcoholism, abusive relationships, racism",
            "People always say it must have been so hard, choosing between loving him and loving her. But that's not really how it worked.",
            "Devastating in the best way. Cried for an hour after finishing. Evelyn deserves the world. The twist at the end completely broke me. Top 5 ever.",
            "BookTok was right about this one. Read in 2 days flat.", admin);

        createBook("A Court of Thorns and Roses", "Sarah J. Maas",
            "A Court of Thorns and Roses", 1, "Bloomsbury", 2015, "978-1619634442",
            BookGenre.FANTASY, BookFormat.PAPERBACK, ReadingStatus.FINISHED,
            419, 419, 4, true, false, true,
            LocalDate.of(2024, 8, 5), LocalDate.of(2024, 8, 18), LocalDate.of(2024, 7, 25),
            "Dark fae fantasy, enemies-to-lovers, slow burn",
            "Sexual content, violence, captivity",
            "Be glad of your human heart, Feyre. Pity those who don't feel anything at all.",
            "ACOTAR was actually the warm-up. Wait until you get to ACOMAF. The whole series is a journey but this one starts it all.",
            "Spicy. Started the BookTok pipeline for fantasy romance.", admin);

        createBook("Fourth Wing", "Rebecca Yarros",
            "The Empyrean", 1, "Entangled Publishing", 2023, "978-1649374042",
            BookGenre.FANTASY, BookFormat.HARDCOVER, ReadingStatus.FINISHED,
            517, 517, 5, true, true, true,
            LocalDate.of(2024, 12, 27), LocalDate.of(2025, 1, 5), LocalDate.of(2024, 12, 20),
            "Dragon riders, war college, deadly trials, enemies-to-lovers",
            "Violence, death, ableism, sexual content, war",
            "A leader who does not bleed for her people is no leader at all.",
            "Could not put this down. Finished at 4am on a Sunday and immediately ordered Iron Flame. Tairn and Andarna are my favorite dragons in fiction.",
            "TikTok made me read this and TikTok was 100% right.", admin);

        createBook("Tomorrow, and Tomorrow, and Tomorrow", "Gabrielle Zevin",
            null, null, "Knopf", 2022, "978-0593321201",
            BookGenre.LITERARY_FICTION, BookFormat.HARDCOVER, ReadingStatus.FINISHED,
            401, 401, 5, true, false, true,
            LocalDate.of(2024, 9, 10), LocalDate.of(2024, 9, 28), LocalDate.of(2024, 9, 5),
            "Friendship, video game design, decades-spanning, melancholic",
            "Death of a child, mass shooting, ableism, mental health",
            "What is a game? It's tomorrow, and tomorrow, and tomorrow. The possibility of infinite rebirth, infinite redemption.",
            "Quietly devastating. About friendship more than romance. The Macbeth references work on every level. Sam and Sadie will haunt me forever.",
            "Best 'literary fiction with video game references' book ever written.", admin);

        createBook("Iron Flame", "Rebecca Yarros",
            "The Empyrean", 2, "Entangled Publishing", 2023, "978-1649374172",
            BookGenre.FANTASY, BookFormat.HARDCOVER, ReadingStatus.CURRENTLY_READING,
            640, 287, null, true, true, false,
            LocalDate.of(2026, 4, 12), null, LocalDate.of(2026, 4, 10),
            "Dark academia, war strategy, betrayal, second-book syndrome avoided",
            "Violence, torture, war, sexual content",
            null,
            null,
            "On chapter 23. Already 100x more brutal than Fourth Wing. Xaden's POV chapters are EVERYTHING.", admin);

        createBook("Babel", "R.F. Kuang",
            null, null, "Harper Voyager", 2022, "978-0063021426",
            BookGenre.FANTASY, BookFormat.PAPERBACK, ReadingStatus.WANT_TO_READ,
            544, null, null, false, false, false,
            null, null, LocalDate.of(2026, 3, 15),
            "Dark academia, magic linguistics, colonialism, Oxford 1830s",
            "Racism, colonialism, violence, suicide",
            null, null,
            "On hold at the library. 4 weeks wait. Heard it requires you to be in the right headspace.", admin);

        createBook("It Ends with Us", "Colleen Hoover",
            null, null, "Atria Books", 2016, "978-1501110368",
            BookGenre.ROMANCE, BookFormat.PAPERBACK, ReadingStatus.DNF,
            384, 156, 2, true, false, false,
            LocalDate.of(2024, 6, 1), null, LocalDate.of(2024, 5, 28),
            "Contemporary romance, abusive relationship, hometown drama",
            "Domestic violence, abuse, suicide, sexual content",
            null,
            "Couldn't finish. The hype didn't match the writing for me. The romanticization of certain dynamics felt off. DNF at page 156.",
            "BookTok algorithm shoved this at me. Not for me.", admin);

        createBook("The Way of Kings", "Brandon Sanderson",
            "The Stormlight Archive", 1, "Tor Books", 2010, "978-0765326355",
            BookGenre.FANTASY, BookFormat.HARDCOVER, ReadingStatus.PAUSED,
            1007, 423, null, true, false, false,
            LocalDate.of(2024, 2, 14), null, LocalDate.of(2024, 2, 1),
            "Epic high fantasy, hero journey, slow build, world-shattering",
            "War violence, slavery, mental illness",
            "Journey before destination.",
            null,
            "Paused for over a year. Started Iron Flame instead. Need to commit to picking this back up. Stormlight is a LIFESTYLE.", admin);

        createBook("Mexican Gothic", "Silvia Moreno-Garcia",
            null, null, "Del Rey", 2020, "978-0525620785",
            BookGenre.HORROR, BookFormat.EBOOK, ReadingStatus.FINISHED,
            301, 301, 4, true, false, false,
            LocalDate.of(2024, 10, 15), LocalDate.of(2024, 10, 22), LocalDate.of(2024, 10, 13),
            "Gothic horror, 1950s Mexico, body horror, atmospheric dread",
            "Body horror, sexual assault, eugenics, gore",
            "There is something underneath, something vile, something that creeps in the dark places.",
            "Spent the whole book wanting to scream RUN at Noemi. The house is a character. Read with the lights on.",
            "Perfect October read. Atmospheric horror at its best.", admin);

        createBook("Project Hail Mary", "Andy Weir",
            null, null, "Ballantine Books", 2021, "978-0593135204",
            BookGenre.SCIENCE_FICTION, BookFormat.AUDIOBOOK, ReadingStatus.FINISHED,
            476, 476, 5, true, true, true,
            LocalDate.of(2025, 1, 10), LocalDate.of(2025, 1, 25), LocalDate.of(2025, 1, 8),
            "Hard sci-fi, friendship, problem-solving, optimistic",
            "None significant",
            "Question. There is no Earth food that is good. Therefore, there is no good food. Mathematically proven.",
            "Rocky! Just Rocky! That's the entire review. The audiobook narrator absolutely nails it. Funniest sci-fi I have ever read.",
            "Audiobook is the way to experience this. Ray Porter's narration of Rocky alone is worth it.", admin);
    }

    private void createBook(
        String title, String author,
        String seriesName, Integer seriesNumber,
        String publisher, Integer pubYear, String isbn,
        BookGenre genre, BookFormat format, ReadingStatus status,
        Integer pageCount, Integer currentPage, Integer rating,
        boolean owned, boolean challenge, boolean reReadable,
        LocalDate started, LocalDate finished, LocalDate added,
        String mood, String triggers, String quote,
        String review, String notes, User addedBy
    ) {
        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setSeriesName(seriesName);
        b.setSeriesNumber(seriesNumber);
        b.setPublisher(publisher);
        b.setPublicationYear(pubYear);
        b.setIsbn(isbn);
        b.setGenre(genre);
        b.setFormat(format);
        b.setStatus(status);
        b.setPageCount(pageCount);
        b.setCurrentPage(currentPage);
        b.setRating(rating);
        b.setOwnedCopy(owned);
        b.setPartOfChallenge(challenge);
        b.setReReadable(reReadable);
        b.setStartedReading(started);
        b.setFinishedReading(finished);
        b.setAddedDate(added);
        b.setMoodAtmosphere(mood);
        b.setTriggerWarnings(triggers);
        b.setFavoriteQuote(quote);
        b.setReview(review);
        b.setPersonalNotes(notes);
        b.setAddedBy(addedBy);
        bookRepository.save(b);
    }
}
