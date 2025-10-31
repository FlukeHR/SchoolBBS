package com.bbs.controller.trade;

import com.bbs.entity.Trade;
import com.bbs.service.impl.TradeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/TradePurchaseHistory")
public class TradePurchaseHistory extends HttpServlet {
    private TradeService tradeService = new TradeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            HttpSession session = request.getSession();
            String currentUser = (String) session.getAttribute("username_session");
            
            if (currentUser == null || currentUser.trim().isEmpty()) {
                response.getWriter().write("[]");
                return;
            }

            List<Trade> purchases = tradeService.listTradesByBuyer(currentUser);
            
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            for (int i = 0; i < purchases.size(); i++) {
                Trade t = purchases.get(i);
                if (i > 0) sb.append(",");
                sb.append("{");
                sb.append("\"id\":").append(t.getId()).append(",");
                sb.append("\"title\":\"").append(escapeJson(t.getTitle())).append("\",");
                sb.append("\"price\":").append(t.getPrice() != null ? t.getPrice() : 0).append(",");
                sb.append("\"description\":\"").append(escapeJson(t.getDescription())).append("\",");
                sb.append("\"images\":\"").append(escapeJson(t.getImages())).append("\",");
                sb.append("\"author\":\"").append(escapeJson(t.getAuthor())).append("\",");
                sb.append("\"date\":\"").append(t.getDate() != null ? sdf.format(t.getDate()) : "").append("\",");
                sb.append("\"status\":\"").append(escapeJson(t.getStatus())).append("\",");
                sb.append("\"buyer\":\"").append(escapeJson(t.getBuyer())).append("\",");
                sb.append("\"paymentTime\":\"").append(t.getPaymentTime() != null ? sdf.format(t.getPaymentTime()) : "").append("\"");
                sb.append("}");
            }
            sb.append("]");
            
            response.getWriter().write(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("[]");
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}