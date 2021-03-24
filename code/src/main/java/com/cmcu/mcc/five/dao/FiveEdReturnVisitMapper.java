package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdReturnVisit;
import java.util.List;
import java.util.Map;

public interface FiveEdReturnVisitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdReturnVisit record);

    FiveEdReturnVisit selectByPrimaryKey(Integer id);

    List<FiveEdReturnVisit> selectAll(Map params);

    int updateByPrimaryKey(FiveEdReturnVisit record);
}