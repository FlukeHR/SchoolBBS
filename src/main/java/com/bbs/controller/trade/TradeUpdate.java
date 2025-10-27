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

@WebServlet("/TradeUpdate")
public class TradeUpdate extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        String author = (String) session.getAttribute("username_session");
        if (author == null || author.trim().isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("{\"ok\":false,\"msg\":\"未登录\"}");
            return;
        }

        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String priceStr = request.getParameter("price");
        String desc = request.getParameter("desc");
        String images = request.getParameter("images");

        if (idStr == null) { response.getWriter().write("{\"ok\":false,\"msg\":\"缺少id\"}"); return; }
        int id = Integer.parseInt(idStr);

        try {
            TradeService svc = new TradeService();
            Trade old = svc.selectById(id);
            if (old == null) { response.getWriter().write("{\"ok\":false,\"msg\":\"不存在\"}"); return; }
            if (old.getAuthor() != null && !old.getAuthor().equals(author)) {
                response.getWriter().write("{\"ok\":false,\"msg\":\"无权限\"}"); return;
            }

            Trade t = new Trade();
            t.setId(id);
            t.setTitle(title != null ? title.replace("<"," ").replace(">"," ") : old.getTitle());
            try { t.setPrice(priceStr != null ? Double.parseDouble(priceStr) : old.getPrice()); } catch (Exception e) { t.setPrice(old.getPrice()); }
            t.setDescription(desc != null ? desc.replace("<"," ").replace(">"," ") : old.getDescription());
            t.setImages(images != null ? images : old.getImages());

            svc.updateTrade(t);
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
