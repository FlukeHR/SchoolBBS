package com.bbs.controller;

import com.bbs.pojo.Trade;
import com.bbs.service.TradeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/TradeGet")
public class TradeGet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String idStr = request.getParameter("id");
        if (idStr == null) { response.getWriter().write("{\"ok\":false,\"msg\":\"缺少id\"}"); return; }
        try {
            int id = Integer.parseInt(idStr);
            TradeService svc = new TradeService();
            Trade t = svc.selectById(id);
            if (t == null) { response.getWriter().write("{\"ok\":false,\"msg\":\"不存在\"}"); return; }
            StringBuilder sb = new StringBuilder();
            sb.append("{\"ok\":true,\"data\":{");
            sb.append("\"id\":"+t.getId()+",");
            sb.append("\"title\":\"").append(escape(t.getTitle())).append("\",");
            sb.append("\"price\":"+(t.getPrice()==null?0:t.getPrice())+",");
            sb.append("\"description\":\"").append(escape(t.getDescription()==null?"":t.getDescription())).append("\",");
            sb.append("\"images\":\"").append(escape(t.getImages()==null?"":t.getImages())).append("\",");
            sb.append("\"author\":\"").append(escape(t.getAuthor()==null?"":t.getAuthor())).append("\",");
            sb.append("\"date\":\"").append(escape(String.valueOf(t.getDate()))).append("\"");
            sb.append("}}\n");
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"失败\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }

    private String escape(String s){
        if (s==null) return "";
        return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n"," ").replace("\r"," ");
    }
}
