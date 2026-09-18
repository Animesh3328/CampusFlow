package com.campusflow.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long resourceId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    protected Booking() {
    }

    private Booking(Long userId, Long resourceId, LocalDate date,
                    LocalTime start, LocalTime end) {
        this.userId = userId;
        this.resourceId = resourceId;
        this.bookingDate = date;
        this.startTime = start;
        this.endTime = end;
        this.status = BookingStatus.CONFIRMED;
    }

    public static class Builder {
        private Long userId;
        private Long resourceId;
        private LocalDate date;
        private LocalTime start;
        private LocalTime end;

        public Builder user(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder resource(Long resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder time(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
            return this;
        }

        public Booking build() {
            return new Booking(userId, resourceId, date, start, end);
        }
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getResourceId() { return resourceId; }
    public LocalDate getBookingDate() { return bookingDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public BookingStatus getStatus() { return status; }

    public void cancel() {
        status = BookingStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return id + " | resource=" + resourceId + " | date=" + bookingDate +
                " | " + startTime + "-" + endTime + " | " + status;
    }
}
