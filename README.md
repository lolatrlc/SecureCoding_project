# BookNook

A personal reading library and book journal built as a Spring Boot 4 project with an MVC interface and a REST API.

## Technologies

| Component | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Spring MVC / Spring Security | 7.x |
| Thymeleaf | 3.x |
| Spring Data JPA / Hibernate | 7.x |
| H2 (in-memory) | runtime |
| Auth0 Java JWT | 4.4.0 |
| springdoc-openapi | 3.0.0 |

## Running the Project in IntelliJ IDEA

1. **Open the project:** `File → Open` → select the `booknook` folder
2. **SDK:** `File → Project Structure → SDK` → set to **Java 25**
3. **Maven:** IntelliJ will automatically download dependencies; if not, run **Reload Maven Project**
4. **Run:** `BooknookApplication.java` → right-click → *Run*
5. **Access:** [http://localhost:8080](http://localhost:8080)

## Default User Accounts (in-memory H2)

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN — full management |
| `user` | `user123` | USER — read and search only |

## MVC Interface

| URL | Description | Access |
|---|---|---|
| `/` | Redirect to `/books` | — |
| `/auth/login` | Login form | Public |
| `/auth/register` | Registration form | Public |
| `/books` | Browse and search books | USER + ADMIN |
| `/books/{id}` | Book details with full reading journey | USER + ADMIN |
| `/books/new` | Add new book form | ADMIN |
| `/books/edit/{id}` | Edit book form | ADMIN |
| `/books/delete/{id}` | Delete book (POST) | ADMIN |

## REST API

Base URL: `/api`

### Authentication

| Method | URL | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Login — returns access + refresh token |
| `POST` | `/api/auth/register` | Register a new user account |
| `POST` | `/api/auth/refresh` | Obtain a new access token |
| `POST` | `/api/auth/logout` | Revoke the refresh token |

### Books (requires Bearer token)

| Method | URL | Description | Role |
|---|---|---|---|
| `GET` | `/api/books` | All books | USER + ADMIN |
| `GET` | `/api/books/{id}` | Single book | USER + ADMIN |
| `GET` | `/api/books/search` | Search (`query`, `genre`, `format`, `status`) | USER + ADMIN |
| `POST` | `/api/books` | Add new book | ADMIN |
| `PUT` | `/api/books/{id}` | Update book | ADMIN |
| `DELETE` | `/api/books/{id}` | Delete book | ADMIN |

### Example: Login and API Usage

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Fetch books using the access token
curl http://localhost:8080/api/books \
  -H "Authorization: Bearer <access_token>"
```

## Swagger UI

Available at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

1. Call `POST /api/auth/login` with `admin` / `admin123`
2. Copy the `accessToken` from the response
3. Click **Authorize** (top right) → paste the token
4. Use any of the secured endpoints

## H2 Console

URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:booknookdb` |
| Username | `sa` |
| Password | *(leave blank)* |

## Key Features

- **Reading progress bars** — for currently reading books, see exact percentage and page (e.g. "Page 287 of 640 — 44%") with a beautiful gold gradient hero card
- **Star ratings** — 5-star rating system with filled (★) and empty (☆) stars in dark academia gold
- **5 reading statuses** — WANT_TO_READ, CURRENTLY_READING, FINISHED, DNF (Did Not Finish), PAUSED — with status-coded color stripes
- **Beautiful quote display** — favorite quotes rendered with decorative quotation marks in serif italic, with gold accent border
- **Mood & atmosphere tagging** — capture vibes like "Cozy autumn vibes, dark academia" or "Old Hollywood glamour"
- **Trigger warnings** — separate field with warning-styled banner so readers can be informed
- **Series tracking** — track series name + book number, displayed inline
- **Reading timeline** — visual timeline cards for Started, Finished, Added to Library, Published year
- **Decorative ornaments** — typographic flourishes (~ • ~) between sections for that bookish aesthetic
- **15 book genres** including Fantasy, Romance, Literary Fiction, Dark Academia favorites
- **Triple-flag system** — Owned copy, Part of yearly challenge, Re-readable

## Project Structure

```
src/main/java/hr/algebra/booknook/
├── BooknookApplication.java
├── config/
│   ├── DataInitializer.java          # 10 sample books on startup
│   ├── OpenApiConfig.java            # Swagger / OpenAPI configuration
│   └── SecurityConfig.java           # Two filter chains (API + MVC)
├── controller/
│   ├── mvc/
│   │   ├── AuthMvcController.java
│   │   ├── BookMvcController.java
│   │   └── HomeController.java
│   └── rest/
│       ├── AuthRestController.java
│       └── BookRestController.java
├── dto/
│   ├── BookDto.java                  # Java record
│   └── Dto.java                      # Login/Register/Token records
├── entity/
│   ├── Book.java
│   ├── RefreshToken.java
│   └── User.java                     # Implements UserDetails
├── enums/
│   ├── BookFormat.java
│   ├── BookGenre.java
│   ├── ReadingStatus.java
│   └── Role.java
├── repository/
│   ├── BookRepository.java
│   ├── RefreshTokenRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtService.java
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    ├── BookService.java
    └── RefreshTokenService.java
```
