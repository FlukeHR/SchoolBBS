package com.bbs.service.impl;

import com.bbs.mapper.PostTypeMapper;
import com.bbs.entity.PostType;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class PostTypeService implements PostTypeMapper {
    
    @Override
    public void createPostType(PostType postType) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostTypeMapper postTypeMapper = sqlSession.getMapper(PostTypeMapper.class);
        postTypeMapper.createPostType(postType);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void deletePostType(int id) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostTypeMapper postTypeMapper = sqlSession.getMapper(PostTypeMapper.class);
        postTypeMapper.deletePostType(id);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public PostType findPostTypeById(int id) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostTypeMapper postTypeMapper = sqlSession.getMapper(PostTypeMapper.class);
        PostType postType = postTypeMapper.findPostTypeById(id);
        sqlSession.close();
        return postType;
    }

    @Override
    public PostType findPostTypeByName(String typeName) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostTypeMapper postTypeMapper = sqlSession.getMapper(PostTypeMapper.class);
        PostType postType = postTypeMapper.findPostTypeByName(typeName);
        sqlSession.close();
        return postType;
    }

    @Override
    public List<PostType> findAllPostTypes() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostTypeMapper postTypeMapper = sqlSession.getMapper(PostTypeMapper.class);
        List<PostType> postTypes = postTypeMapper.findAllPostTypes();
        sqlSession.close();
        return postTypes;
    }

    public List<PostType> findPostTypesByCreator(String creator) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostTypeMapper postTypeMapper = sqlSession.getMapper(PostTypeMapper.class);
        List<PostType> postTypes = postTypeMapper.findPostTypesByCreator(creator);
        sqlSession.close();
        return postTypes;
    }

    public void updatePostTypeName(PostType postType) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostTypeMapper postTypeMapper = sqlSession.getMapper(PostTypeMapper.class);
        postTypeMapper.updatePostTypeName(postType);
        sqlSession.commit();
        sqlSession.close();
    }
}
