package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrHonor;
import java.util.List;
import java.util.Map;

public interface HrHonorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrHonor record);

    HrHonor selectByPrimaryKey(Integer id);

    List<HrHonor> selectAll(Map params);

    int updateByPrimaryKey(HrHonor record);
}