package com.bbs.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static SqlSessionFactory sqlSessionFactory;
    
    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            logger.info("MyBatis SqlSessionFactory initialized successfully");
        } catch (IOException e) {
            logger.error("Failed to initialize MyBatis SqlSessionFactory", e);
            throw new RuntimeException("Failed to initialize database configuration", e);
        }
    }
    
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
    
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
    
    public static SqlSession getSqlSession(boolean autoCommit) {
        return sqlSessionFactory.openSession(autoCommit);
    }
}