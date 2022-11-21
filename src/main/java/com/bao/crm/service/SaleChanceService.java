package com.bao.crm.service;

import com.bao.crm.base.BaseService;
import com.bao.crm.dao.SaleChanceMapper;
import com.bao.crm.vo.SaleChance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;
}
