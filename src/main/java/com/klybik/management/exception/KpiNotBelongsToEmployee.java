package com.klybik.management.exception;

public class KpiNotBelongsToEmployee extends RuntimeException {
    public KpiNotBelongsToEmployee(String message) {
        super(message);
    }
}
