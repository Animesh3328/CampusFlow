# CampusFlow

## Smart Campus Resource and Event Management System

CampusFlow is a Java console application for managing shared campus resources and events. It provides role-based access, resource discovery, conflict-aware booking, event registration, notifications, reporting, file export and runtime diagnostics.

The project is intentionally structured around Programming in Java concepts such as classes and objects, constructors, encapsulation, inheritance, abstraction, interfaces, overloading and overriding, enums, nested and anonymous classes, singleton-style utility design, exceptions, annotations, reflection, multithreading, synchronization, collections, arrays, I/O, JDBC and JPA.

## Main Features

1. User registration and role-based login
2. Resource management for classrooms, laboratories and equipment
3. Conflict-aware resource booking and cancellation
4. Campus event creation and registration
5. Console notifications
6. Activity and usage reports
7. Text-file report export
8. Runtime diagnostics using reflection and annotations
9. JDBC booking-count reporting for administrators
10. Background monitoring thread without noisy console output

## Technology Stack

- Java 17 language target
- Maven
- Hibernate ORM / Jakarta Persistence (JPA)
- H2 Database
- JDBC
- JUnit 5
- Git / GitHub

## Project Structure

```text
src/main/java/com/campusflow/
    annotation/
    exception/
    io/
    jdbc/
    model/
    reflection/
    repository/
    service/
    thread/
    ui/
    util/

src/test/java/com/campusflow/

docs/
    architecture.md
    diagrams.md
    report-outline.md

screenshots/
```

## Requirements

- JDK 17 or later
- Maven 3.9+
- Internet access on first Maven build so dependencies can be downloaded

The Maven compiler is configured with `release 17`, so the project targets Java 17 even when a newer JDK is installed.

## Build and Test

From the project root:

```bash
mvn clean test
mvn clean package
```

The package command produces:

```text
target/campusflow-1.0.0.jar
```

## Run

The application can be run from VS Code using `Main.java`. A dependency-resolved command-line run can also be created with Maven's dependency classpath tooling.

## Demo Accounts

The application creates these accounts on first run:

- Admin: `admin@campusflow.local` / `admin123`
- Student: `student@campusflow.local` / `student123`

## Testing

The current automated test suite contains two JUnit tests covering booking conflict detection and polymorphic resource-cost behaviour.

Run:

```bash
mvn test
```

## Java Concepts Demonstrated

| Concept | Project location |
|---|---|
| Classes / objects | model package |
| Constructors | model classes |
| Encapsulation | private fields and accessors |
| Inheritance | Student, Faculty, Admin from User |
| Overloading | BookingService and InputValidator |
| Overriding | User and Resource subclasses |
| `super` | subclass constructors |
| Abstract classes | User, Resource |
| Interfaces | NotificationService |
| Runtime polymorphism | User and Resource references |
| Enums | UserRole, ResourceType, BookingStatus, EventCategory |
| Nested class | Booking.Builder |
| Anonymous class | Main notification demonstration |
| Singleton-style utility | JPAUtil |
| Custom exceptions | exception package |
| Annotations | Auditable |
| Reflection | ReflectionService |
| Threads | BookingMonitor |
| Synchronization | BookingService |
| Collections | ArrayList, Vector, Stack, Map |
| Static method overloading | InputValidator |
| Arrays | ReportService |
| Recursion | ResourceHierarchy |
| I/O streams | FileReportWriter |
| JDBC | JdbcReportService |
| JPA / ORM | entity and repository layer |

## Documentation

- `statement.md` — project statement
- `docs/architecture.md` — layered architecture
- `docs/diagrams.md` — design diagrams
- `docs/report-outline.md` — report structure
- `screenshots/` — application validation captures

## Academic Note

The implementation is project-specific and should be understood by the submitting student. The source, design and report should be presented consistently with the student's own explanation during evaluation.
