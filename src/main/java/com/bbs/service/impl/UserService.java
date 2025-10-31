package com.bbs.service.impl;

import com.bbs.entity.User;
import com.bbs.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

public class UserService {
    
    private SqlSessionFactory factory() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    public List<User> selectAll() throws Exception {
        try (SqlSession session = factory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.selectAllUsers();
        }
    }

    public List<User> searchUsers(String keyword) throws Exception {
        try (SqlSession session = factory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> allUsers = mapper.selectAllUsers();
            List<User> result = new ArrayList<>();
            
            if (allUsers != null) {
                for (User user : allUsers) {
                    if (user.getUsername() != null && user.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(user);
                    }
                }
            }
            
            return result;
        }
    }

    public User findUserByName(String name) throws Exception {
        try (SqlSession session = factory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.findUserByName(name);
        }
    }
}