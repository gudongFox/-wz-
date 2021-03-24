package com.cmcu.mcc.supervise.dao;

import com.cmcu.mcc.supervise.entity.FiveOaSuperviseDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaSuperviseDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaSuperviseDetail record);

    FiveOaSuperviseDetail selectByPrimaryKey(Integer id);

    List<FiveOaSuperviseDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaSuperviseDetail record);
}