package com.oceanview.modal;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SystemLogTest {

    @Test
    void testSystemLogConstructors() {
        SystemLog log1 = new SystemLog();
        assertNotNull(log1.getTimestamp());

        SystemLog log2 = new SystemLog("U001", "LOGIN", "User logged in successfully");
        assertEquals("U001", log2.getUserId());
        assertEquals("LOGIN", log2.getAction());
        assertEquals("User logged in successfully", log2.getDescription());
        assertNotNull(log2.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        SystemLog log = new SystemLog();
        log.setLogId("L001");

        LocalDateTime time = LocalDateTime.now();
        log.setTimestamp(time);

        log.setUserId("U002");
        log.setAction("UPDATE");
        log.setDescription("Updated room rate");
        log.setIpAddress("192.168.1.1");
        log.setEntityType("Room");
        log.setEntityId("R005");

        assertEquals("L001", log.getLogId());
        assertEquals(time, log.getTimestamp());
        assertEquals("U002", log.getUserId());
        assertEquals("UPDATE", log.getAction());
        assertEquals("Updated room rate", log.getDescription());
        assertEquals("192.168.1.1", log.getIpAddress());
        assertEquals("Room", log.getEntityType());
        assertEquals("R005", log.getEntityId());
    }

    @Test
    void testToString() {
        SystemLog log = new SystemLog("U003", "DELETE", "Deleted record");
        log.setLogId("L002");
        String str = log.toString();

        assertTrue(str.contains("L002"));
        assertTrue(str.contains("U003"));
        assertTrue(str.contains("DELETE"));
    }
}
