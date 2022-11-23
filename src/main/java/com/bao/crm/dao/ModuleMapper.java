package com.bao.crm.dao;

import com.bao.crm.base.BaseMapper;
import com.bao.crm.model.TreeModel;
import com.bao.crm.vo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module, Integer> {

    List<TreeModel> queryAllModules();
}