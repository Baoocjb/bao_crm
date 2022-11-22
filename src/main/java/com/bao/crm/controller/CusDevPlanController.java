package com.bao.crm.controller;

import com.bao.crm.base.BaseController;
import com.bao.crm.base.ResultInfo;
import com.bao.crm.enums.StateStatus;
import com.bao.crm.query.CusDevPlanQuery;
import com.bao.crm.query.SaleChanceQuery;
import com.bao.crm.service.CusDevPlanService;
import com.bao.crm.service.SaleChanceService;
import com.bao.crm.utils.UserHolder;
import com.bao.crm.utils.UserIDBase64;
import com.bao.crm.vo.CusDevPlan;
import com.bao.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Resource
    private CusDevPlanService cusDevPlanService;

    @Resource
    private SaleChanceService saleChanceService;

    @GetMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    @GetMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        if(id != null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            request.setAttribute("saleChance", saleChance);
        }
        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 开发计划分页多条件查询
     * @param cusDevPlanQuery
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success();
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success();
    }

    @GetMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(Integer sId, HttpServletRequest request, Integer id){
        if(sId != null){
            request.setAttribute("sId", sId);
        }
        if (id != null) {
            request.setAttribute("cusDevPlan", cusDevPlanService.selectByPrimaryKey(id));
        }
        return "cusDevPlan/add_update";
    }

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success();
    }
}
