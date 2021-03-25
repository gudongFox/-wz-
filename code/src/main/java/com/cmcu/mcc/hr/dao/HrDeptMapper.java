package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrDept;
import java.util.List;
import java.util.Map;

public interface HrDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrDept record);

    HrDept selectByPrimaryKey(Integer id);

    List<HrDept> selectAll(Map params);
    //List<HrDept> selectDeptLeaderAndFinancer(Map params);

    int updateByPrimaryKey(HrDept record);
}