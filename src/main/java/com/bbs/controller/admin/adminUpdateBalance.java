package com.bbs.controller.admin;

import com.bbs.service.impl.RechargeService;
import com.bbs.entity.Recharge;
import com.bbs.mapper.UserMapper;
import com.bbs.mapper.RechargeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@WebServlet("/adminUpdateBalance")
public class adminUpdateBalance extends HttpServlet {
    private RechargeService rechargeService = new RechargeService();

    private SqlSessionFactory factory() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            String username = request.getParameter("username");
            String type = request.getParameter("type");
            String amountStr = request.getParameter("amount");
            String remark = request.getParameter("remark");

            if (username == null || username.trim().isEmpty()) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"用户名不能为空\"}");
                return;
            }

            if (type == null || type.trim().isEmpty()) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"操作类型不能为空\"}");
                return;
            }

            if (amountStr == null || amountStr.trim().isEmpty()) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"金额不能为空\"}");
                return;
            }

            BigDecimal amount;
            try {
                amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    response.getWriter().write("{\"ok\":false,\"msg\":\"金额不能为负数\"}");
                    return;
                }
            } catch (NumberFormatException e) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"金额格式错误\"}");
                return;
            }

            // 获取当前余额
            BigDecimal currentBalance = rechargeService.getUserBalance(username);
            BigDecimal newBalance;

            switch (type) {
                case "ADD":
                    newBalance = currentBalance.add(amount);
                    break;
                case "SUBTRACT":
                    newBalance = currentBalance.subtract(amount);
                    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                        response.getWriter().write("{\"ok\":false,\"msg\":\"余额不足，无法减少指定金额\"}");
                        return;
                    }
                    break;
                case "SET":
                    newBalance = amount;
                    break;
                default:
                    response.getWriter().write("{\"ok\":false,\"msg\":\"不支持的操作类型\"}");
                    return;
            }

            // 执行余额更新
            boolean success = updateUserBalance(username, newBalance, type, amount, remark);
            
            if (success) {
                response.getWriter().write("{\"ok\":true,\"msg\":\"余额修改成功\"}");
            } else {
                response.getWriter().write("{\"ok\":false,\"msg\":\"余额修改失败\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"操作失败：" + e.getMessage() + "\"}");
        }
    }

    private boolean updateUserBalance(String username, BigDecimal newBalance, String operationType, 
                                    BigDecimal operationAmount, String remark) throws Exception {
        try (SqlSession session = factory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            // 确保用户表有余额字段
            try { 
                userMapper.addBalanceColumn(); 
            } catch (Exception ignore) {}
            try { 
                userMapper.initUserBalance(); 
            } catch (Exception ignore) {}
            
            // 设置新余额
            int result = userMapper.setUserBalance(username, newBalance);
            
            if (result > 0) {
                // 记录操作日志（创建一条充值记录作为管理员操作的记录）
                logAdminOperation(username, operationType, operationAmount, remark);
                session.commit();
                return true;
            }
            
            return false;
        }
    }

    private void logAdminOperation(String username, String operationType, BigDecimal amount, String remark) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = sdf.format(new Timestamp(System.currentTimeMillis()));
            int random = (int) (Math.random() * 10000);
            String orderNo = "ADMIN" + timestamp + String.format("%04d", random);

            Recharge adminLog = new Recharge();
            adminLog.setUsername(username);
            
            // 根据操作类型设置金额（减少操作记录为负数）
            if ("SUBTRACT".equals(operationType)) {
                adminLog.setAmount(amount.negate());
            } else {
                adminLog.setAmount(amount);
            }
            
            adminLog.setPaymentMethod("ADMIN");
            adminLog.setStatus("SUCCESS");
            adminLog.setOrderNo(orderNo);
            adminLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
            
            String finalRemark = "管理员操作-" + getOperationName(operationType);
            if (remark != null && !remark.trim().isEmpty()) {
                finalRemark += "：" + remark;
            }
            adminLog.setRemark(finalRemark);

            // 保存操作记录
            try (SqlSession session = factory().openSession()) {
                RechargeMapper rechargeMapper = session.getMapper(RechargeMapper.class);
                rechargeMapper.initSchema();
                rechargeMapper.insertRecharge(adminLog);
                session.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOperationName(String operationType) {
        switch (operationType) {
            case "ADD": return "增加余额";
            case "SUBTRACT": return "减少余额";
            case "SET": return "设置余额";
            default: return "未知操作";
        }
    }
}