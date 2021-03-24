package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaArchiveApply;
import java.util.List;
import java.util.Map;

public interface OaArchiveApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaArchiveApply record);

    OaArchiveApply selectByPrimaryKey(Integer id);

    List<OaArchiveApply> selectAll(Map params);

    int updateByPrimaryKey(OaArchiveApply record);
}