package com.oceanview.servlet;

import com.oceanview.dao.PaymentDAO;
import com.oceanview.dao.BillDAO;
import com.oceanview.modal.Payment;
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

@WebServlet(name = "PaymentServlet", urlPatterns = { "/api/payments/*" })
public class PaymentServlet extends HttpServlet {

    private PaymentDAO paymentDAO;
    private BillDAO billDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        paymentDAO = new PaymentDAO();
        billDAO = new BillDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<Payment> payments = paymentDAO.findAll();
                sendResponse(response, JSONUtil.createSuccessResponse("Payments retrieved", payments));
            } else {
                String id = pathInfo.substring(1);
                Payment payment = paymentDAO.findById(id);
                if (payment != null) {
                    sendResponse(response, JSONUtil.createSuccessResponse("Payment found", payment));
                } else {
                    sendError(response, "Payment not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error retrieving payments: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = getRequestBody(request);

            Payment p = new Payment();
            p.setPaymentId("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            p.setBillId(extractJsonValue(body, "billId"));

            String amountStr = extractJsonValue(body, "amount");
            if (amountStr != null)
                p.setAmount(Double.parseDouble(amountStr));

            p.setPaymentMethod(extractJsonValue(body, "paymentMethod"));
            p.setTransactionId(extractJsonValue(body, "transactionId"));
            p.setStatus("SUCCESS");
            p.setRemarks(extractJsonValue(body, "remarks"));

            boolean success = paymentDAO.insert(p);
            if (success) {
                // Update bill status if paid in full
                Bill bill = billDAO.findById(p.getBillId());
                if (bill != null) {
                    bill.setPaymentStatus("PAID");
                    bill.setPaymentMethod(p.getPaymentMethod());
                    billDAO.update(bill);
                }
                sendResponse(response, JSONUtil.createSuccessResponse("Payment recorded", p));
            } else {
                sendError(response, "Failed to record payment");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error recording payment: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Payment ID required");
                return;
            }

            String id = pathInfo.substring(1);
            Payment existing = paymentDAO.findById(id);
            if (existing == null) {
                sendError(response, "Payment not found");
                return;
            }

            String body = getRequestBody(request);

            String status = extractJsonValue(body, "status");
            if (status != null)
                existing.setStatus(status);

            String remarks = extractJsonValue(body, "remarks");
            if (remarks != null)
                existing.setRemarks(remarks);

            boolean success = paymentDAO.update(existing);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Payment updated", existing));
            } else {
                sendError(response, "Failed to update payment");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error updating payment: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "Payment ID required");
                return;
            }

            String id = pathInfo.substring(1);
            boolean success = paymentDAO.delete(id);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("Payment deleted", null));
            } else {
                sendError(response, "Payment not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error deleting payment: " + e.getMessage());
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
