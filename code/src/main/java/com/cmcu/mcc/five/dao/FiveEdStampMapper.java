package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdStamp;
import java.util.List;
import java.util.Map;

public interface FiveEdStampMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdStamp record);

    FiveEdStamp selectByPrimaryKey(Integer id);

    List<FiveEdStamp> selectAll(Map params);

    int updateByPrimaryKey(FiveEdStamp record);
}