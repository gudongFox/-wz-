package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdServiceDiscuss;
import java.util.List;
import java.util.Map;

public interface FiveEdServiceDiscussMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdServiceDiscuss record);

    FiveEdServiceDiscuss selectByPrimaryKey(Integer id);

    List<FiveEdServiceDiscuss> selectAll(Map params);

    int updateByPrimaryKey(FiveEdServiceDiscuss record);
}