package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaReport;
import java.util.List;
import java.util.Map;

public interface FiveOaReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaReport record);

    FiveOaReport selectByPrimaryKey(Integer id);

    List<FiveOaReport> selectAll(Map params);

    int updateByPrimaryKey(FiveOaReport record);
}