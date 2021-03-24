package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentProcurement;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationEquipmentProcurementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationEquipmentProcurement record);

    FiveOaInformationEquipmentProcurement selectByPrimaryKey(Integer id);

    List<FiveOaInformationEquipmentProcurement> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationEquipmentProcurement record);
}