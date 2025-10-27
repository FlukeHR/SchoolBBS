package com.bbs.service.impl;

import com.bbs.mapper.CommentLikeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class LikeCommentService {
    private SqlSession openSession() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        return factory.openSession();
    }

    public boolean hasLiked(String username, int commentId) throws Exception {
        try (SqlSession session = openSession()) {
            CommentLikeMapper mapper = session.getMapper(CommentLikeMapper.class);
            Integer cnt = mapper.hasLiked(username, commentId);
            return cnt != null && cnt > 0;
        }
    }

    public void like(String username, int commentId) throws Exception {
        try (SqlSession session = openSession()) {
            CommentLikeMapper mapper = session.getMapper(CommentLikeMapper.class);
            mapper.like(username, commentId);
            session.commit();
        }
    }

    public void unlike(String username, int commentId) throws Exception {
        try (SqlSession session = openSession()) {
            CommentLikeMapper mapper = session.getMapper(CommentLikeMapper.class);
            mapper.unlike(username, commentId);
            session.commit();
        }
    }

    public int countLikes(int commentId) throws Exception {
        try (SqlSession session = openSession()) {
            CommentLikeMapper mapper = session.getMapper(CommentLikeMapper.class);
            Integer cnt = mapper.countLikes(commentId);
            return cnt == null ? 0 : cnt;
        }
    }
}
