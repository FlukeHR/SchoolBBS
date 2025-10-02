package com.bbs.mapper;

import com.bbs.pojo.PostType;
import java.util.List;

public interface PostTypeMapper {
    void createPostType(PostType postType) throws Exception;
    void deletePostType(int id) throws Exception;
    PostType findPostTypeById(int id) throws Exception;
    PostType findPostTypeByName(String typeName) throws Exception;
    List<PostType> findAllPostTypes() throws Exception;
    List<PostType> findPostTypesByCreator(String creator) throws Exception;
    void updatePostTypeName(PostType postType) throws Exception;
}
