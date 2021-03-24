package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdHouseRefer;
import java.util.List;
import java.util.Map;

public interface FiveEdHouseReferMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdHouseRefer record);

    FiveEdHouseRefer selectByPrimaryKey(Integer id);

    List<FiveEdHouseRefer> selectAll(Map params);

    int updateByPrimaryKey(FiveEdHouseRefer record);
}