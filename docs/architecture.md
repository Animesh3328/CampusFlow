# System Architecture

CampusFlow follows a layered architecture:

```text
+---------------------------+
| Console UI / Main         |
+-------------+-------------+
              |
              v
+---------------------------+
| Service Layer             |
| Auth / Booking / Event    |
| Report / Notification     |
+-------------+-------------+
              |
              v
+---------------------------+
| Repository Layer          |
| JPA Repositories          |
+-------------+-------------+
              |
              v
+---------------------------+
| H2 Database               |
+---------------------------+

Supporting services:
- JDBC reporting
- File I/O
- Background booking monitor
- Reflection diagnostics
```

The design keeps user interaction separate from business rules and persistence.
