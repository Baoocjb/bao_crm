package com.bao.crm.controller;

import com.bao.crm.base.BaseController;
import com.bao.crm.base.ResultInfo;
import com.bao.crm.query.RoleQuery;
import com.bao.crm.service.RoleService;
import com.bao.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    @PostMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    @GetMapping("index")
    public String index(){
        return "role/role";
    }

    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> queryByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success();
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success();
    }

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId){
        roleService.deleteRole(roleId);
        return success();
    }

    @GetMapping("toAddOrUpdateRolePage")
    public String toAddOrUpdateRolePage(Integer roleId, HttpServletRequest request){
        if (roleId != null) {
            Role role = roleService.selectByPrimaryKey(roleId);
            request.setAttribute("role", role);
        }
        return "role/add_update";
    }

    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mIds, Integer roleId){
        roleService.addGrant(mIds, roleId);
        return success();
    }
}
