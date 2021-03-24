package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrInvent;
import java.util.List;
import java.util.Map;

public interface HrInventMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrInvent record);

    HrInvent selectByPrimaryKey(Integer id);

    List<HrInvent> selectAll(Map params);

    int updateByPrimaryKey(HrInvent record);
}