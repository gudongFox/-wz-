package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaEmployeeTransferExamine;
import java.util.List;
import java.util.Map;

public interface FiveOaEmployeeTransferExamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaEmployeeTransferExamine record);

    FiveOaEmployeeTransferExamine selectByPrimaryKey(Integer id);

    List<FiveOaEmployeeTransferExamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaEmployeeTransferExamine record);
}