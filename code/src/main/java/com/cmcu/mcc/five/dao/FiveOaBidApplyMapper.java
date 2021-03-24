package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaBidApply;
import com.cmcu.mcc.five.entity.FiveOaBidApply;
import java.util.List;
import java.util.Map;

public interface FiveOaBidApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaBidApply record);

    FiveOaBidApply selectByPrimaryKey(Integer id);

    List<FiveOaBidApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaBidApply record);
}