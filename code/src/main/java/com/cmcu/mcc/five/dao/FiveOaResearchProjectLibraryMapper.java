package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaResearchProjectLibrary;
import java.util.List;
import java.util.Map;

public interface FiveOaResearchProjectLibraryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaResearchProjectLibrary record);

    FiveOaResearchProjectLibrary selectByPrimaryKey(Integer id);

    List<FiveOaResearchProjectLibrary> selectAll(Map params);

    int updateByPrimaryKey(FiveOaResearchProjectLibrary record);
}