# CampusFlow Design Diagrams

## 1. Use Case Diagram

```text
                         +----------------------+
                         |      CampusFlow      |
                         +----------------------+

 Student  --------------------> Login
 Student  --------------------> Browse Resources
 Student  --------------------> Book Resource
 Student  --------------------> View Bookings
 Student  --------------------> Cancel Booking
 Student  --------------------> View Events
 Student  --------------------> Register for Event
 Student  --------------------> Export Activity
 Student  --------------------> View Usage Report

 Faculty  --------------------> Login
 Faculty  --------------------> Browse Resources
 Faculty  --------------------> Book Resource
 Faculty  --------------------> View Bookings
 Faculty  --------------------> Cancel Booking
 Faculty  --------------------> View Events
 Faculty  --------------------> Register for Event

 Admin    --------------------> Login
 Admin    --------------------> View Resources
 Admin    --------------------> Book Resource
 Admin    --------------------> View Bookings
 Admin    --------------------> Cancel Booking
 Admin    --------------------> Manage Resources
 Admin    --------------------> Manage Events
 Admin    --------------------> View Reports
 Admin    --------------------> JDBC Booking Count
 Admin    --------------------> System Diagnostics
```

## 2. System Workflow

```text
Start
  |
  v
Login / Register
  |
  v
Authenticate User
  |
  +------ Invalid ------> Display Error
  |
  v
Identify User Role
  |
  +----------+----------+----------+
  |          |          |
Student    Faculty     Admin
  |          |          |
  +----------+----------+
             |
             v
       Select Operation
             |
   +---------+---------+
   |         |         |
Resources  Booking   Events
             |
             v
     Check Availability
             |
       +-----+-----+
       |           |
   Available    Conflict
       |           |
       v           v
  Save Booking   Reject
       |
       v
 Notification
       |
       v
 Return to Dashboard
       |
       v
     Logout
       |
       v
      End
```

## 3. Component Diagram

```text
+------------------------------------------------------+
|                 CampusFlow System                    |
+------------------------------------------------------+

+-----------------------------+
| Presentation Layer          |
|-----------------------------|
| Main.java                   |
| Menu.java                   |
+-------------+---------------+
              |
              v
+-----------------------------+
| Service Layer               |
|-----------------------------|
| AuthService                 |
| BookingService              |
| EventService                |
| ReportService               |
| NotificationService         |
| ActivityHistory             |
+-------------+---------------+
              |
              v
+-----------------------------+
| Repository Layer            |
|-----------------------------|
| UserRepository              |
| ResourceRepository          |
| BookingRepository           |
| EventRepository             |
+-------------+---------------+
              |
              v
+-----------------------------+
| Persistence Layer           |
|-----------------------------|
| JPA / Hibernate             |
| JDBC                        |
| H2 Database                 |
+-----------------------------+

Supporting Services:

JdbcReportService
FileReportWriter
BookingMonitor
ReflectionService
```

## 4. Class Diagram

```text
                    +------------------+
                    | <<abstract>>     |
                    | User             |
                    +--------+---------+
                             |
              +--------------+--------------+
              |              |              |
              v              v              v
        +-----------+  +-----------+  +-----------+
        | Student   |  | Faculty   |  | Admin     |
        +-----------+  +-----------+  +-----------+

                    +------------------+
                    | <<abstract>>     |
                    | Resource         |
                    +--------+---------+
                             |
              +--------------+--------------+
              |              |              |
              v              v              v
        +-----------+  +-----------+  +-----------+
        | Classroom |  | Laboratory|  | Equipment |
        +-----------+  +-----------+  +-----------+

+-------------------+       +-------------------+
| Booking           |       | CampusEvent       |
+-------------------+       +---------+---------+
                                      |
                                      v
                            +-------------------+
                            | EventRegistration |
                            +-------------------+

+-------------------+
| BookingService    |
+---------+---------+
          |
     +----+----------------+
     |                     |
     v                     v
+----------------+  +-------------------+
| BookingRepo    |  | ResourceRepo      |
+----------------+  +-------------------+

+-------------------+
| EventService     |
+---------+--------+
          |
          v
+-------------------+
| EventRepository   |
+-------------------+
```

