package com.bbs.controller.admin;

import com.bbs.service.impl.TradeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/adminDeleteTrade")
public class adminDeleteTrade extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String redirect = request.getParameter("redirectPage");
        
        if (id == null || id.isEmpty()) {
            String info = "参数缺失，请检查输入";
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
            return;
        }
        
        try {
            int tradeId = Integer.parseInt(id);
            TradeService tradeService = new TradeService();
            tradeService.deleteTrade(tradeId);
            
            // 重定向回管理页面
            response.sendRedirect(redirect);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            String info = "商品ID格式错误";
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
            String info = "删除商品失败: " + e.getMessage();
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}