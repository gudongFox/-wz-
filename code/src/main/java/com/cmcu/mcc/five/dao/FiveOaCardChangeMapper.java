package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaCardChange;
import java.util.List;
import java.util.Map;

public interface FiveOaCardChangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaCardChange record);

    FiveOaCardChange selectByPrimaryKey(Integer id);

    List<FiveOaCardChange> selectAll(Map params);

    int updateByPrimaryKey(FiveOaCardChange record);
}