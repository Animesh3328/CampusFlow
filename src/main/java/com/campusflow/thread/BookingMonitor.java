package com.campusflow.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Background worker used to demonstrate Java multithreading without
 * continuously writing status messages to the application console.
 */
public class BookingMonitor extends Thread {

    private volatile boolean running = true;
    private final AtomicInteger healthChecks = new AtomicInteger();

    @Override
    public void run() {
        while (running) {
            healthChecks.incrementAndGet();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public int getHealthChecks() {
        return healthChecks.get();
    }

    public void shutdown() {
        running = false;
        interrupt();
    }
}
