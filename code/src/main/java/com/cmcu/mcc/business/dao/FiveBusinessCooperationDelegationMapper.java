package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessCooperationDelegation;
import java.util.List;
import java.util.Map;

public interface FiveBusinessCooperationDelegationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessCooperationDelegation record);

    FiveBusinessCooperationDelegation selectByPrimaryKey(Integer id);

    List<FiveBusinessCooperationDelegation> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessCooperationDelegation record);
}