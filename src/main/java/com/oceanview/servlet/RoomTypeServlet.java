package com.oceanview.servlet;

import com.oceanview.dao.RoomTypeDAO;
import com.oceanview.modal.RoomType;
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

@WebServlet(name = "RoomTypeServlet", urlPatterns = { "/api/room-types/*" })
public class RoomTypeServlet extends HttpServlet {

    private RoomTypeDAO roomTypeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        roomTypeDAO = new RoomTypeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<RoomType> types = roomTypeDAO.findAll();
                sendResponse(response, JSONUtil.createSuccessResponse("Room types retrieved", types));
            } else {
                String id = pathInfo.substring(1);
                RoomType type = roomTypeDAO.findById(id);
                if (type != null) {
                    sendResponse(response, JSONUtil.createSuccessResponse("Room type found", type));
                } else {
                    sendError(response, "Room type not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error retrieving room types: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = getRequestBody(request);

            RoomType rt = new RoomType();
            rt.setTypeId(extractJsonValue(body, "typeId"));
            rt.setTypeName(extractJsonValue(body, "typeName"));

            String rateStr = extractJsonValue(body, "baseRate");
            if (rateStr != null)
                rt.setBaseRate(Double.parseDouble(rateStr));

            rt.setDescription(extractJsonValue(body, "description"));

            String occStr = extractJsonValue(body, "maxOccupancy");
            if (occStr != null)
                rt.setMaxOccupancy(Integer.parseInt(occStr));

            rt.setAmenities(extractJsonValue(body, "amenities"));

            boolean success = roomTypeDAO.insert(rt);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Room type created", rt));
            } else {
                sendError(response, "Failed to create room type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error creating room type: " + e.getMessage());
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
        while (valueStart < json.length() && (json.charAt(valueStart) == ' ' || json.charAt(valueStart) == '"')) {
            valueStart++;
        }

        int valueEnd = valueStart;
        while (valueEnd < json.length() && json.charAt(valueEnd) != '"' && json.charAt(valueEnd) != ','
                && json.charAt(valueEnd) != '}') {
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
