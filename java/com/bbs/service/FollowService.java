package com.bbs.service;

import com.bbs.mapper.FollowMapper;
import com.bbs.pojo.PostType;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class FollowService {

    private SqlSession openSession() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        return factory.openSession();
    }

    public boolean isFollowed(String username, int typeId) throws Exception {
        try (SqlSession session = openSession()) {
            FollowMapper mapper = session.getMapper(FollowMapper.class);
            Integer cnt = mapper.isFollowed(username, typeId);
            return cnt != null && cnt > 0;
        }
    }

    public void follow(String username, int typeId) throws Exception {
        try (SqlSession session = openSession()) {
            FollowMapper mapper = session.getMapper(FollowMapper.class);
            mapper.follow(username, typeId);
            session.commit();
        }
    }

    public void unfollow(String username, int typeId) throws Exception {
        try (SqlSession session = openSession()) {
            FollowMapper mapper = session.getMapper(FollowMapper.class);
            mapper.unfollow(username, typeId);
            session.commit();
        }
    }

    public List<PostType> findFollowedTypes(String username) throws Exception {
        try (SqlSession session = openSession()) {
            FollowMapper mapper = session.getMapper(FollowMapper.class);
            return mapper.findFollowedTypes(username);
        }
    }
}