## 5. Booking Sequence Diagram

```text
User
 |
 | createBooking()
 v
BookingService
 |
 | findById(resourceId)
 v
ResourceRepository
 |
 | SELECT resource
 v
H2 Database
 |
 | resource
 v
ResourceRepository
 |
 v
BookingService
 |
 | findForResource(resourceId)
 v
BookingRepository
 |
 | SELECT existing bookings
 v
H2 Database
 |
 | existing bookings
 v
BookingRepository
 |
 v
BookingService
 |
 | Check time overlap
 |
 +-----------------------+
 |                       |
No conflict           Conflict
 |                       |
 v                       v
Save Booking      BookingConflictException
 |
 v
BookingRepository
 |
 | INSERT
 v
H2 Database
 |
 v
BookingService
 |
 | send confirmation
 v
NotificationService
 |
 v
User
```

## 6. Event Registration Sequence

```text
User
 |
 | Select Event
 v
EventService
 |
 | Find Event
 v
EventRepository
 |
 | Query Event
 v
H2 Database
 |
 | Event Details
 v
EventRepository
 |
 v
EventService
 |
 | Validate Registration
 |
 | Save Registration
 v
EventRepository
 |
 | INSERT
 v
H2 Database
 |
 v
NotificationService
 |
 v
User
```

## 7. ER Design

```text
+----------------------+
| USERS                |
+----------------------+
| ID PK                |
| NAME                 |
| EMAIL                |
| PASSWORD             |
| ROLE                 |
+----------+-----------+
           |
           | 1
           |
           | M
+----------v-----------+
| BOOKINGS             |
+----------------------+
| ID PK                |
| USER_ID FK           |
| RESOURCE_ID FK       |
| BOOKING_DATE         |
| START_TIME           |
| END_TIME             |
| STATUS               |
+----------+-----------+
           |
           | M
           |
           | 1
+----------v-----------+
| RESOURCES            |
+----------------------+
| ID PK                |
| NAME                 |
| LOCATION             |
| CAPACITY             |
| AVAILABLE            |
| RESOURCE_TYPE        |
+----------------------+

+----------------------+
| EVENT_REGISTRATIONS  |
+----------------------+
| ID PK                |
| USER_ID FK           |
| EVENT_ID FK          |
+----------+-----------+
           |
           | M
           |
           | 1
+----------v-----------+
| CAMPUS_EVENTS        |
+----------------------+
| ID PK                |
| NAME                 |
| CATEGORY             |
| EVENT_DATE            |
| VENUE                |
| CAPACITY             |
+----------------------+

+----------------------+
| NOTIFICATIONS        |
+----------------------+
| ID PK                |
| USER_ID FK           |
| MESSAGE              |
| CREATED_AT            |
+----------------------+
```

## 8. Data Flow

```text
User Input
    |
    v
Main / Menu
    |
    v
Service Layer
    |
    +---- Validation
    |
    +---- Business Rules
    |
    +---- Conflict Detection
    |
    v
Repository Layer
    |
    +---- JPA / Hibernate
    |
    +---- JDBC
    |
    v
H2 Database
    |
    v
Result
    |
    +---- Console Output
    |
    +---- Notification
    |
    +---- File Report
```

## 9. Design Principles Demonstrated

- Separation of concerns
- Encapsulation
- Inheritance
- Polymorphism
- Method overloading and overriding
- Abstract classes
- Interfaces
- Exception handling
- Multithreading
- Synchronization
- Collections
- Arrays
- File I/O
- JDBC
- JPA
- Annotations
- Reflection
- Nested classes
- Repository pattern
