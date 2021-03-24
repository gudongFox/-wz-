package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentProcurementDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationEquipmentProcurementDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationEquipmentProcurementDetail record);

    FiveOaInformationEquipmentProcurementDetail selectByPrimaryKey(Integer id);

    List<FiveOaInformationEquipmentProcurementDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationEquipmentProcurementDetail record);
}