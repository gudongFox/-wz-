package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrPosition;
import java.util.List;
import java.util.Map;

public interface HrPositionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrPosition record);

    HrPosition selectByPrimaryKey(Integer id);

    List<HrPosition> selectAll(Map params);

    int updateByPrimaryKey(HrPosition record);
}