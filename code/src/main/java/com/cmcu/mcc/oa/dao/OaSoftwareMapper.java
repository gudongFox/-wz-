package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaSoftware;
import java.util.List;
import java.util.Map;

public interface OaSoftwareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaSoftware record);

    OaSoftware selectByPrimaryKey(Integer id);

    List<OaSoftware> selectAll(Map params);

    int updateByPrimaryKey(OaSoftware record);
}