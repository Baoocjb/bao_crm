package com.bao.crm.controller;

import com.bao.crm.base.BaseController;
import com.bao.crm.service.SaleChanceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

}
