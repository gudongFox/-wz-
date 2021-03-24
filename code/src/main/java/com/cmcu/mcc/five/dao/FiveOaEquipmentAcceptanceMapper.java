package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptance;
import java.util.List;
import java.util.Map;

public interface FiveOaEquipmentAcceptanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaEquipmentAcceptance record);

    FiveOaEquipmentAcceptance selectByPrimaryKey(Integer id);

    List<FiveOaEquipmentAcceptance> selectAll(Map params);

    int updateByPrimaryKey(FiveOaEquipmentAcceptance record);
}