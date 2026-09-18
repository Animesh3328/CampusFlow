package com.campusflow;

import com.campusflow.exception.BookingConflictException;
import com.campusflow.model.*;
import com.campusflow.repository.BookingRepository;
import com.campusflow.repository.ResourceRepository;
import com.campusflow.service.BookingService;
import com.campusflow.service.NotificationService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    @Test
    void overlappingBookingsShouldBeRejected() {
        BookingRepository bookingRepository = new BookingRepository() {
            @Override
            public List<Booking> findForResource(Long id, LocalDate date) {
                return List.of(new Booking.Builder()
                        .user(1L)
                        .resource(id)
                        .date(date)
                        .time(LocalTime.of(10, 0), LocalTime.of(11, 0))
                        .build());
            }
        };

        Resource resource = new Classroom("Room", "Block A", 30, true);
        User user = new Student("Test", "test@example.com", "secret1", "CSE");

        ResourceRepository resourceRepository = new ResourceRepository() {
            @Override
            public Resource findById(Long id) {
                return resource;
            }
        };

        NotificationService notification = (uid, message) -> {};

        BookingService service =
                new BookingService(bookingRepository, resourceRepository, notification);

        assertThrows(BookingConflictException.class, () ->
                service.createBooking(user, resource, LocalDate.now().plusDays(1),
                        LocalTime.of(10, 30), LocalTime.of(11, 30)));
    }

    @Test
    void resourceCostShouldUseOverriddenMethod() {
        Resource room = new Classroom("Room", "A", 20, false);
        Resource lab = new Laboratory("Lab", "B", 20, 20);

        assertEquals(100.0, room.calculateBookingCost());
        assertEquals(250.0, lab.calculateBookingCost());
    }
}
