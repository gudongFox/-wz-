package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentExamineListDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationEquipmentExamineListDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationEquipmentExamineListDetail record);

    FiveOaInformationEquipmentExamineListDetail selectByPrimaryKey(Integer id);

    List<FiveOaInformationEquipmentExamineListDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationEquipmentExamineListDetail record);
}