package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.modal.Reservation;
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

@WebServlet(name = "ReservationServlet", urlPatterns = { "/api/reservations/*" })
public class ReservationServlet extends HttpServlet {

    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<Reservation> reservations = reservationDAO.findAll();
                sendResponse(response, JSONUtil.createSuccessResponse("Reservations retrieved", reservations));
            } else if (pathInfo.equals("/check-availability")) {
                String roomId = request.getParameter("roomId");
                String checkIn = request.getParameter("checkIn");
                String checkOut = request.getParameter("checkOut");

                if (roomId == null || checkIn == null || checkOut == null) {
                    sendError(response, "roomId, checkIn, and checkOut parameters required");
                    return;
                }

                boolean available = reservationDAO.checkAvailability(
                        roomId, LocalDate.parse(checkIn), LocalDate.parse(checkOut));

                String json = String.format(
                        "{\"success\":true,\"available\":%s,\"message\":\"%s\"}",
                        available,
                        available ? "Room is available" : "Room is not available for these dates");
                sendResponse(response, json);
            } else {
                String id = pathInfo.substring(1);
                Reservation reservation = reservationDAO.findById(id);
                if (reservation != null) {
                    sendResponse(response, JSONUtil.createSuccessResponse("Reservation found", reservation));
                } else {
                    sendError(response, "Reservation not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error retrieving reservations: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = getRequestBody(request);

            String guestId = extractJsonValue(body, "guestId");
            String roomId = extractJsonValue(body, "roomId");
            String checkInStr = extractJsonValue(body, "checkInDate");
            String checkOutStr = extractJsonValue(body, "checkOutDate");

            if (guestId == null || roomId == null || checkInStr == null || checkOutStr == null) {
                sendError(response, "guestId, roomId, checkInDate, and checkOutDate are required");
                return;
            }

            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);

            // Check availability first
            boolean available = reservationDAO.checkAvailability(roomId, checkIn, checkOut);
            if (!available) {
                sendError(response, "Room is not available for the selected dates");
                return;
            }

            Reservation reservation = new Reservation();
            String resNumber = "RES-" + java.time.Year.now().getValue() + "-" +
                    String.format("%05d", (int) (Math.random() * 99999));
            reservation.setReservationNumber(resNumber);
            reservation.setGuestId(guestId);
            reservation.setRoomId(roomId);
            reservation.setCheckInDate(checkIn);
            reservation.setCheckOutDate(checkOut);

            String numberOfGuestsStr = extractJsonValue(body, "numberOfGuests");
            if (numberOfGuestsStr != null) {
                reservation.setNumberOfGuests(Integer.parseInt(numberOfGuestsStr));
            }

            reservation.setSpecialRequests(extractJsonValue(body, "specialRequests"));
            reservation.setCreatedBy("system");

            boolean success = reservationDAO.insert(reservation);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Reservation created", reservation));
            } else {
                sendError(response, "Failed to create reservation");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error creating reservation: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Reservation number required");
                return;
            }

            String resNumber = pathInfo.substring(1);
            Reservation existing = reservationDAO.findById(resNumber);
            if (existing == null) {
                sendError(response, "Reservation not found");
                return;
            }

            String body = getRequestBody(request);

            String guestId = extractJsonValue(body, "guestId");
            if (guestId != null)
                existing.setGuestId(guestId);

            String roomId = extractJsonValue(body, "roomId");
            if (roomId != null)
                existing.setRoomId(roomId);

            String checkInStr = extractJsonValue(body, "checkInDate");
            if (checkInStr != null)
                existing.setCheckInDate(LocalDate.parse(checkInStr));

            String checkOutStr = extractJsonValue(body, "checkOutDate");
            if (checkOutStr != null)
                existing.setCheckOutDate(LocalDate.parse(checkOutStr));

            String numberOfGuestsStr = extractJsonValue(body, "numberOfGuests");
            if (numberOfGuestsStr != null)
                existing.setNumberOfGuests(Integer.parseInt(numberOfGuestsStr));

            String status = extractJsonValue(body, "status");
            if (status != null)
                existing.setStatus(status);

            String specialRequests = extractJsonValue(body, "specialRequests");
            if (specialRequests != null)
                existing.setSpecialRequests(specialRequests);

            boolean success = reservationDAO.update(existing);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Reservation updated", existing));
            } else {
                sendError(response, "Failed to update reservation");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error updating reservation: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Reservation number required");
                return;
            }

            String resNumber = pathInfo.substring(1);
            boolean success = reservationDAO.delete(resNumber);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Reservation cancelled", null));
            } else {
                sendError(response, "Reservation not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error deleting reservation: " + e.getMessage());
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
