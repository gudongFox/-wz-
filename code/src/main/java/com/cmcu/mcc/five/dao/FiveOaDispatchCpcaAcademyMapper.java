package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaDispatchCpcaAcademy;
import java.util.List;
import java.util.Map;

public interface FiveOaDispatchCpcaAcademyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaDispatchCpcaAcademy record);

    FiveOaDispatchCpcaAcademy selectByPrimaryKey(Integer id);

    List<FiveOaDispatchCpcaAcademy> selectAll(Map params);

    int updateByPrimaryKey(FiveOaDispatchCpcaAcademy record);
}