package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaDispatchAcademy;
import java.util.List;
import java.util.Map;

public interface FiveOaDispatchAcademyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaDispatchAcademy record);

    FiveOaDispatchAcademy selectByPrimaryKey(Integer id);

    List<FiveOaDispatchAcademy> selectAll(Map params);

    int updateByPrimaryKey(FiveOaDispatchAcademy record);
}