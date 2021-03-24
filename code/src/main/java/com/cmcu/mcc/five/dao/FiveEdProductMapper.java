package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdProduct;
import java.util.List;
import java.util.Map;

public interface FiveEdProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdProduct record);

    FiveEdProduct selectByPrimaryKey(Integer id);

    List<FiveEdProduct> selectAll(Map params);

    int updateByPrimaryKey(FiveEdProduct record);
}