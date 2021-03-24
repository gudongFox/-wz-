package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentApplyDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaInformationEquipmentApplyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInformationEquipmentApplyDetail record);

    FiveOaInformationEquipmentApplyDetail selectByPrimaryKey(Integer id);

    List<FiveOaInformationEquipmentApplyDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInformationEquipmentApplyDetail record);
}