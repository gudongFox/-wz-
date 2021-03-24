package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamineList;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationEquipmentExamineListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationEquipmentExamineList record);

    FiveOaInformationEquipmentExamineList selectByPrimaryKey(Integer id);

    List<FiveOaInformationEquipmentExamineList> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationEquipmentExamineList record);
}