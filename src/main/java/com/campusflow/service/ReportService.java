package com.campusflow.service;

import com.campusflow.model.Booking;
import com.campusflow.repository.BookingRepository;
import com.campusflow.repository.ResourceRepository;

import java.util.*;

public class ReportService {

    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;

    public ReportService(BookingRepository bookingRepository,
                         ResourceRepository resourceRepository) {
        this.bookingRepository = bookingRepository;
        this.resourceRepository = resourceRepository;
    }

    public String buildUserReport(Long userId) {
        List<Booking> bookings = bookingRepository.findForUser(userId);

        StringBuilder report = new StringBuilder();
        report.append("CAMPUSFLOW USER ACTIVITY REPORT\n");
        report.append("================================\n");
        report.append("Total bookings: ").append(bookings.size()).append("\n\n");

        for (Booking booking : bookings) {
            report.append(booking).append('\n');
        }

        return report.toString();
    }

    public String buildUsageReport() {
        List<Booking> all = new ArrayList<>();
        for (var resource : resourceRepository.findAll()) {
            all.addAll(bookingRepository.findForResource(
                    resource.getId(), java.time.LocalDate.now()));
        }

        int[] usage = new int[3];
        int[][] dailyMatrix = new int[3][4];

        for (Booking b : all) {
            int index = (int) (b.getResourceId() % 3);
            usage[index]++;
            dailyMatrix[index][0]++;
        }

        StringBuilder result = new StringBuilder();
        result.append("RESOURCE USAGE SNAPSHOT\n");
        result.append("=======================\n");

        for (int i = 0; i < usage.length; i++) {
            result.append("Category ").append(i + 1)
                    .append(": ").append(usage[i]).append(" booking(s)\n");
        }

        return result.toString();
    }
}
