package com.bao.crm.service;

import com.bao.crm.base.BaseService;
import com.bao.crm.dao.SaleChanceMapper;
import com.bao.crm.enums.DevResult;
import com.bao.crm.enums.StateStatus;
import com.bao.crm.query.SaleChanceQuery;
import com.bao.crm.utils.AssertUtil;
import com.bao.crm.utils.PhoneUtil;
import com.bao.crm.utils.UserHolder;
import com.bao.crm.vo.SaleChance;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String, Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        // 配置分页参数
        PageInfo<SaleChance> saleChancePageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", saleChancePageInfo.getTotal());
        map.put("data", saleChancePageInfo.getList());
        return map;
    }

    /**
     * 添加SaleChance
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        // 参数校验:客户名称非空, 联系人非空, 联系号码非空且格式正确
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        // 设置默认参数
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateMan(UserHolder.getUser().getUserName());
        saleChance.setIsValid(1);
        // 设置分配人
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
            saleChance.setState(StateStatus.UNSTATE.getType());
        }else{
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setState(StateStatus.STATED.getType());
        }
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1, "添加营销计划失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        // 参数校验
        AssertUtil.isTrue(saleChance == null || saleChance.getId() == null, "待更新记录不存在!");
        SaleChance oldSaleChance = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(oldSaleChance == null, "待更新记录不存在!");
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        // 待更新记录存在且校验通过
        // 判断是否有更改分配人
        boolean nowHaveAssignMan = !StringUtils.isBlank(saleChance.getAssignMan());
        if(StringUtils.isBlank(oldSaleChance.getAssignMan())){
            // 原本没有分配人时,继续判断
            if(nowHaveAssignMan){
                // 现在有分配人
                saleChance.setAssignTime(new Date());
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        }else{
            // 原本就有分配人

            if(nowHaveAssignMan){
                // 现在还有分配人
                if(!oldSaleChance.getAssignMan().equals(saleChance.getAssignMan())){
                    // 分配人前后都相同不用更改分配时间,否则要更改分配时间
                    saleChance.setAssignTime(new Date());
                }
            }else{
                // 现在没有分配人
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
                saleChance.setAssignTime(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
            }
        }
        // 更新修改时间
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "营销计划更新失败!");
    }


    /**
     * 校验SaleChance指定参数
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名称不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人名称不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系号码不能为空!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "联系号码格式错误!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delSaleChance(Integer[] ids){
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) < 1, "待删除记录不存在!");
    }

    /**
     * 更新开发状态
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue(id == null || devResult == null, "待开发记录不存在!");
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance == null, "待开发记录不存在!");
        saleChance.setDevResult(devResult);
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "修改开发状态失败!");
    }

}
