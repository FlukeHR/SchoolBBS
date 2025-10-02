package com.bbs.service;

import com.bbs.mapper.UserMapper;
import com.bbs.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class UserService implements UserMapper {
    //加入用户的方法
    public int insertUser(User user) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.insertUser(user);
        sqlSession.commit();
        return 1;
    }
    //删除用户的方法
    public int deleteUser(User user) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.deleteUser(user);
        sqlSession.commit();
        return 1;
    }
    // 通过id选择用户
    public User selectUserById(int id) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        sqlSession.commit();
        return userMapper.selectUserById(id);
    }
    // 通过name查找用户
    public User findUserByName(String name) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        sqlSession.commit();
        return userMapper.findUserByName(name);
    }
    // 选择所有用户
    public List<User> selectAllUsers() throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        sqlSession.commit();
        return userMapper.selectAllUsers();
    }
    // 更新用户
    public int updateUser(User user) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.updateUser(user);
        sqlSession.commit();
        return 1;
    }
    // 检查用户
    public User check(String username, String password) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.check(username, password);
        sqlSession.commit();
        return user;
    }
    // 检查用户名
    public boolean checkName(String name) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        sqlSession.commit();
        return userMapper.checkName(name);
    }
    // 通过id查找性别
    public String findSexById(int id) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        sqlSession.commit();
        return userMapper.findSexById(id);
    }
    // 通过name查找id
    public int findIdByName(String name) throws Exception {
        //读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        //创建会话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //准备执行sql语句
        //创建userMapper对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        sqlSession.commit();
        return userMapper.findIdByName(name);
    }

}
