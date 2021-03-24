package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaDispatchCpc;
import java.util.List;
import java.util.Map;

public interface FiveOaDispatchCpcMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaDispatchCpc record);

    FiveOaDispatchCpc selectByPrimaryKey(Integer id);

    List<FiveOaDispatchCpc> selectAll(Map params);

    int updateByPrimaryKey(FiveOaDispatchCpc record);
}