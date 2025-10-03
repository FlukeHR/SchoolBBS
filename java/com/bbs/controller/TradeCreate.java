package com.bbs.controller;

import com.bbs.pojo.Trade;
import com.bbs.service.TradeService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/TradeCreate")
public class TradeCreate extends HttpServlet {
    private static final String TRADE_UPLOAD_URL_PREFIX = "/uploads/trade"; // 对外可访问的URL前缀
    private static final String TRADE_UPLOAD_DIR = System.getProperty("user.home") + File.separator + "bbs_uploads" + File.separator + "trade"; // 磁盘持久目录
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 单图限制 10MB

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

        String title = null;
        String priceStr = null;
        String desc = null;
        StringBuilder images = new StringBuilder();

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            upload.setFileSizeMax(MAX_FILE_SIZE);

            String uploadRoot = TRADE_UPLOAD_DIR;
            File uploadDir = new File(uploadRoot);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            try {
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        String value = item.getString("UTF-8");
                        if ("title".equals(name)) title = value;
                        else if ("price".equals(name)) priceStr = value;
                        else if ("desc".equals(name)) desc = value;
                    } else {
                        String fileName = item.getName();
                        if (fileName != null && !fileName.isEmpty()) {
                            String contentType = item.getContentType();
                            if (contentType == null || !contentType.startsWith("image/")) {
                                continue; // 仅接受图片
                            }
                            if (item.getSize() > MAX_FILE_SIZE) {
                                continue; // 超过大小直接跳过
                            }
                            String ext = fileName.lastIndexOf('.') > -1 ? fileName.substring(fileName.lastIndexOf('.')) : "";
                            String unique = UUID.randomUUID().toString() + ext;
                            File store = new File(uploadRoot, unique);
                            item.write(store);
                            String p = TRADE_UPLOAD_URL_PREFIX + "/" + unique;
                            if (images.length() > 0) images.append(',');
                            images.append(p);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("{\"ok\":false,\"msg\":\"上传失败\"}");
                return;
            }
        } else {
            title = request.getParameter("title");
            priceStr = request.getParameter("price");
            desc = request.getParameter("desc");
        }

        Double price = 0.0;
        try { if (priceStr != null && priceStr.trim().length() > 0) price = Double.parseDouble(priceStr); } catch (Exception ignore) {}

        if (title == null || title.trim().isEmpty() || desc == null || desc.trim().isEmpty()) {
            response.getWriter().write("{\"ok\":false,\"msg\":\"标题和描述不能为空\"}");
            return;
        }

        Trade t = new Trade();
        // 基本XSS清理
        title = title.replace("<"," ").replace(">"," ");
        desc = desc.replace("<"," ").replace(">"," ");
        t.setTitle(title.trim());
        t.setPrice(price);
        t.setDescription(desc.trim());
        if (images.length() > 0) {
            t.setImages(images.toString());
        }
        t.setAuthor(author);

        try {
            new TradeService().createTrade(t);
            response.getWriter().write("{\"ok\":true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"保存失败\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doPost(req, resp);
    }
}
