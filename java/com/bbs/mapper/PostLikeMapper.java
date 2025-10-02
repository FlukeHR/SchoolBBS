package com.bbs.mapper;

import org.apache.ibatis.annotations.Param;

public interface PostLikeMapper {
    Integer hasLiked(@Param("username") String username, @Param("postId") int postId) throws Exception;
    void like(@Param("username") String username, @Param("postId") int postId) throws Exception;
    void unlike(@Param("username") String username, @Param("postId") int postId) throws Exception;
    Integer countLikes(@Param("postId") int postId) throws Exception;
}
