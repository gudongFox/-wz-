package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaSoftware;
import java.util.List;
import java.util.Map;

public interface FiveOaSoftwareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaSoftware record);

    FiveOaSoftware selectByPrimaryKey(Integer id);

    List<FiveOaSoftware> selectAll(Map params);

    int updateByPrimaryKey(FiveOaSoftware record);
}