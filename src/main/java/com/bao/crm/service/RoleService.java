package com.bao.crm.service;

import com.bao.crm.base.BaseService;
import com.bao.crm.dao.ModuleMapper;
import com.bao.crm.dao.PermissionMapper;
import com.bao.crm.dao.RoleMapper;
import com.bao.crm.utils.AssertUtil;
import com.bao.crm.vo.Permission;
import com.bao.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role, Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        AssertUtil.isTrue(role == null, "添加记录为空!");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空!");
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1, "添加角色失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        AssertUtil.isTrue(role == null, "待添加记录为空!");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空!");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1, "修改角色失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId) {
        AssertUtil.isTrue(roleId == null, "待删除记录不存在!");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role == null, "待删除记录不存在!");
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1, "删除角色失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer[] mIds, Integer roleId) {
        AssertUtil.isTrue(roleId == null, "待授权记录不存在!");
        // 判断当前角色含有的权限数量
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        if(count > 0){
            // 删除所有权限
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId) != count, "角色授权失败!");
        }
        // 新增权限
        if(mIds == null || mIds.length == 0)return;
        List<Permission> permissionList = new ArrayList<>();
        for (int mId : mIds){
            Permission permission = new Permission();
            permission.setRoleId(roleId);
            permission.setModuleId(mId);
            permission.setCreateDate(new Date());
            permission.setUpdateDate(new Date());
            permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
            permissionList.add(permission);
        }
        Integer integer = permissionMapper.insertPermissions(permissionList);
        AssertUtil.isTrue(integer != permissionList.size(), "角色授权失败!");
    }
}
