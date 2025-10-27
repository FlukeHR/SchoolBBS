package com.bbs.mapper;

import com.bbs.entity.PostType;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface FollowMapper {
    Integer isFollowed(@Param("username") String username, @Param("typeId") int typeId) throws Exception;
    void follow(@Param("username") String username, @Param("typeId") int typeId) throws Exception;
    void unfollow(@Param("username") String username, @Param("typeId") int typeId) throws Exception;
    List<PostType> findFollowedTypes(@Param("username") String username) throws Exception;
}
