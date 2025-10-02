package com.bbs.service;

import com.bbs.mapper.PostLikeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class LikePostService {
    private SqlSession openSession() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        return factory.openSession();
    }

    public boolean hasLiked(String username, int postId) throws Exception {
        try (SqlSession session = openSession()) {
            PostLikeMapper mapper = session.getMapper(PostLikeMapper.class);
            Integer cnt = mapper.hasLiked(username, postId);
            return cnt != null && cnt > 0;
        }
    }

    public void like(String username, int postId) throws Exception {
        try (SqlSession session = openSession()) {
            PostLikeMapper mapper = session.getMapper(PostLikeMapper.class);
            mapper.like(username, postId);
            session.commit();
        }
    }

    public void unlike(String username, int postId) throws Exception {
        try (SqlSession session = openSession()) {
            PostLikeMapper mapper = session.getMapper(PostLikeMapper.class);
            mapper.unlike(username, postId);
            session.commit();
        }
    }

    public int countLikes(int postId) throws Exception {
        try (SqlSession session = openSession()) {
            PostLikeMapper mapper = session.getMapper(PostLikeMapper.class);
            Integer cnt = mapper.countLikes(postId);
            return cnt == null ? 0 : cnt;
        }
    }
}
