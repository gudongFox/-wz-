package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaWordSize;
import java.util.List;
import java.util.Map;

public interface FiveOaWordSizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaWordSize record);

    FiveOaWordSize selectByPrimaryKey(Integer id);

    List<FiveOaWordSize> selectAll(Map params);

    int updateByPrimaryKey(FiveOaWordSize record);
}