package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaArchive;
import java.util.List;
import java.util.Map;

public interface OaArchiveMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaArchive record);

    OaArchive selectByPrimaryKey(Integer id);

    List<OaArchive> selectAll(Map params);

    int updateByPrimaryKey(OaArchive record);
}