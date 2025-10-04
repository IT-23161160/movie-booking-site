# Movie Booking Site

A comprehensive web-based movie booking system built with Spring Boot that allows users to browse movies, select showtimes, book seats, and process payments.

## 🚀 Features

- **Movie Management**: Browse current and upcoming movies
- **Theatre & Screen Management**: Multiple theatres with different screens
- **Showtime Selection**: Dynamic showtime scheduling
- **Seat Booking**: Interactive seat selection with real-time availability
- **Payment Processing**: Secure payment handling with PDF ticket generation
- **Admin Dashboard**: Complete admin panel for managing movies, bookings, and payments
- **User Authentication**: Role-based access control (Admin/User)

## 🛠 Tools and Frameworks Used

### Core Framework
- **Spring Boot 3.4.5** - Main application framework
- **Spring MVC** - Web layer architecture
- **Spring Security** - Authentication and authorization
- **Thymeleaf** - Server-side templating engine

### Development Tools
- **Java 24** - Programming language
- **Maven** - Build automation and dependency management
- **Project Lombok** - Reduces boilerplate code
- **Spring Boot DevTools** - Development-time tools for hot reloading

### Additional Libraries
- **iText PDF 5.5.13.3** - PDF generation for tickets
- **Jackson DataType JSR310** - Java 8 time API JSON serialization
- **SLF4J & Logback** - Logging framework
- **Spring REST Docs** - API documentation
- **Thymeleaf Spring Security** - Security integration with templates

### Testing Framework
- **Spring Boot Test Starter** - Testing utilities
- **Spring Security Test** - Security testing support
- **Spring REST Docs MockMVC** - API documentation testing

## 📊 Data Structures Used

### Core Data Structures
- **ArrayList** - Primary collection for storing movies, bookings, screens, theatres
- **HashMap/Map** - Caching and mapping entities (movieMap, theatreMap, screenMap)
- **HashSet** - Fast seat availability checking
- **LinkedList (Custom)** - Custom queue implementation for booking processing
- **Optional** - Safe handling of nullable values

### Custom Data Structures
- **BookingQueue** - Custom queue implementation with thread-safe operations
- **BookingNode** - Linked list node for booking queue
- **File-based Storage** - Custom file I/O operations for data persistence

### Collections Usage Examples
```java
// ArrayList for entity storage
List<Movie> movies = new ArrayList<>();
List<Booking> bookings = new ArrayList<>();

// HashMap for entity mapping
Map<String, Movie> movieMap = new HashMap<>();
Map<String, Theatre> theatreMap = new HashMap<>();

// HashSet for fast lookups
Set<String> availableSeats = new HashSet<>(availableList);

// Custom queue for booking processing
private BookingQueue bookingQueue = new BookingQueue();
```

## 🎯 OOP Concepts Used

### 1. **Encapsulation**
- All model classes have private fields with public getters/setters
- Repository classes encapsulate data access logic
- Service classes encapsulate business logic

```java
public class Movie {
    private String movieId;
    private String title;
    private String genre;
    private LocalDate releaseDate;
    
    // Getters and setters...
}
```

### 2. **Abstraction**
- Service layer abstracts business logic from controllers
- Repository pattern abstracts data access
- Utility classes provide common functionality

### 3. **Inheritance**
- Extends Spring Boot's base classes
- Model classes follow inheritance patterns where applicable

### 4. **Polymorphism**
- Interface implementations (Service interfaces)
- Method overloading in service classes
- Spring's dependency injection enabling polymorphic behavior

### 5. **Composition**
- Services composed of multiple repositories
- Controllers composed of multiple services
- Complex objects built from simpler components

```java
@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SeatFileUtil seatUtil;
    // Service composed of multiple dependencies
}
```

### 6. **Dependency Injection**
- Constructor and field injection using `@Autowired`
- Inversion of Control through Spring framework

## 🏗 Project Structure

