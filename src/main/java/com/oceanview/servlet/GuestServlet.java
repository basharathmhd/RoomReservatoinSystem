package com.oceanview.servlet;

import com.oceanview.dao.GuestDAO;
import com.oceanview.modal.Guest;
import com.oceanview.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "GuestServlet", urlPatterns = { "/api/guests/*" })
public class GuestServlet extends HttpServlet {

    private GuestDAO guestDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        guestDAO = new GuestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                String search = request.getParameter("search");
                List<Guest> guests;
                if (search != null && !search.trim().isEmpty()) {
                    guests = guestDAO.searchByName(search);
                } else {
                    guests = guestDAO.findAll();
                }
                sendResponse(response, JSONUtil.createSuccessResponse("Guests retrieved", guests));
            } else {
                String id = pathInfo.substring(1);
                Guest guest = guestDAO.findById(id);
                if (guest != null) {
                    sendResponse(response, JSONUtil.createSuccessResponse("Guest found", guest));
                } else {
                    sendError(response, "Guest not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error retrieving guests: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = getRequestBody(request);

            Guest guest = new Guest();
            guest.setGuestId("GST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            guest.setFirstName(extractJsonValue(body, "firstName"));
            guest.setLastName(extractJsonValue(body, "lastName"));
            guest.setAddress(extractJsonValue(body, "address"));
            guest.setContactNumber(extractJsonValue(body, "contactNumber"));
            guest.setEmail(extractJsonValue(body, "email"));
            guest.setIdentificationNumber(extractJsonValue(body, "identificationNumber"));
            guest.setNationality(extractJsonValue(body, "nationality"));

            String dob = extractJsonValue(body, "dateOfBirth");
            if (dob != null && !dob.isEmpty()) {
                guest.setDateOfBirth(LocalDate.parse(dob));
            }

            guest.setCreatedBy("system");

            boolean success = guestDAO.insert(guest);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Guest created", guest));
            } else {
                sendError(response, "Failed to create guest");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error creating guest: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Guest ID required");
                return;
            }

            String id = pathInfo.substring(1);
            Guest existing = guestDAO.findById(id);
            if (existing == null) {
                sendError(response, "Guest not found");
                return;
            }

            String body = getRequestBody(request);

            String firstName = extractJsonValue(body, "firstName");
            if (firstName != null)
                existing.setFirstName(firstName);

            String lastName = extractJsonValue(body, "lastName");
            if (lastName != null)
                existing.setLastName(lastName);

            String address = extractJsonValue(body, "address");
            if (address != null)
                existing.setAddress(address);

            String contactNumber = extractJsonValue(body, "contactNumber");
            if (contactNumber != null)
                existing.setContactNumber(contactNumber);

            String email = extractJsonValue(body, "email");
            if (email != null)
                existing.setEmail(email);

            String idNumber = extractJsonValue(body, "identificationNumber");
            if (idNumber != null)
                existing.setIdentificationNumber(idNumber);

            String nationality = extractJsonValue(body, "nationality");
            if (nationality != null)
                existing.setNationality(nationality);

            String dob = extractJsonValue(body, "dateOfBirth");
            if (dob != null && !dob.isEmpty()) {
                existing.setDateOfBirth(LocalDate.parse(dob));
            }

            existing.setModifiedDate(java.time.LocalDateTime.now());
            existing.setModifiedBy("system");

            boolean success = guestDAO.update(existing);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Guest updated", existing));
            } else {
                sendError(response, "Failed to update guest");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error updating guest: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Guest ID required");
                return;
            }

            String id = pathInfo.substring(1);
            boolean success = guestDAO.delete(id);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Guest deleted", null));
            } else {
                sendError(response, "Guest not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error deleting guest: " + e.getMessage());
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1)
            return null;

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1)
            return null;

        int valueStart = colonIndex + 1;
        while (valueStart < json.length() &&
                (json.charAt(valueStart) == ' ' || json.charAt(valueStart) == '"')) {
            valueStart++;
        }

        int valueEnd = valueStart;
        while (valueEnd < json.length() &&
                json.charAt(valueEnd) != '"' &&
                json.charAt(valueEnd) != ',' &&
                json.charAt(valueEnd) != '}') {
            valueEnd++;
        }

        return json.substring(valueStart, valueEnd).trim();
    }

    private void sendResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = response.getWriter();
        out.print(JSONUtil.createErrorResponse(message));
        out.flush();
    }
}
