package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaHardware;
import java.util.List;
import java.util.Map;

public interface OaHardwareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaHardware record);

    OaHardware selectByPrimaryKey(Integer id);

    List<OaHardware> selectAll(Map params);

    int updateByPrimaryKey(OaHardware record);
}