package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrVacationDetail;
import java.util.List;
import java.util.Map;

public interface HrVacationDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrVacationDetail record);

    HrVacationDetail selectByPrimaryKey(Integer id);

    List<HrVacationDetail> selectAll(Map params);

    int updateByPrimaryKey(HrVacationDetail record);
}