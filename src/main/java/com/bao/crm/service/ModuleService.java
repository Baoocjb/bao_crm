package com.bao.crm.service;

import com.bao.crm.base.BaseService;
import com.bao.crm.dao.ModuleMapper;
import com.bao.crm.dao.PermissionMapper;
import com.bao.crm.model.TreeModel;
import com.bao.crm.utils.AssertUtil;
import com.bao.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeModel> queryAllModules(Integer roleId) {
        List<TreeModel> treeModels = moduleMapper.queryAllModules();
        AssertUtil.isTrue(roleId == null, "待授权角色不存在!");
        // 通过角色查询该角色已经拥有的资源id
        List<Integer> moduleIds = permissionMapper.queryHasModuleIdByRoleId(roleId);
        if(moduleIds != null && moduleIds.size() > 0){
            treeModels.forEach((treeModel)->{
                if (moduleIds.contains(treeModel.getId())){
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModels;
    }
}
