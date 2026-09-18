package com.campusflow;

import com.campusflow.exception.*;
import com.campusflow.io.FileReportWriter;
import com.campusflow.jdbc.JdbcReportService;
import com.campusflow.model.*;
import com.campusflow.reflection.ReflectionService;
import com.campusflow.repository.*;
import com.campusflow.service.*;
import com.campusflow.thread.BookingMonitor;
import com.campusflow.ui.Menu;
import com.campusflow.util.JPAUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final Scanner scanner = new Scanner(System.in);

    private final UserRepository userRepository = new UserRepository();
    private final ResourceRepository resourceRepository = new ResourceRepository();
    private final BookingRepository bookingRepository = new BookingRepository();
    private final EventRepository eventRepository = new EventRepository();

    private final NotificationService notificationService =
            new ConsoleNotificationService();

    private final AuthService authService =
            new AuthService(userRepository);

    private final BookingService bookingService =
            new BookingService(bookingRepository, resourceRepository, notificationService);

    private final EventService eventService =
            new EventService(eventRepository, notificationService);

    private final ReportService reportService =
            new ReportService(bookingRepository, resourceRepository);

    private final BookingMonitor monitor = new BookingMonitor();

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        seedData();
        monitor.start();

        try {
            mainMenu();
        } finally {
            monitor.shutdown();
            JPAUtil.close();
            scanner.close();
        }
    }

    private void mainMenu() {
        boolean running = true;

        while (running) {
            Menu.header("CAMPUSFLOW");
            System.out.println("1. Login");
            System.out.println("2. Register Student");
            System.out.println("3. Browse Resources");
            System.out.println("4. View Events");
            System.out.println("5. System Diagnostics");
            System.out.println("6. Exit");

            int choice = readInt("Choice: ");

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> listResources();
                case 4 -> listEvents();
                case 5 -> diagnostics();
                case 6 -> running = false;
                default -> System.out.println("Please select a valid option.");
            }
        }
    }

    private void login() {
        String email = readLine("Email: ");
        String password = readLine("Password: ");

        User user = authService.authenticate(email, password);

        if (user == null) {
            System.out.println("Login failed. Check your credentials.");
            return;
        }

        System.out.println("\nWelcome, " + user.getName() +
                " (" + user.getRoleDescription() + ")");
        dashboard(user);
    }

    private void register() {
        try {
            String name = readLine("Name: ");
            String email = readLine("Email: ");
            String password = readLine("Password: ");
            String programme = readLine("Programme: ");

            authService.registerStudent(name, email, password, programme);
            System.out.println("Registration completed.");
        } catch (ValidationException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    private void dashboard(User user) {
        boolean loggedIn = true;

        while (loggedIn) {
            Menu.header(user.getRoleDescription().toUpperCase() + " DASHBOARD");
            System.out.println("1. View Resources");
            System.out.println("2. Book Resource");
            System.out.println("3. My Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Events");
            System.out.println("6. Register for Event");
            System.out.println("7. Export My Activity");
            System.out.println("8. View Usage Report");
            if (user.getRole() == UserRole.ADMIN) {
                System.out.println("9. Add Resource");
                System.out.println("10. Add Event");
                System.out.println("11. JDBC Booking Count");
            }
            System.out.println("0. Logout");

            int choice = readInt("Choice: ");

            try {
                switch (choice) {
                    case 1 -> listResources();
                    case 2 -> bookResource(user);
                    case 3 -> showBookings(user);
                    case 4 -> cancelBooking();
                    case 5 -> listEvents();
                    case 6 -> registerEvent(user);
                    case 7 -> exportActivity(user);
                    case 8 -> System.out.println(reportService.buildUsageReport());
                    case 9 -> {
                        if (user.getRole() == UserRole.ADMIN) addResource();
                        else System.out.println("Access denied.");
                    }
                    case 10 -> {
                        if (user.getRole() == UserRole.ADMIN) addEvent();
                        else System.out.println("Access denied.");
                    }
                    case 11 -> {
                        if (user.getRole() == UserRole.ADMIN) {
                            System.out.println("JDBC booking count: " +
                                    new JdbcReportService().countBookings());
                        } else System.out.println("Access denied.");
                    }
                    case 0 -> loggedIn = false;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (CampusException e) {
                System.out.println("Operation failed: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database report failed: " + e.getMessage());
            }
        }
    }

    private void listResources() {
        Menu.header("AVAILABLE RESOURCES");
        List<Resource> resources = resourceRepository.findAll();

        if (resources.isEmpty()) {
            System.out.println("No resources found.");
            return;
        }

        resources.forEach(System.out::println);
    }

    private void bookResource(User user) throws CampusException {
        listResources();
        long id = readLong("Resource ID: ");

        Resource resource = resourceRepository.findById(id);

        if (resource == null) {
            throw new ValidationException("Resource not found.");
        }

        String dateText = readLine("Date (YYYY-MM-DD): ");
        String startText = readLine("Start time (HH:MM): ");
        String endText = readLine("End time (HH:MM): ");

        Booking booking = bookingService.createBooking(
                user,
                resource,
                LocalDate.parse(dateText),
                LocalTime.parse(startText),
                LocalTime.parse(endText)
        );

        System.out.println("Booking created: " + booking);
    }

    private void showBookings(User user) {
        Menu.header("MY BOOKINGS");
        List<Booking> bookings = bookingRepository.findForUser(user.getId());

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        bookings.forEach(System.out::println);
    }

    private void cancelBooking() throws ValidationException {
        long id = readLong("Booking ID: ");
        bookingService.cancelBooking(id);
        System.out.println("Booking cancelled.");
    }

    private void listEvents() {
        Menu.header("CAMPUS EVENTS");
        List<CampusEvent> events = eventService.listEvents();

        if (events.isEmpty()) {
            System.out.println("No events scheduled.");
            return;
        }

        events.forEach(System.out::println);
    }

    private void registerEvent(User user) throws ValidationException {
        listEvents();
        long id = readLong("Event ID: ");
        eventService.register(user, id);
        System.out.println("Event registration successful.");
    }

    private void exportActivity(User user) {
        try {
            String report = reportService.buildUserReport(user.getId());
            FileReportWriter writer = new FileReportWriter();
            writer.writeText("reports/user-" + user.getId() + "-activity.txt", report);
            System.out.println("Report exported to reports/.");
        } catch (IOException e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    private void addResource() {
        String name = readLine("Resource name: ");
        String location = readLine("Location: ");
        int capacity = readInt("Capacity: ");

        System.out.println("1. Classroom");
        System.out.println("2. Laboratory");
        System.out.println("3. Equipment");
        int type = readInt("Type: ");

        Resource resource;

        switch (type) {
            case 1 -> resource = new Classroom(name, location, capacity,
                    readLine("Smart board? (true/false): ").equalsIgnoreCase("true"));
            case 2 -> resource = new Laboratory(name, location, capacity,
                    readInt("Computer count: "));
            case 3 -> resource = new Equipment(name, location, capacity,
                    readLine("Condition: "));
            default -> {
                System.out.println("Invalid resource type.");
                return;
            }
        }

        resourceRepository.save(resource);
        System.out.println("Resource added.");
    }

    private void addEvent() throws ValidationException {
        String title = readLine("Title: ");
        String description = readLine("Description: ");
        LocalDate date = LocalDate.parse(readLine("Date (YYYY-MM-DD): "));
        String venue = readLine("Venue: ");
        int capacity = readInt("Capacity: ");

        EventCategory[] categories = EventCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }

        int selected = readInt("Category: ");
        if (selected < 1 || selected > categories.length) {
            throw new ValidationException("Invalid event category.");
        }

        eventService.createEvent(title, description, date, venue,
                capacity, categories[selected - 1]);

        System.out.println("Event created.");
    }

    private void diagnostics() {
        Menu.header("SYSTEM DIAGNOSTICS");

        ReflectionService reflection = new ReflectionService();
        System.out.println("Background monitor checks: " + monitor.getHealthChecks());
        System.out.println(reflection.inspect(BookingService.class));

        NotificationService oneTimeNotifier = new NotificationService() {
            @Override
            public void send(Long userId, String message) {
                System.out.println("[Anonymous Notification] " + message);
            }
        };

        oneTimeNotifier.send(0L, "Anonymous class demonstration active.");
    }

    private void seedData() {
        try {
            if (userRepository.findByEmail("admin@campusflow.local") == null) {
                userRepository.save(new Admin(
                        "System Admin",
                        "admin@campusflow.local",
                        "admin123"));
            }

            if (userRepository.findByEmail("student@campusflow.local") == null) {
                userRepository.save(new Student(
                        "Demo Student",
                        "student@campusflow.local",
                        "student123",
                        "CSE AI & ML"));
            }

            if (resourceRepository.findAll().isEmpty()) {
                resourceRepository.save(
                        new Classroom("AR-202", "Academic Block", 60, true));
                resourceRepository.save(
                        new Laboratory("AI Lab", "AB-218", 40, true));
                resourceRepository.save(
                        new Equipment("Projector-01", "Central Store", 1, "Good"));
            }

            if (eventRepository.findAll().isEmpty()) {
                eventRepository.save(new CampusEvent(
                        "Java Project Showcase",
                        "Student project demonstration",
                        LocalDate.now().plusDays(7),
                        "Seminar Hall",
                        100,
                        EventCategory.TECHNICAL));
            }
        } catch (Exception e) {
            System.out.println("Initial data setup warning: " + e.getMessage());
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid ID.");
            }
        }
    }
}
