package com.bao.crm.service;

import com.bao.crm.base.BaseService;
import com.bao.crm.dao.PermissionMapper;
import com.bao.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PermissionService extends BaseService<Permission, Integer> {

    @Resource
    private PermissionMapper permissionMapper;
}
