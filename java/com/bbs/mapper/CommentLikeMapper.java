package com.bbs.mapper;

import org.apache.ibatis.annotations.Param;

public interface CommentLikeMapper {
    Integer hasLiked(@Param("username") String username, @Param("commentId") int commentId) throws Exception;
    void like(@Param("username") String username, @Param("commentId") int commentId) throws Exception;
    void unlike(@Param("username") String username, @Param("commentId") int commentId) throws Exception;
    Integer countLikes(@Param("commentId") int commentId) throws Exception;
}
