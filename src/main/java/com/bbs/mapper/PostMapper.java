package com.bbs.mapper;

import com.bbs.entity.Post;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper {
    void createPost(Post post) throws Exception;
    void removePost(int id) throws Exception ;
    Post findPostById(int id) throws Exception;
    Post findPostByTitle(String Title) throws Exception;
    List<Post> searchPosts(String keyword) throws Exception;
    List<Post> findPostByAuthor (String author) throws Exception;
    List<Post> findPostByType (String type) throws Exception;
    List<Post> findAllPost() throws Exception;
    void updatePost(Post post) throws Exception;
    List<Post> findAllPostByType (String type) throws Exception;
    // 新增：按类型+关键词搜索（分页）
    List<Post> searchPostsByTypePaged(@Param("type") String type,
                                      @Param("keyword") String keyword,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit,
                                      @Param("sort") String sort) throws Exception;
    int countPostsByTypeKeyword(@Param("type") String type,
                                @Param("keyword") String keyword) throws Exception;
}
