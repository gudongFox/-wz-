package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveHrQualifyApplyDetail;
import java.util.List;
import java.util.Map;

public interface FiveHrQualifyApplyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveHrQualifyApplyDetail record);

    FiveHrQualifyApplyDetail selectByPrimaryKey(Integer id);

    List<FiveHrQualifyApplyDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveHrQualifyApplyDetail record);
}