package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaExternalStandardApply;
import java.util.List;
import java.util.Map;

public interface FiveOaExternalStandardApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaExternalStandardApply record);

    FiveOaExternalStandardApply selectByPrimaryKey(Integer id);

    List<FiveOaExternalStandardApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaExternalStandardApply record);
}