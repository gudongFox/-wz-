package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveHrQualifyChief;
import java.util.List;
import java.util.Map;

public interface FiveHrQualifyChiefMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveHrQualifyChief record);

    FiveHrQualifyChief selectByPrimaryKey(Integer id);

    List<FiveHrQualifyChief> selectAll(Map params);

    int updateByPrimaryKey(FiveHrQualifyChief record);
}