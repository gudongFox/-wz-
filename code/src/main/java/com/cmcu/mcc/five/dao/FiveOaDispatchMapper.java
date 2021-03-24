package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaDispatch;
import java.util.List;
import java.util.Map;

public interface FiveOaDispatchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaDispatch record);

    FiveOaDispatch selectByPrimaryKey(Integer id);

    List<FiveOaDispatch> selectAll(Map params);

    int updateByPrimaryKey(FiveOaDispatch record);

    List<FiveOaDispatch> fuzzySearch(Map params);
}