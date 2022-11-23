package com.bao.crm.dao;

import com.bao.crm.base.BaseMapper;
import com.bao.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission, Integer> {
    // 查询对应角色id的授权数量
    Integer countPermissionByRoleId(Integer roleId);
    // 删除对应角色的所有授权
    Integer deletePermissionByRoleId(Integer roleId);
    // 批量插入授权
    Integer insertPermissions(List<Permission> permissionList);

    List<Integer> queryHasModuleIdByRoleId(Integer roleId);
}