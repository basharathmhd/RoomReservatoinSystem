package com.oceanview.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        // Remove spaces, hyphens, parentheses and plus sign
        String cleanPhone = phone.replaceAll("[\\s\\-()+]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return ALPHANUMERIC_PATTERN.matcher(str).matches();
    }

    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (isEmpty(str)) {
            return false;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }

    public static boolean isPositiveNumber(double number) {
        return number > 0;
    }

    public static boolean isInRange(int number, int min, int max) {
        return number >= min && number <= max;
    }

    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        if (input.trim().isEmpty()) {
            return input;
        }
        // Remove potentially dangerous characters
        return input.replaceAll("[';\"\\\\*]", "");
    }

    public static boolean isValidReservationNumber(String reservationNumber) {
        if (isEmpty(reservationNumber)) {
            return false;
        }
        return Pattern.matches("^RES-\\d{4}-\\d{5}$", reservationNumber);
    }
}
