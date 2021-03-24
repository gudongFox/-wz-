package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaExternalResearchProjectApplyDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaExternalResearchProjectApplyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaExternalResearchProjectApplyDetail record);

    FiveOaExternalResearchProjectApplyDetail selectByPrimaryKey(Integer id);

    List<FiveOaExternalResearchProjectApplyDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaExternalResearchProjectApplyDetail record);
}