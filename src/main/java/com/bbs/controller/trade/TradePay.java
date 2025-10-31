package com.bbs.controller.trade;

import com.bbs.entity.Trade;
import com.bbs.service.impl.TradeService;
import com.bbs.service.impl.RechargeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

@WebServlet("/TradePay")
public class TradePay extends HttpServlet {
    private TradeService tradeService = new TradeService();
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

            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"缺少交易ID\"}");
                return;
            }

            int tradeId = Integer.parseInt(idStr);
            Trade trade = tradeService.selectById(tradeId);

            if (trade == null) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"交易不存在\"}");
                return;
            }

            if (currentUser.equals(trade.getAuthor())) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"不能购买自己的商品\"}");
                return;
            }

            if (!"AVAILABLE".equals(trade.getStatus())) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"商品已被购买或不可购买\"}");
                return;
            }

            // 检查用户余额是否足够
            BigDecimal userBalance = rechargeService.getUserBalance(currentUser);
            if (userBalance == null) {
                userBalance = BigDecimal.ZERO;
            }
            Double tradePriceDouble = trade.getPrice();
            if (tradePriceDouble == null) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"商品价格信息错误\"}");
                return;
            }
            BigDecimal tradePrice = BigDecimal.valueOf(tradePriceDouble);
            
            if (userBalance.compareTo(tradePrice) < 0) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"余额不足，当前余额：￥" + userBalance + "，商品价格：￥" + tradePrice + "\"}");
                return;
            }

            // 扣减买家余额
            BigDecimal newBalance = userBalance.subtract(tradePrice);
            boolean balanceUpdated = updateUserBalance(currentUser, newBalance);
            
            if (!balanceUpdated) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"余额更新失败\"}");
                return;
            }

            // 更新交易状态为已支付
            boolean success = tradeService.updatePaymentStatus(tradeId, "PAID", currentUser, new Timestamp(System.currentTimeMillis()));
            
            if (success) {
                // 增加卖家余额
                try {
                    BigDecimal sellerBalance = rechargeService.getUserBalance(trade.getAuthor());
                    BigDecimal newSellerBalance = sellerBalance.add(tradePrice);
                    updateUserBalance(trade.getAuthor(), newSellerBalance);
                } catch (Exception e) {
                    // 如果卖家余额更新失败，记录错误但不影响交易成功
                    e.printStackTrace();
                }
                
                response.getWriter().write("{\"ok\":true,\"msg\":\"支付成功\"}");
            } else {
                // 如果交易状态更新失败，需要回滚买家余额
                try {
                    updateUserBalance(currentUser, userBalance);
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
                response.getWriter().write("{\"ok\":false,\"msg\":\"支付失败\"}");
            }

        } catch (NumberFormatException e) {
            response.getWriter().write("{\"ok\":false,\"msg\":\"交易ID格式错误\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"支付失败：" + e.getMessage() + "\"}");
        }
    }

    private boolean updateUserBalance(String username, BigDecimal newBalance) throws Exception {
        com.bbs.mapper.UserMapper userMapper;
        try (org.apache.ibatis.session.SqlSession session = getSessionFactory().openSession()) {
            userMapper = session.getMapper(com.bbs.mapper.UserMapper.class);
            
            // 确保用户表有余额字段
            try { 
                userMapper.addBalanceColumn(); 
            } catch (Exception ignore) {}
            try { 
                userMapper.initUserBalance(); 
            } catch (Exception ignore) {}
            
            int result = userMapper.setUserBalance(username, newBalance);
            session.commit();
            return result > 0;
        }
    }

    private org.apache.ibatis.session.SqlSessionFactory getSessionFactory() throws Exception {
        java.io.InputStream inputStream = org.apache.ibatis.io.Resources.getResourceAsStream("mybatis-config.xml");
        return new org.apache.ibatis.session.SqlSessionFactoryBuilder().build(inputStream);
    }
}