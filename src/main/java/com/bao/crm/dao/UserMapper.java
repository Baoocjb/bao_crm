package com.bao.crm.dao;

import com.bao.crm.base.BaseMapper;
import com.bao.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User, Integer> {
    public User queryUserByName(String userName);
    public int updatePwdById(Integer userId, String pwd);
    public List<Map<String, Object>> queryAllSales();
}