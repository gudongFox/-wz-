package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrInsure;
import java.util.List;
import java.util.Map;

public interface HrInsureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrInsure record);

    HrInsure selectByPrimaryKey(Integer id);

    List<HrInsure> selectAll(Map params);

    int updateByPrimaryKey(HrInsure record);
}