```
src/
├── main/
│   ├── java/com/moviebooking/
│   │   ├── controller/          # MVC Controllers
│   │   │   ├── AdminController.java
│   │   │   ├── BookingController.java
│   │   │   ├── MovieController.java
│   │   │   ├── PaymentController.java
│   │   │   └── ShowtimeController.java
│   │   ├── model/              # Entity Models
│   │   │   ├── Booking.java
│   │   │   ├── Movie.java
│   │   │   ├── Payment.java
│   │   │   ├── Screen.java
│   │   │   ├── Showtime.java
│   │   │   └── Theatre.java
│   │   ├── service/            # Business Logic Layer
│   │   │   ├── BookingService.java
│   │   │   ├── MovieService.java
│   │   │   ├── PaymentService.java
│   │   │   └── ShowtimeService.java
│   │   ├── repository/         # Data Access Layer
│   │   │   ├── BookingRepository.java
│   │   │   ├── MovieRepository.java
│   │   │   ├── PaymentRepository.java
│   │   │   └── ScreenRepository.java
│   │   ├── util/              # Utility Classes
│   │   │   └── SeatFileUtil.java
│   │   └── MovieBookingApplication.java
│   └── resources/
│       ├── templates/          # Thymeleaf Templates
│       └── static/            # CSS, JS, Images
├── data/                      # File-based data storage
└── uploads/                   # Uploaded movie images
```

## ⚙️ Setup and Run Guide

### Prerequisites
- **Java 24** or higher
- **Maven 3.6+**
- **Git**

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/IT-23161160/movie-booking-site.git
   cd movie-booking-site
   ```

2. **Build the Project**
   ```bash
   # Using Maven wrapper (recommended)
   ./mvnw clean compile
   
   # Or using system Maven
   mvn clean compile
   ```

3. **Run the Application**
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using system Maven
   mvn spring-boot:run
   
   # Or run the JAR file
   ./mvnw clean package
   java -jar target/MovieBooking-0.0.1-SNAPSHOT.jar
   ```

4. **Access the Application**
   - Open your web browser
   - Navigate to: `http://localhost:8080`
   - Admin panel: `http://localhost:8080/admin`

### Environment Setup

1. **Create Required Directories**
   ```bash
   mkdir -p data uploads/images
   ```

2. **File Permissions** (Linux/Mac)
   ```bash
   chmod +x mvnw
   ```

### Configuration

The application uses file-based storage by default. Data files will be created automatically in the `data/` directory:
- `movies.txt` - Movie information
- `bookings.txt` - Booking records
- `payments.txt` - Payment records
- `screens.txt` - Screen information
- `theatres.txt` - Theatre information
- `showtimes.txt` - Showtime schedules
- `seats_[showtimeId].txt` - Seat availability per showtime

### Default Admin Access
- Configure admin users through Spring Security configuration
- Default security settings are in the security configuration classes

## 🚀 Running in Development Mode

```bash
# Run with dev profile for hot reloading
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🧪 Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=BookingServiceTest
```

## 📋 Key Features Implementation

- **Thread-Safe Booking**: Custom queue implementation ensures thread-safe booking operations
- **Real-time Seat Management**: File-based seat tracking with status updates
- **PDF Ticket Generation**: Automated ticket generation using iText
- **Role-based Security**: Admin and user role separation
- **Responsive Design**: Thymeleaf templates with modern UI
- **File Upload**: Movie poster upload functionality
- **Data Persistence**: File-based storage system

## 🔧 Troubleshooting

### Common Issues
1. **Port 8080 already in use**: Change server port in `application.properties`
2. **File permission errors**: Ensure write permissions for `data/` and `uploads/` directories
3. **Java version mismatch**: Ensure Java 24 is installed and configured

### Debug Mode
```bash
./mvnw spring-boot:run -Ddebug
```

## 📄 License

This project is for educational purposes. Please refer to the repository for license information.

## 👥 Contributors

- IT-23161160 - Project Developer

---

For more information, visit the [GitHub repository](https://github.com/IT-23161160/movie-booking-site).
