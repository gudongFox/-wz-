package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaAssociationApply;
import java.util.List;
import java.util.Map;

public interface FiveOaAssociationApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaAssociationApply record);

    FiveOaAssociationApply selectByPrimaryKey(Integer id);

    List<FiveOaAssociationApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaAssociationApply record);
}