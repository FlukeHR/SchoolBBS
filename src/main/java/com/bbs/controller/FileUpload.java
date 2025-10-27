package com.bbs.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/FileUpload")
public class FileUpload extends HttpServlet {
    
    private static final String UPLOAD_DIRECTORY = "uploads";
    private static final int MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final int MAX_REQUEST_SIZE = 100 * 1024 * 1024; // 100MB
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        // 检查是否为multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().write("{\"success\":false,\"message\":\"请求格式错误\"}");
            return;
        }
        
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024); // 1MB
        
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        upload.setHeaderEncoding("UTF-8");
        
        // 获取上传路径
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        List<String> uploadedFiles = new ArrayList<>();
        
        try {
            List<FileItem> formItems = upload.parseRequest(request);
            
            for (FileItem item : formItems) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    if (fileName != null && !fileName.isEmpty()) {
                        // 获取文件扩展名
                        String extension = fileName.substring(fileName.lastIndexOf("."));
                        // 生成唯一文件名
                        String uniqueFileName = UUID.randomUUID().toString() + extension;
                        String filePath = uploadPath + File.separator + uniqueFileName;
                        
                        // 保存文件
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        
                        // 返回相对路径
                        uploadedFiles.add(UPLOAD_DIRECTORY + "/" + uniqueFileName);
                    }
                }
            }
            
            // 返回成功响应
            String filesJson = String.join(",", uploadedFiles);
            response.getWriter().write("{\"success\":true,\"files\":\"" + filesJson + "\"}");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().write("{\"success\":false,\"message\":\"请使用POST方法上传文件\"}");
    }
}
