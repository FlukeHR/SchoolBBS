package com.bbs.controller.recharge;

import com.bbs.service.impl.RechargeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/UserBalance")
public class UserBalance extends HttpServlet {
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
                response.getWriter().write("{\"ok\":false,\"msg\":\"请先登录\"}");
                return;
            }

            BigDecimal balance = rechargeService.getUserBalance(currentUser);
            
            response.getWriter().write("{\"ok\":true,\"balance\":" + balance + "}");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"查询余额失败：" + e.getMessage() + "\"}");
        }
    }
}