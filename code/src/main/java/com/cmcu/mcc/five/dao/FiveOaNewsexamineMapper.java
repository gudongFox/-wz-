package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaNewsexamine;
import java.util.List;
import java.util.Map;

public interface FiveOaNewsexamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaNewsexamine record);

    FiveOaNewsexamine selectByPrimaryKey(Integer id);

    List<FiveOaNewsexamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaNewsexamine record);
}