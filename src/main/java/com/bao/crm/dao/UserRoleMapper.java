package com.bao.crm.dao;

import com.bao.crm.base.BaseMapper;
import com.bao.crm.vo.User;
import com.bao.crm.vo.UserRole;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole, Integer> {

    public Integer countUserRelationRole(Integer userId);

    public Integer delectUserRelationRole(Integer userId);

}