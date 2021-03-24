package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaExternalStandardApplyDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaExternalStandardApplyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaExternalStandardApplyDetail record);

    FiveOaExternalStandardApplyDetail selectByPrimaryKey(Integer id);

    List<FiveOaExternalStandardApplyDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaExternalStandardApplyDetail record);
}