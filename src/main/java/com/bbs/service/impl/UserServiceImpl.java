package com.bbs.service.impl;

import com.bbs.config.DatabaseConfig;
import com.bbs.entity.User;
import com.bbs.exception.BusinessException;
import com.bbs.mapper.UserMapper;
import com.bbs.service.UserService;
import com.bbs.util.StringUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Override
    public int insertUser(User user) throws Exception {
        if (user == null) {
            throw new BusinessException("用户信息不能为空");
        }
        
        if (StringUtil.isEmpty(user.getUsername())) {
            throw new BusinessException("用户名不能为空");
        }
        
        if (StringUtil.isEmpty(user.getPassword())) {
            throw new BusinessException("密码不能为空");
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            
            // 检查用户名是否已存在
            User existingUser = userMapper.findUserByName(user.getUsername());
            if (existingUser != null) {
                throw new BusinessException("用户名已存在");
            }
            
            int result = userMapper.insertUser(user);
            sqlSession.commit();
            logger.info("User created successfully: {}", user.getUsername());
            return result;
        } catch (Exception e) {
            logger.error("Failed to create user: {}", user.getUsername(), e);
            if (e instanceof BusinessException) {
                throw e;
            }
            throw new BusinessException("创建用户失败");
        }
    }
    
    @Override
    public int deleteUser(User user) throws Exception {
        if (user == null || user.getUid() <= 0) {
            throw new BusinessException("用户信息无效");
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            int result = userMapper.deleteUser(user);
            sqlSession.commit();
            logger.info("User removed successfully, id: {}", user.getUid());
            return result;
        } catch (Exception e) {
            logger.error("Failed to remove user, id: {}", user.getUid(), e);
            throw new BusinessException("删除用户失败");
        }
    }
    
    @Override
    public User selectUserById(int id) throws Exception {
        if (id <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.selectUserById(id);
        } catch (Exception e) {
            logger.error("Failed to find user by id: {}", id, e);
            throw new BusinessException("查询用户失败");
        }
    }
    
    @Override
    public User findUserByName(String name) throws Exception {
        if (StringUtil.isEmpty(name)) {
            throw new BusinessException("用户名不能为空");
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.findUserByName(name);
        } catch (Exception e) {
            logger.error("Failed to find user by name: {}", name, e);
            throw new BusinessException("查询用户失败");
        }
    }
    
    @Override
    public List<User> selectAllUsers() throws Exception {
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.selectAllUsers();
        } catch (Exception e) {
            logger.error("Failed to find all users", e);
            throw new BusinessException("查询用户列表失败");
        }
    }
    
    @Override
    public int updateUser(User user) throws Exception {
        if (user == null || user.getUid() <= 0) {
            throw new BusinessException("用户信息无效");
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            int result = userMapper.updateUser(user);
            sqlSession.commit();
            logger.info("User updated successfully: {}", user.getUsername());
            return result;
        } catch (Exception e) {
            logger.error("Failed to update user: {}", user.getUsername(), e);
            throw new BusinessException("更新用户失败");
        }
    }
    
    @Override
    public User check(String username, String password) throws Exception {
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            throw new BusinessException("用户名和密码不能为空");
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.check(username, password);
        } catch (Exception e) {
            logger.error("Failed to validate user: {}", username, e);
            throw new BusinessException("用户验证失败");
        }
    }
    
    @Override
    public boolean checkName(String name) throws Exception {
        if (StringUtil.isEmpty(name)) {
            return false;
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.checkName(name);
        } catch (Exception e) {
            logger.error("Failed to check username: {}", name, e);
            return false;
        }
    }
    
    @Override
    public String findSexById(int id) throws Exception {
        if (id <= 0) {
            throw new BusinessException("用户ID无效");
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.findSexById(id);
        } catch (Exception e) {
            logger.error("Failed to find sex by id: {}", id, e);
            throw new BusinessException("查询用户性别失败");
        }
    }
    
    @Override
    public int findIdByName(String name) throws Exception {
        if (StringUtil.isEmpty(name)) {
            throw new BusinessException("用户名不能为空");
        }
        
        try (SqlSession sqlSession = DatabaseConfig.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.findIdByName(name);
        } catch (Exception e) {
            logger.error("Failed to find id by name: {}", name, e);
            throw new BusinessException("查询用户ID失败");
        }
    }
}