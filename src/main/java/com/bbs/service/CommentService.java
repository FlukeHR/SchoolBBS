package com.bbs.service;

import com.bbs.entity.Comment;
import java.util.List;

public interface CommentService {
    
    void createComment(Comment comment) throws Exception;
    
    void removeCommentById(int id) throws Exception;
    
    Comment findCommentById(int id) throws Exception;
    
    List<Comment> findCommentByAuthor(String author) throws Exception;
    
    List<Comment> findCommentsByPostId(int id) throws Exception;
    
    List<Comment> findCommentAll() throws Exception;
    
    void updateComment(Comment comment) throws Exception;
    
    int findPostIdByCommentId(int id) throws Exception;
}