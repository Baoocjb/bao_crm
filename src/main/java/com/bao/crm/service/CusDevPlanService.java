package com.bao.crm.service;

import com.bao.crm.base.BaseService;
import com.bao.crm.dao.CusDevPlanMapper;
import com.bao.crm.query.CusDevPlanQuery;
import com.bao.crm.query.SaleChanceQuery;
import com.bao.crm.utils.AssertUtil;
import com.bao.crm.vo.CusDevPlan;
import com.bao.crm.vo.SaleChance;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    /**
     * 多条件分页查询
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String, Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        // 配置分页参数
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(cusDevPlan == null, "待添加记录不存在!");
        checkCusDevPlanParams(cusDevPlan);
        // 设置默认值
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setIsValid(1);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) < 1, "添加客户开发计划失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(cusDevPlan == null
                || cusDevPlan.getId() == null
                || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null, "待更新记录不存在!");
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "修改客户开发计划失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id){
        AssertUtil.isTrue(id == null, "所选删除id不能为空!");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(cusDevPlan == null, "待删除记录不存在");
        cusDevPlan.setUpdateDate(new Date());
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "删除客户开发计划失败!");
    }

    /**
     * 参数校验
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(cusDevPlan.getSaleChanceId() == null, "营销计划Id不能为空!");
        AssertUtil.isTrue(cusDevPlan.getPlanItem() == null, "计划项不能为空!");
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null, "计划时间不能为空!");
    }
}
