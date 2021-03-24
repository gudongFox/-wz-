package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessCooperationContract;
import java.util.List;
import java.util.Map;

public interface FiveBusinessCooperationContractMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessCooperationContract record);

    FiveBusinessCooperationContract selectByPrimaryKey(Integer id);

    List<FiveBusinessCooperationContract> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessCooperationContract record);
}