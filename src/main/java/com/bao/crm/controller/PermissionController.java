package com.bao.crm.controller;

import com.bao.crm.base.BaseController;
import com.bao.crm.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("permission")
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;
}
