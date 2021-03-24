package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaContractLawExamine;
import java.util.List;
import java.util.Map;

public interface FiveOaContractLawExamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaContractLawExamine record);

    FiveOaContractLawExamine selectByPrimaryKey(Integer id);

    List<FiveOaContractLawExamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaContractLawExamine record);
}