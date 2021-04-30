package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessStatistics;
import java.util.List;
import java.util.Map;

public interface FiveBusinessStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessStatistics record);

    FiveBusinessStatistics selectByPrimaryKey(Integer id);

    List<FiveBusinessStatistics> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessStatistics record);
}