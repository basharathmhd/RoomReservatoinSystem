package com.oceanview.util;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONUtilTest {

    static class Dummy {
        private String name;
        private int age;

        public Dummy(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    void testToJSONNull() {
        assertEquals("null", JSONUtil.toJSON(null));
    }

    @Test
    void testToJSONString() {
        assertEquals("\"test\"", JSONUtil.toJSON("test"));
        assertEquals("\"multi\\nline\"", JSONUtil.toJSON("multi\nline"));
        assertEquals("\"quote\\\"test\\\"\"", JSONUtil.toJSON("quote\"test\""));
    }

    @Test
    void testToJSONNumberAndBoolean() {
        assertEquals("123", JSONUtil.toJSON(123));
        assertEquals("45.67", JSONUtil.toJSON(45.67));
        assertEquals("true", JSONUtil.toJSON(true));
        assertEquals("false", JSONUtil.toJSON(false));
    }

    @Test
    void testToJSONDate() {
        LocalDate date = LocalDate.of(2023, 10, 27);
        assertEquals("\"2023-10-27\"", JSONUtil.toJSON(date));
    }

    @Test
    void testToJSONDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 10, 27, 14, 30, 0);
        assertEquals("\"2023-10-27T14:30:00\"", JSONUtil.toJSON(dateTime));
    }

    @Test
    void testToJSONCollection() {
        List<String> list = Arrays.asList("apple", "banana");
        assertEquals("[\"apple\",\"banana\"]", JSONUtil.toJSON(list));
    }

    @Test
    void testToJSONObject() {
        Dummy dummy = new Dummy("John", 30);
        String json = JSONUtil.toJSON(dummy);
        assertTrue(json.contains("\"name\":\"John\""));
        assertTrue(json.contains("\"age\":30"));
        assertTrue(json.startsWith("{") && json.endsWith("}"));
    }

    @Test
    void testCreateResponse() {
        String response = JSONUtil.createResponse(true, "Success", "data");
        assertTrue(response.contains("\"success\":true"));
        assertTrue(response.contains("\"message\":\"Success\""));
        assertTrue(response.contains("\"data\":\"data\""));
    }

    @Test
    void testCreateErrorResponse() {
        String response = JSONUtil.createErrorResponse("Error message");
        assertTrue(response.contains("\"success\":false"));
        assertTrue(response.contains("\"message\":\"Error message\""));
        assertFalse(response.contains("\"data\""));
    }

    @Test
    void testCreateSuccessResponse() {
        String response = JSONUtil.createSuccessResponse("Success message", 123);
        assertTrue(response.contains("\"success\":true"));
        assertTrue(response.contains("\"message\":\"Success message\""));
        assertTrue(response.contains("\"data\":123"));
    }
}
