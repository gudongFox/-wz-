package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaArchiveDetail;
import java.util.List;
import java.util.Map;

public interface OaArchiveDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaArchiveDetail record);

    OaArchiveDetail selectByPrimaryKey(Integer id);

    List<OaArchiveDetail> selectAll(Map params);

    int updateByPrimaryKey(OaArchiveDetail record);
}