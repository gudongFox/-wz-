package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaTechnology;
import java.util.List;
import java.util.Map;

public interface OaTechnologyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaTechnology record);

    OaTechnology selectByPrimaryKey(Integer id);

    List<OaTechnology> selectAll(Map params);

    int updateByPrimaryKey(OaTechnology record);
}