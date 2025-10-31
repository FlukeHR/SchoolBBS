package com.bbs.mapper;

import com.bbs.entity.Recharge;
import java.util.List;

public interface RechargeMapper {
    void initSchema() throws Exception;
    void insertRecharge(Recharge recharge) throws Exception;
    void updateRechargeStatus(String orderNo, String status) throws Exception;
    Recharge selectByOrderNo(String orderNo) throws Exception;
    List<Recharge> selectByUsername(String username) throws Exception;
    List<Recharge> selectAllRecharges() throws Exception;
}