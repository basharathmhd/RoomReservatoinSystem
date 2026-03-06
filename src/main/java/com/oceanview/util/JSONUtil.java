package com.oceanview.util;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;


public class JSONUtil {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static String toJSON(Object obj) {
        if (obj == null) {
            return "null";
        }

        if (obj instanceof String) {
            return "\"" + escapeJSON((String) obj) + "\"";
        }

        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }

        if (obj instanceof LocalDate) {
            return "\"" + ((LocalDate) obj).format(DATE_FORMATTER) + "\"";
        }

        if (obj instanceof LocalDateTime) {
            return "\"" + ((LocalDateTime) obj).format(DATETIME_FORMATTER) + "\"";
        }

        if (obj instanceof Collection) {
            return collectionToJSON((Collection<?>) obj);
        }

        // Handle custom objects
        return objectToJSON(obj);
    }

   
    private static String collectionToJSON(Collection<?> collection) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Object item : collection) {
            if (!first) {
                json.append(",");
            }
            json.append(toJSON(item));
            first = false;
        }

        json.append("]");
        return json.toString();
    }

  
    private static String objectToJSON(Object obj) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;

        try {
            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {
                // Skip serialVersionUID and other static fields
                if (field.getName().equals("serialVersionUID") ||
                        java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);
                Object value = field.get(obj);

                // Skip null values
                if (value == null) {
                    continue;
                }

                if (!first) {
                    json.append(",");
                }

                json.append("\"").append(field.getName()).append("\":");
                json.append(toJSON(value));

                first = false;
            }
        } catch (IllegalAccessException e) {
            System.err.println("Error converting object to JSON: " + e.getMessage());
        }

        json.append("}");
        return json.toString();
    }

    
    private static String escapeJSON(String str) {
        if (str == null) {
            return "";
        }

        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    
    public static String createResponse(boolean success, String message, Object data) {
        StringBuilder json = new StringBuilder("{");
        json.append("\"success\":").append(success).append(",");
        json.append("\"message\":\"").append(escapeJSON(message)).append("\"");

        if (data != null) {
            json.append(",\"data\":").append(toJSON(data));
        }

        json.append("}");
        return json.toString();
    }

   
    public static String createErrorResponse(String message) {
        return createResponse(false, message, null);
    }

   
    public static String createSuccessResponse(String message, Object data) {
        return createResponse(true, message, data);
    }
}
