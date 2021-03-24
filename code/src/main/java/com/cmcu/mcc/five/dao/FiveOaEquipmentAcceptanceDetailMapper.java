package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaEquipmentAcceptanceDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaEquipmentAcceptanceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaEquipmentAcceptanceDetail record);

    FiveOaEquipmentAcceptanceDetail selectByPrimaryKey(Integer id);

    List<FiveOaEquipmentAcceptanceDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaEquipmentAcceptanceDetail record);
}