package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaExternalResearchProjectApply;
import java.util.List;
import java.util.Map;

public interface FiveOaExternalResearchProjectApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaExternalResearchProjectApply record);

    FiveOaExternalResearchProjectApply selectByPrimaryKey(Integer id);

    List<FiveOaExternalResearchProjectApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaExternalResearchProjectApply record);
}