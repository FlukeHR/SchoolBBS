package com.bbs.controller.recharge;

import com.bbs.service.impl.RechargeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/RechargePay")
public class RechargePay extends HttpServlet {
    private RechargeService rechargeService = new RechargeService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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

            String orderNo = request.getParameter("orderNo");

            if (orderNo == null || orderNo.trim().isEmpty()) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"缺少订单号\"}");
                return;
            }

            // 模拟支付成功（实际项目中这里会调用第三方支付接口）
            boolean success = rechargeService.completeRecharge(orderNo);
            
            if (success) {
                response.getWriter().write("{\"ok\":true,\"msg\":\"充值成功\"}");
            } else {
                response.getWriter().write("{\"ok\":false,\"msg\":\"充值失败，订单不存在或已处理\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"充值失败：" + e.getMessage() + "\"}");
        }
    }
}