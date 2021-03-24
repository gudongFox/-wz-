package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaOutSpecialist;
import java.util.List;
import java.util.Map;

public interface FiveOaOutSpecialistMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaOutSpecialist record);

    FiveOaOutSpecialist selectByPrimaryKey(Integer id);

    List<FiveOaOutSpecialist> selectAll(Map params);

    int updateByPrimaryKey(FiveOaOutSpecialist record);
}