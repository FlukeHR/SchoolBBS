package com.bbs.controller.trade;

import com.bbs.entity.Trade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/TradeList")
public class TradeList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try {
            List<Trade> list = new com.bbs.service.impl.TradeService().listTrades();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < list.size(); i++) {
                Trade t = list.get(i);
                if (i > 0) sb.append(',');
                sb.append('{')
                  .append("\"id\":").append(t.getId()).append(',')
                  .append("\"title\":\"").append(escape(t.getTitle())).append("\",")
                  .append("\"price\":").append(t.getPrice() == null ? 0 : t.getPrice()).append(',')
                  .append("\"description\":\"").append(escape(t.getDescription())).append("\",")
                  .append("\"img\":\"").append(escape(t.getImg())).append("\",")
                  .append("\"images\":\"").append(escape(t.getImages())).append("\",")
                  .append("\"author\":\"").append(escape(t.getAuthor())).append("\",")
                  .append("\"date\":\"").append(t.getDate() == null ? "" : t.getDate().toString()).append("\"")
                  .append('}');
            }
            sb.append("]");
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("[]");
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
