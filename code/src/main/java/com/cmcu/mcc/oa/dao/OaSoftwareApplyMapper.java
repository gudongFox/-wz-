package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaSoftwareApply;
import java.util.List;
import java.util.Map;

public interface OaSoftwareApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaSoftwareApply record);

    OaSoftwareApply selectByPrimaryKey(Integer id);

    List<OaSoftwareApply> selectAll(Map params);

    int updateByPrimaryKey(OaSoftwareApply record);
}