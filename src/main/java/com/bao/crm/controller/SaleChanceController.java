package com.bao.crm.controller;

import com.bao.crm.base.BaseController;
import com.bao.crm.base.ResultInfo;
import com.bao.crm.enums.StateStatus;
import com.bao.crm.query.SaleChanceQuery;
import com.bao.crm.service.SaleChanceService;
import com.bao.crm.utils.AssertUtil;
import com.bao.crm.utils.UserHolder;
import com.bao.crm.utils.UserIDBase64;
import com.bao.crm.vo.SaleChance;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 营销机会分页多条件查询
     * @param saleChanceQuery
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery, Integer flag){
        if(flag != null && flag == 1){
            saleChanceQuery.setState(StateStatus.STATED.getType());
            saleChanceQuery.setAssignMan(UserIDBase64.decoderUserID(UserHolder.getUser().getUserIdStr()));
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    @GetMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    @GetMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request){
        if(saleChanceId != null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            request.setAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }


    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance){
        saleChanceService.addSaleChance(saleChance);
        return success();
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success();
    }

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        AssertUtil.isTrue(ids == null || ids.length < 1, "待删除记录不存在!");
        saleChanceService.deleteBatch(ids);
        return success();
    }

    @PostMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id, Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id, devResult);
        return success();
    }
}
