package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessAdvanceCollect;
import java.util.List;
import java.util.Map;

public interface FiveBusinessAdvanceCollectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessAdvanceCollect record);

    FiveBusinessAdvanceCollect selectByPrimaryKey(Integer id);

    List<FiveBusinessAdvanceCollect> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessAdvanceCollect record);
}