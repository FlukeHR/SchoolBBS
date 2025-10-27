package com.bbs.controller.post;

import com.bbs.entity.PostType;
import com.bbs.service.impl.PostTypeService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/GetPostTypes")
public class GetPostTypes extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        PostTypeService postTypeService = new PostTypeService();
        try {
            List<PostType> postTypes = postTypeService.findAllPostTypes();
            Gson gson = new Gson();
            String json = gson.toJson(postTypes);
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("[]");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
