package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.CoApp;
import java.util.List;
import java.util.Map;

public interface CoAppMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CoApp record);

    CoApp selectByPrimaryKey(Integer id);

    List<CoApp> selectAll(Map params);

    int updateByPrimaryKey(CoApp record);

    CoApp getLatest();
}