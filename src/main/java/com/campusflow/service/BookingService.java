package com.campusflow.service;

import com.campusflow.annotation.Auditable;
import com.campusflow.exception.*;
import com.campusflow.model.*;
import com.campusflow.repository.BookingRepository;
import com.campusflow.repository.ResourceRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingService {

    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;
    private final NotificationService notificationService;

    public BookingService(BookingRepository bookingRepository,
                          ResourceRepository resourceRepository,
                          NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.resourceRepository = resourceRepository;
        this.notificationService = notificationService;
    }

    @Auditable("Create booking")
    public synchronized Booking createBooking(User user, Resource resource)
            throws CampusException {
        return createBooking(user, resource, LocalDate.now().plusDays(1),
                LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @Auditable("Create booking with time")
    public synchronized Booking createBooking(User user, Resource resource,
                                              LocalDate date,
                                              LocalTime start,
                                              LocalTime end)
            throws CampusException {

        if (date == null || start == null || end == null) {
            throw new ValidationException("Date and time are required.");
        }

        if (!end.isAfter(start)) {
            throw new ValidationException("End time must be after start time.");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new ValidationException("Booking date cannot be in the past.");
        }

        Resource current = resourceRepository.findById(resource.getId());
        if (current == null || !current.isAvailable()) {
            throw new ResourceUnavailableException("The selected resource is unavailable.");
        }

        List<Booking> existing =
                bookingRepository.findForResource(resource.getId(), date);

        for (Booking booking : existing) {
            boolean overlaps = start.isBefore(booking.getEndTime())
                    && end.isAfter(booking.getStartTime());

            if (overlaps) {
                throw new BookingConflictException(
                        "The resource is already booked during the selected time.");
            }
        }

        Booking booking = new Booking.Builder()
                .user(user.getId())
                .resource(resource.getId())
                .date(date)
                .time(start, end)
                .build();

        bookingRepository.save(booking);
        notificationService.send(user.getId(),
                "Booking confirmed for " + resource.getName() + " on " + date);

        return booking;
    }

    public void cancelBooking(Long bookingId) throws ValidationException {
        Booking booking = bookingRepository.findById(bookingId);

        if (booking == null) {
            throw new ValidationException("Booking not found.");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ValidationException("Booking is already cancelled.");
        }

        booking.cancel();
        bookingRepository.update(booking);
    }
}
