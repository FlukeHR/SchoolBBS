package com.bbs.mapper;


import com.bbs.entity.Admin;

public interface adminMapper {
    Admin checkAdmin(String username, String password) throws Exception;
}
