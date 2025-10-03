package com.bbs.controller;

import com.bbs.pojo.Post;
import com.bbs.service.PostService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/PostWrite")
public class PostWrite extends HttpServlet {
  
  private static final String URL_PREFIX = "/uploads/post"; // 对外URL
  private static final String DISK_DIR = System.getProperty("user.home") + File.separator + "bbs_uploads" + File.separator + "post"; // 磁盘目录
  private static final int MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.setCharacterEncoding("UTF-8");
    
    HttpSession session = request.getSession();
    String author = (String) session.getAttribute("username_session");
    
    String title = null;
    String type = null;
    String content = null;
    List<String> uploadedFiles = new ArrayList<>();
    
    // 检查是否为multipart请求（包含文件上传）
    if (ServletFileUpload.isMultipartContent(request)) {
      // 配置文件上传
      DiskFileItemFactory factory = new DiskFileItemFactory();
      ServletFileUpload upload = new ServletFileUpload(factory);
      upload.setFileSizeMax(MAX_FILE_SIZE);
      upload.setHeaderEncoding("UTF-8");
      
      // 获取上传路径（应用外部，防止重部署丢失）
      String uploadPath = DISK_DIR;
      File uploadDir = new File(uploadPath);
      if (!uploadDir.exists()) {
        uploadDir.mkdirs();
      }
      
      try {
        List<FileItem> formItems = upload.parseRequest(request);
        
        for (FileItem item : formItems) {
          if (item.isFormField()) {
            // 处理普通表单字段
            String fieldName = item.getFieldName();
            String fieldValue = item.getString("UTF-8");
            
            if ("title".equals(fieldName)) {
              title = fieldValue;
            } else if ("ntype".equals(fieldName)) {
              type = fieldValue;
            } else if ("content".equals(fieldName)) {
              content = fieldValue;
            }
          } else {
            // 处理文件上传
            String fileName = item.getName();
            if (fileName != null && !fileName.isEmpty()) {
              String extension = fileName.substring(fileName.lastIndexOf("."));
              String uniqueFileName = UUID.randomUUID().toString() + extension;
              String filePath = uploadPath + File.separator + uniqueFileName;
              
              File storeFile = new File(filePath);
              item.write(storeFile);
              
              uploadedFiles.add(URL_PREFIX + "/" + uniqueFileName);
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        String info = "文件上传失败：" + e.getMessage();
        request.setAttribute("outputMessage", info);
        request.getRequestDispatcher("postErrorInfo.jsp").forward(request, response);
        return;
      }
    } else {
      // 普通表单提交（不包含文件）
      title = request.getParameter("title");
      type = request.getParameter("ntype");
      content = request.getParameter("content");
    }

    Post post = new Post();
    post.setTitle(title);
    post.setAuthor(author);
    post.setPostType(type);
    post.setContent(content);
    
    // 设置媒体文件路径
    if (!uploadedFiles.isEmpty()) {
      post.setMediaFiles(String.join(",", uploadedFiles));
    }
    
    PostService postService = new PostService();
    try {
      postService.createPost(post);
      post = postService.findPostByTitle(title);
      response
          .getWriter()
          .print(
              "<script type=\"text/javascript\">\n"
                  + "window.location.href='post.jsp?id="+post.getId()+"'\n"
                  + "</script>");
    } catch (Exception e) {
      e.printStackTrace();
      String info = "文章录入失败,标题、类型不能为空";
      request.setAttribute("outputMessage",info);
      request.getRequestDispatcher("postErrorInfo.jsp").forward(request, response);
    }
}

@Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException,ServletException {
    doPost(request,response);
  }
}
