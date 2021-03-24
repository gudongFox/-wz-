package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMessageExport;
import java.util.List;
import java.util.Map;

public interface FiveOaMessageExportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMessageExport record);

    FiveOaMessageExport selectByPrimaryKey(Integer id);

    List<FiveOaMessageExport> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMessageExport record);
}