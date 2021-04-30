package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessConsultingContractStatistics;
import java.util.List;
import java.util.Map;

public interface FiveBusinessConsultingContractStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessConsultingContractStatistics record);

    FiveBusinessConsultingContractStatistics selectByPrimaryKey(Integer id);

    List<FiveBusinessConsultingContractStatistics> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessConsultingContractStatistics record);
}