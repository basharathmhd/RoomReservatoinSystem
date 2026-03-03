package com.oceanview.servlet;

import com.oceanview.dao.RoomDAO;
import com.oceanview.modal.Room;
import com.oceanview.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "RoomServlet", urlPatterns = { "/api/rooms/*" })
public class RoomServlet extends HttpServlet {

    private RoomDAO roomDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        roomDAO = new RoomDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/rooms — list all
                List<Room> rooms = roomDAO.findAll();
                sendResponse(response, JSONUtil.createSuccessResponse("Rooms retrieved", rooms));
            } else {
                // GET /api/rooms/{id}
                String id = pathInfo.substring(1);
                Room room = roomDAO.findById(id);
                if (room != null) {
                    sendResponse(response, JSONUtil.createSuccessResponse("Room found", room));
                } else {
                    sendError(response, "Room not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error retrieving rooms: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = getRequestBody(request);

            Room room = new Room();
            room.setRoomId("RM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            room.setRoomNumber(extractJsonValue(body, "roomNumber"));
            room.setTypeId(extractJsonValue(body, "typeId"));

            String floorStr = extractJsonValue(body, "floor");
            if (floorStr != null)
                room.setFloor(Integer.parseInt(floorStr));

            String capacityStr = extractJsonValue(body, "capacity");
            if (capacityStr != null)
                room.setCapacity(Integer.parseInt(capacityStr));

            room.setStatus(extractJsonValue(body, "status") != null ? extractJsonValue(body, "status") : "AVAILABLE");
            room.setAmenities(extractJsonValue(body, "amenities"));

            boolean success = roomDAO.insert(room);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Room created", room));
            } else {
                sendError(response, "Failed to create room");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error creating room: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Room ID required");
                return;
            }

            String id = pathInfo.substring(1);
            Room existing = roomDAO.findById(id);
            if (existing == null) {
                sendError(response, "Room not found");
                return;
            }

            String body = getRequestBody(request);

            String roomNumber = extractJsonValue(body, "roomNumber");
            if (roomNumber != null)
                existing.setRoomNumber(roomNumber);

            String typeId = extractJsonValue(body, "typeId");
            if (typeId != null)
                existing.setTypeId(typeId);

            String floorStr = extractJsonValue(body, "floor");
            if (floorStr != null)
                existing.setFloor(Integer.parseInt(floorStr));

            String capacityStr = extractJsonValue(body, "capacity");
            if (capacityStr != null)
                existing.setCapacity(Integer.parseInt(capacityStr));

            String status = extractJsonValue(body, "status");
            if (status != null)
                existing.setStatus(status);

            String amenities = extractJsonValue(body, "amenities");
            if (amenities != null)
                existing.setAmenities(amenities);

            boolean success = roomDAO.update(existing);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Room updated", existing));
            } else {
                sendError(response, "Failed to update room");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error updating room: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Room ID required");
                return;
            }

            String id = pathInfo.substring(1);
            boolean success = roomDAO.delete(id);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Room deleted", null));
            } else {
                sendError(response, "Room not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error deleting room: " + e.getMessage());
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
