package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrContract;

import java.util.List;
import java.util.Map;

public interface HrContractMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrContract record);

    HrContract selectByPrimaryKey(Integer id);

    List<HrContract> selectAll(Map params);

    int updateByPrimaryKey(HrContract record);
}