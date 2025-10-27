package com.bbs.service;

import com.bbs.entity.PostType;
import java.util.List;

public interface PostTypeService {
    
    int createPostType(PostType postType) throws Exception;
    
    int deletePostType(PostType postType) throws Exception;
    
    PostType findPostTypeById(int id) throws Exception;
    
    PostType findPostTypeByName(String name) throws Exception;
    
    List<PostType> findAllPostTypes() throws Exception;
    
    int updatePostType(PostType postType) throws Exception;
}