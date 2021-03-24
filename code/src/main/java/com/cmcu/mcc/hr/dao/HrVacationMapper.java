package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrVacation;
import java.util.List;
import java.util.Map;

public interface HrVacationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrVacation record);

    HrVacation selectByPrimaryKey(Integer id);

    List<HrVacation> selectAll(Map params);

    int updateByPrimaryKey(HrVacation record);
}