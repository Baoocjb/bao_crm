package com.bao.crm.dao;

import com.bao.crm.base.BaseMapper;
import com.bao.crm.vo.User;

public interface UserMapper extends BaseMapper<User, Integer> {
    public User queryUserByName(String userName);
    public int updatePwdById(Integer userId, String pwd);
}