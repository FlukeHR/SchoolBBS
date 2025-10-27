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

@WebServlet("/TradeDelete")
public class TradeDelete extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String idStr = request.getParameter("id");
        if (idStr == null) { response.getWriter().write("{\"ok\":false,\"msg\":\"缺少id\"}"); return; }
        int id = Integer.parseInt(idStr);
        HttpSession session = request.getSession();
        String current = (String) session.getAttribute("username_session");
        if (current == null || current.trim().isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("{\"ok\":false,\"msg\":\"未登录\"}");
            return;
        }
        try {
            TradeService svc = new TradeService();
            Trade t = svc.selectById(id);
            if (t == null) { response.getWriter().write("{\"ok\":false,\"msg\":\"不存在\"}"); return; }
            if (t.getAuthor() != null && !t.getAuthor().equals(current)) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"无权限\"}");
                return;
            }
            svc.deleteTrade(id);
            response.getWriter().write("{\"ok\":true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"失败\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doPost(req, resp);
    }
}
