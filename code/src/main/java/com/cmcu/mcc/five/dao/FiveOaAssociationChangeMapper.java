package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaAssociationChange;
import java.util.List;
import java.util.Map;

public interface FiveOaAssociationChangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaAssociationChange record);

    FiveOaAssociationChange selectByPrimaryKey(Integer id);

    List<FiveOaAssociationChange> selectAll(Map params);

    int updateByPrimaryKey(FiveOaAssociationChange record);
}