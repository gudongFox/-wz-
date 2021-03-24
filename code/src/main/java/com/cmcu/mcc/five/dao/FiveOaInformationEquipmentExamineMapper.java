package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamine;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationEquipmentExamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationEquipmentExamine record);

    FiveOaInformationEquipmentExamine selectByPrimaryKey(Integer id);

    List<FiveOaInformationEquipmentExamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationEquipmentExamine record);
}