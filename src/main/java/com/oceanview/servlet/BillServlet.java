package com.oceanview.servlet;

import com.oceanview.dao.BillDAO;
import com.oceanview.modal.Bill;
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

@WebServlet(name = "BillServlet", urlPatterns = { "/api/bills/*" })
public class BillServlet extends HttpServlet {

    private BillDAO billDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        billDAO = new BillDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<Bill> bills = billDAO.findAll();
                sendResponse(response, JSONUtil.createSuccessResponse("Bills retrieved", bills));
            } else {
                String id = pathInfo.substring(1);
                Bill bill = billDAO.findById(id);
                if (bill != null) {
                    sendResponse(response, JSONUtil.createSuccessResponse("Bill found", bill));
                } else {
                    sendError(response, "Bill not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error retrieving bills: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = getRequestBody(request);

            Bill bill = new Bill();
            bill.setBillId("BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            bill.setReservationNumber(extractJsonValue(body, "reservationNumber"));

            String rcStr = extractJsonValue(body, "roomCharges");
            if (rcStr != null && !rcStr.isEmpty())
                bill.setRoomCharges(Double.parseDouble(rcStr));

            String scStr = extractJsonValue(body, "serviceCharges");
            if (scStr != null && !scStr.isEmpty())
                bill.setServiceCharges(Double.parseDouble(scStr));

            String taStr = extractJsonValue(body, "taxAmount");
            if (taStr != null && !taStr.isEmpty())
                bill.setTaxAmount(Double.parseDouble(taStr));

            String daStr = extractJsonValue(body, "discountAmount");
            if (daStr != null && !daStr.isEmpty())
                bill.setDiscountAmount(Double.parseDouble(daStr));

            bill.calculateTotalAmount();
            bill.calculateTax(0.1); // 10% tax
            bill.calculateFinalAmount();

            bill.setPaymentStatus("PENDING");
            bill.setNotes(extractJsonValue(body, "notes"));

            boolean success = billDAO.insert(bill);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Bill created", bill));
            } else {
                sendError(response, "Failed to create bill");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error creating bill: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Bill ID required");
                return;
            }

            String id = pathInfo.substring(1);
            Bill existing = billDAO.findById(id);
            if (existing == null) {
                sendError(response, "Bill not found");
                return;
            }

            String body = getRequestBody(request);

            String status = extractJsonValue(body, "status");
            if (status != null)
                existing.setPaymentStatus(status);

            String notes = extractJsonValue(body, "notes");
            if (notes != null)
                existing.setNotes(notes);

            boolean success = billDAO.update(existing);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Bill updated", existing));
            } else {
                sendError(response, "Failed to update bill");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error updating bill: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Bill ID required");
                return;
            }

            String id = pathInfo.substring(1);
            boolean success = billDAO.delete(id);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Bill deleted", null));
            } else {
                sendError(response, "Bill not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error deleting bill: " + e.getMessage());
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
