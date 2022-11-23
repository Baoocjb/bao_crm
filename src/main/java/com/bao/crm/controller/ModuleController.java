package com.bao.crm.controller;

import com.bao.crm.base.BaseController;
import com.bao.crm.model.TreeModel;
import com.bao.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    @GetMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        if(roleId != null){
            request.setAttribute("roleId", roleId);
        }
        return "role/grant";
    }

    @GetMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }
}
