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

@WebServlet("/RechargeCreate")
public class RechargeCreate extends HttpServlet {
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

            String amountStr = request.getParameter("amount");
            String paymentMethod = request.getParameter("paymentMethod");

            if (amountStr == null || amountStr.trim().isEmpty()) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"请输入充值金额\"}");
                return;
            }

            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"请选择支付方式\"}");
                return;
            }

            // 验证金额格式
            BigDecimal amount;
            try {
                // 清理金额字符串，移除可能的特殊字符
                String cleanAmountStr = amountStr.trim().replaceAll("[^0-9.]", "");
                if (cleanAmountStr.isEmpty()) {
                    throw new NumberFormatException("Invalid amount format");
                }
                amount = new BigDecimal(cleanAmountStr);
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"The string did not match the expected pattern.\"}");
                return;
            }
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"充值金额必须大于0\"}");
                return;
            }

            if (amount.compareTo(new BigDecimal("10000")) > 0) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"单次充值金额不能超过10000元\"}");
                return;
            }

            if (!"WECHAT".equals(paymentMethod) && !"ALIPAY".equals(paymentMethod)) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"不支持的支付方式\"}");
                return;
            }

            String orderNo = rechargeService.createRechargeOrder(currentUser, amount, paymentMethod);
            
            response.getWriter().write("{\"ok\":true,\"orderNo\":\"" + orderNo + "\",\"msg\":\"充值订单创建成功\"}");

        } catch (NumberFormatException e) {
            response.getWriter().write("{\"ok\":false,\"msg\":\"金额格式错误\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"创建充值订单失败：" + e.getMessage() + "\"}");
        }
    }
}