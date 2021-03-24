package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdHouseValidate;
import java.util.List;
import java.util.Map;

public interface FiveEdHouseValidateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdHouseValidate record);

    FiveEdHouseValidate selectByPrimaryKey(Integer id);

    List<FiveEdHouseValidate> selectAll(Map params);

    int updateByPrimaryKey(FiveEdHouseValidate record);
}