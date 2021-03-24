package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdArrange;
import java.util.List;
import java.util.Map;

public interface FiveEdArrangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdArrange record);

    FiveEdArrange selectByPrimaryKey(Integer id);

    List<FiveEdArrange> selectAll(Map params);

    int updateByPrimaryKey(FiveEdArrange record);
}