package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaNonSecretEquipmentScrap;
import java.util.List;
import java.util.Map;

public interface FiveOaNonSecretEquipmentScrapMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaNonSecretEquipmentScrap record);

    FiveOaNonSecretEquipmentScrap selectByPrimaryKey(Integer id);

    List<FiveOaNonSecretEquipmentScrap> selectAll(Map params);

    int updateByPrimaryKey(FiveOaNonSecretEquipmentScrap record);
}