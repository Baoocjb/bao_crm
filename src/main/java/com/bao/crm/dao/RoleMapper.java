package com.bao.crm.dao;

import com.bao.crm.base.BaseMapper;
import com.bao.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {

    List<Map<String, Object>> queryAllRoles(Integer userId);
}