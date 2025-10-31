package com.bbs.service.impl;

import com.bbs.entity.Recharge;
import com.bbs.mapper.RechargeMapper;
import com.bbs.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class RechargeService {
    
    private SqlSessionFactory factory() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    // 生成订单号
    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Timestamp(System.currentTimeMillis()));
        int random = (int) (Math.random() * 10000);
        return "RC" + timestamp + String.format("%04d", random);
    }

    // 创建充值订单
    public String createRechargeOrder(String username, BigDecimal amount, String paymentMethod) throws Exception {
        try (SqlSession session = factory().openSession()) {
            RechargeMapper mapper = session.getMapper(RechargeMapper.class);
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            // 初始化表结构
            mapper.initSchema();
            try { userMapper.addBalanceColumn(); } catch (Exception ignore) {}
            try { userMapper.initUserBalance(); } catch (Exception ignore) {}
            
            Recharge recharge = new Recharge();
            recharge.setUsername(username);
            recharge.setAmount(amount);
            recharge.setPaymentMethod(paymentMethod);
            recharge.setStatus("PENDING");
            recharge.setOrderNo(generateOrderNo());
            recharge.setCreateTime(new Timestamp(System.currentTimeMillis()));
            recharge.setRemark("用户充值");
            
            mapper.insertRecharge(recharge);
            session.commit();
            
            return recharge.getOrderNo();
        }
    }

    // 完成充值（模拟支付成功）
    public boolean completeRecharge(String orderNo) throws Exception {
        try (SqlSession session = factory().openSession()) {
            RechargeMapper mapper = session.getMapper(RechargeMapper.class);
            UserMapper userMapper = session.getMapper(UserMapper.class);
            
            // 查询充值记录
            Recharge recharge = mapper.selectByOrderNo(orderNo);
            if (recharge == null || !"PENDING".equals(recharge.getStatus())) {
                return false;
            }
            
            // 更新充值状态
            mapper.updateRechargeStatus(orderNo, "SUCCESS");
            
            // 增加用户余额
            userMapper.addUserBalance(recharge.getUsername(), recharge.getAmount());
            
            session.commit();
            return true;
        }
    }

    // 查询用户充值记录
    public List<Recharge> getUserRechargeHistory(String username) throws Exception {
        try (SqlSession session = factory().openSession()) {
            RechargeMapper mapper = session.getMapper(RechargeMapper.class);
            mapper.initSchema();
            return mapper.selectByUsername(username);
        }
    }

    // 根据订单号查询充值记录
    public Recharge getRechargeByOrderNo(String orderNo) throws Exception {
        try (SqlSession session = factory().openSession()) {
            RechargeMapper mapper = session.getMapper(RechargeMapper.class);
            mapper.initSchema();
            return mapper.selectByOrderNo(orderNo);
        }
    }

    // 查询用户余额
    public BigDecimal getUserBalance(String username) throws Exception {
        try (SqlSession session = factory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            try { userMapper.addBalanceColumn(); } catch (Exception ignore) {}
            try { userMapper.initUserBalance(); } catch (Exception ignore) {}
            
            BigDecimal balance = userMapper.getUserBalance(username);
            return balance != null ? balance : BigDecimal.ZERO;
        }
    }
}