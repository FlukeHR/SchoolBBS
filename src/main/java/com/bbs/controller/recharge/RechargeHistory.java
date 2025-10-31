package com.bbs.controller.recharge;

import com.bbs.entity.Recharge;
import com.bbs.service.impl.RechargeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/RechargeHistory")
public class RechargeHistory extends HttpServlet {
    private RechargeService rechargeService = new RechargeService();

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

            List<Recharge> recharges = rechargeService.getUserRechargeHistory(currentUser);
            
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            for (int i = 0; i < recharges.size(); i++) {
                Recharge r = recharges.get(i);
                if (i > 0) sb.append(",");
                sb.append("{");
                sb.append("\"id\":").append(r.getId()).append(",");
                sb.append("\"amount\":").append(r.getAmount()).append(",");
                sb.append("\"paymentMethod\":\"").append(escapeJson(r.getPaymentMethod())).append("\",");
                sb.append("\"status\":\"").append(escapeJson(r.getStatus())).append("\",");
                sb.append("\"orderNo\":\"").append(escapeJson(r.getOrderNo())).append("\",");
                sb.append("\"createTime\":\"").append(r.getCreateTime() != null ? sdf.format(r.getCreateTime()) : "").append("\",");
                sb.append("\"remark\":\"").append(escapeJson(r.getRemark() != null ? r.getRemark() : "")).append("\"");
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