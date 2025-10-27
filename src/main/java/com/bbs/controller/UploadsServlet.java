package com.bbs.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

@WebServlet(urlPatterns = "/uploads/*")
public class UploadsServlet extends HttpServlet {
    private File baseDir;

    @Override
    public void init() throws ServletException {
        // 与 TradeCreate 使用同一物理目录：~/bbs_uploads
        String root = System.getProperty("user.home") + File.separator + "bbs_uploads";
        baseDir = new File(root);
        if (!baseDir.exists()) baseDir.mkdirs();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 请求路径形如 /uploads/trade/xxxxx.jpg
        String pathInfo = req.getPathInfo(); // /trade/xxxxx.jpg
        if (pathInfo == null || pathInfo.contains("..")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        File target = new File(baseDir, pathInfo);
        if (!target.exists() || target.isDirectory()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String mime = URLConnection.guessContentTypeFromName(target.getName());
        if (mime == null) mime = "application/octet-stream";
        resp.setContentType(mime);
        // 允许直接在浏览器查看图片；若需要下载可在链接上加 download 属性
        resp.setHeader("Cache-Control", "public, max-age=604800"); // 7天缓存
        resp.setContentLengthLong(target.length());
        try (InputStream in = new BufferedInputStream(new FileInputStream(target));
             OutputStream out = new BufferedOutputStream(resp.getOutputStream())) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
