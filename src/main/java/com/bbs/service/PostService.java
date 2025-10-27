package com.bbs.service;

import com.bbs.entity.Post;
import java.util.List;

public interface PostService {
    
    void createPost(Post post) throws Exception;
    
    Post findPostById(int id) throws Exception;
    
    Post findPostByTitle(String title) throws Exception;
    
    List<Post> findPostByAuthor(String author) throws Exception;
    
    List<Post> findPostAll() throws Exception;
    
    List<Post> findPostByType(int type) throws Exception;
    
    void updatePost(Post post) throws Exception;
    
    void removePostById(int id) throws Exception;
    
    List<Post> searchPostByTitle(String title) throws Exception;
    
    List<Post> findPostWithPage(int offset, int pageSize) throws Exception;
    
    List<Post> findPostByTypeWithPage(int type, int offset, int pageSize) throws Exception;
}