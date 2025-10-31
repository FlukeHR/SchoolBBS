package com.bbs.mapper;

import com.bbs.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserMapper {
    int insertUser(User user) throws Exception;
    int deleteUser(User user) throws Exception ;
    User selectUserById(int id) throws Exception;
    User findUserByName(String name) throws Exception;
    List<User> selectAllUsers() throws Exception;
    int updateUser(User user) throws Exception;
    User check(String username, String password) throws Exception;
    boolean checkName(String name) throws Exception;
    String findSexById(int id) throws Exception;
    int findIdByName(String name) throws Exception;
    
    // 余额相关方法
    void addBalanceColumn() throws Exception;
    void initUserBalance() throws Exception;
    BigDecimal getUserBalance(String username) throws Exception;
    void updateUserBalance(String username, BigDecimal balance) throws Exception;
    void addUserBalance(String username, BigDecimal amount) throws Exception;
    int setUserBalance(String username, BigDecimal balance) throws Exception;
    int batchUpdateAllUsersBalance(BigDecimal balance) throws Exception;
}
