package com.bbs.controller.admin;

import com.bbs.mapper.UserMapper;
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

@WebServlet("/adminBatchUpdateBalance")
public class adminBatchUpdateBalance extends HttpServlet {

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
            String amountStr = request.getParameter("amount");
            
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

            // 批量更新所有用户余额
            int updatedCount = batchUpdateAllUsersBalance(amount);
            
            if (updatedCount > 0) {
                response.getWriter().write("{\"ok\":true,\"msg\":\"成功更新了 " + updatedCount + " 个用户的余额\"}");
            } else {
                response.getWriter().write("{\"ok\":false,\"msg\":\"没有用户余额被更新\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"操作失败：" + e.getMessage() + "\"}");
        }
    }

    private int batchUpdateAllUsersBalance(BigDecimal amount) throws Exception {
        try (SqlSession session = factory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            // 确保用户表有余额字段
            try { 
                userMapper.addBalanceColumn(); 
            } catch (Exception ignore) {}
            try { 
                userMapper.initUserBalance(); 
            } catch (Exception ignore) {}
            
            // 批量更新所有用户余额
            int result = userMapper.batchUpdateAllUsersBalance(amount);
            session.commit();
            
            return result;
        }
    }
}