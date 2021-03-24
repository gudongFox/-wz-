package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaNonSecretEquipmentScrapDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaNonSecretEquipmentScrapDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaNonSecretEquipmentScrapDetail record);

    FiveOaNonSecretEquipmentScrapDetail selectByPrimaryKey(Integer id);

    List<FiveOaNonSecretEquipmentScrapDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaNonSecretEquipmentScrapDetail record);
}