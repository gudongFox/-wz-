package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaContractSign;
import java.util.List;
import java.util.Map;

public interface FiveOaContractSignMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaContractSign record);

    FiveOaContractSign selectByPrimaryKey(Integer id);

    List<FiveOaContractSign> selectAll(Map params);

    int updateByPrimaryKey(FiveOaContractSign record);
}