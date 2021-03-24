package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApply;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationEquipmentApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationEquipmentApply record);

    FiveOaInformationEquipmentApply selectByPrimaryKey(Integer id);

    List<FiveOaInformationEquipmentApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationEquipmentApply record);
}