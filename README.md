# Online Movie Ticket Booking System

A college-level web application for booking movie tickets online. Built with Java Servlets, JSP, HTML/CSS, vanilla JavaScript, and XML-based data storage (no database).

## Technologies Used

- **Java 17** — backend logic and data models
- **Jakarta Servlets 6.0** — HTTP request handling
- **Jakarta JSP 3.1 + JSTL** — server-side views
- **HTML5 / CSS3** — frontend markup and styling
- **Vanilla JavaScript** — seat selection interactivity
- **XML (DOM parsing)** — data storage (users, movies, shows, bookings)
- **Apache Tomcat 10.1+** — servlet container
- **Maven** — build and dependency management

No Spring Boot, Hibernate, React, Bootstrap, external APIs, or databases are used.

## Features

### Guest / User
- Register a new account and log in / log out
- Browse currently available movies with search by title, genre, or language
- View movie details: poster, title, genre, duration, language, rating, description
- View available showtimes (theatre, screen, date, time, price, seats left)
- Select seats from a visual seat layout
- See booking summary and total amount before confirming
- Confirm booking and view a ticket stub
- View "My Bookings" with booking history
- Cancel a booking (only while status is CONFIRMED; seats are released)

### Admin
- Separate admin login
- Add, edit, and remove movies from the catalogue
- Add new showtimes (movie, theatre, screen, date, time, price, seats)
- View all bookings in the system

## Folder Structure

```
movie-ticket-booking-system/
├── pom.xml                                 # Maven build file
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/movieticket/
│   │   │   ├── model/
│   │   │   │   ├── Movie.java
│   │   │   │   ├── Show.java
│   │   │   │   ├── User.java
│   │   │   │   └── Booking.java
│   │   │   ├── dao/
│   │   │   │   ├── MovieDao.java
│   │   │   │   ├── ShowDao.java
│   │   │   │   ├── UserDao.java
│   │   │   │   └── BookingDao.java
│   │   │   ├── servlet/
│   │   │   │   ├── HomeServlet.java
│   │   │   │   ├── AuthServlet.java        (login)
│     │   │   ├── RegisterServlet.java
│   │   │   │   ├── LogoutServlet.java
│   │   │   │   ├── MovieServlet.java       (listing + search)
│   │   │   │   ├── MovieDetailServlet.java
│   │   │   │   ├── SeatServlet.java
│   │   │   │   ├── BookingServlet.java
│   │   │   │   ├── CancelServlet.java
│   │   │   │   ├── MyBookingsServlet.java
│   │   │   │   ├── AdminDashboardServlet.java
│   │   │   │   ├── AdminMovieServlet.java
│   │   │   │   ├── AdminMovieDeleteServlet.java
│   │   │   │   └── AdminShowServlet.java
│   │   │   ├── filter/
│   │   │   │   └── AuthFilter.java
│   │   │   └── util/
│   │   │       ├── XmlUtil.java
│   │   │       ├── SessionUtil.java
│   │   │       └── IdGenerator.java
│   │   ├── resources/
│   │   │   └── data/
│   │   │       ├── movies.xml              (6 sample movies)
│   │   │       ├── shows.xml               (multiple theatres & showtimes)
│   │   │       ├── users.xml               (test user + admin)
│   │   │       └── bookings.xml            (empty, populated at runtime)
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml                 (servlet & filter mappings)
│   │       │   ├── data/                   (runtime XML data copy)
│   │       │   └── views/
│   │       │       ├── home.jsp
│   │       │       ├── login.jsp
│   │       │       ├── register.jsp
│   │       │       ├── movies.jsp
│   │       │       ├── movie-detail.jsp
│   │       │       ├── seat-select.jsp
│   │       │       ├── confirmation.jsp
│   │       │       ├── my-bookings.jsp
│   │       │       ├── error.jsp
│   │       │       └── admin/
│   │       │           ├── dashboard.jsp
│   │       │           ├── manage-movies.jsp
│   │       │           └── manage-shows.jsp
│   │       ├── css/
│   │       │   └── style.css
│   │       └── js/
│   │           └── app.js
└── target/                                 (build output)
```

## Setup Instructions

### Prerequisites
- Java JDK 17 or later
- Apache Maven (optional; `javac` works too)
- Apache Tomcat 10.1 or later (Jakarta EE 10 / Servlet 6.0)

### Option A: Build with Maven
```bash
cd movie-ticket-booking-system
mvn clean package
```
This produces `target/movie-ticket.war`.

Deploy the WAR:
1. Copy `target/movie-ticket.war` to `%TOMCAT_HOME%\webapps\`
2. Start Tomcat
3. Open `http://localhost:8080/movie-ticket/`

The XML data files are included under `WEB-INF/data/` inside the WAR. For a production deployment, consider moving the data folder outside the WAR so data persists across redeploys.

### Option B: Compile without Maven
```bash
cd movie-ticket-booking-system
javac -encoding UTF-8 -cp <servlet-api.jar> -d out $(find src/main/java -name "*.java")
```
Deploy the compiled classes and `src/main/webapp/` contents to a Tomcat webapp folder.

## Default Test Credentials

| Role | Username | Password |
|------|----------|----------|
| User | `guest` | `password123` |
| Admin | `admin` | `admin123` |

You can also register a new account via the Register page.

## Sample Data Included

- **6 movies**: The Dark Knight, Inception, Interstellar, Dangal, 3 Idiots, Spirited Away
- **15 showtimes** across PVR Cinemas, INOX, and Cinepolis theatres
- **1 test user** and **1 admin account** pre-seeded in `users.xml`
- Empty `bookings.xml` — populated as users make bookings

## Key Design Decisions

- **XML storage**: All data is stored in plain XML files under `WEB-INF/data/` and parsed with the built-in DOM API (`javax.xml.parsers`). No external database is required.
- **Session-based auth**: Logged-in user and role are stored in the HTTP session. `AuthFilter` protects user and admin routes.
- **Seat concurrency**: `XmlUtil.save()` is synchronized to prevent file corruption from concurrent booking requests.
- **Double-booking prevention**: Before saving a booking, the servlet checks each selected seat against the show's `bookedSeats` list.
- **Cancellation**: Cancelling a CONFIRMED booking releases its seats back to the show and marks the booking `CANCELLED`.
- **Simple password storage**: Passwords are stored in plain text for this academic demo. Production applications should hash passwords (e.g., BCrypt).

## Limitations & Future Improvements

- Passwords are stored in plain text (not suitable for production)
- No payment gateway integration (booking confirmation is simulated)
- No email notifications for bookings
- No image upload for movie posters (uses local placeholder paths)
- No pagination on movie listing or booking history
- Data files are inside the WAR; redeploying may reset runtime data unless stored externally

## Screenshots

Place screenshots here:
- Home page
- Movie listing
- Seat selection
- Booking confirmation
- Admin dashboard
