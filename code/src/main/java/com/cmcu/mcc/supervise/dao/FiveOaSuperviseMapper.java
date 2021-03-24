package com.cmcu.mcc.supervise.dao;

import com.cmcu.mcc.supervise.entity.FiveOaSupervise;
import java.util.List;
import java.util.Map;

public interface FiveOaSuperviseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaSupervise record);

    FiveOaSupervise selectByPrimaryKey(Integer id);

    List<FiveOaSupervise> selectAll(Map params);

    int updateByPrimaryKey(FiveOaSupervise record);
}