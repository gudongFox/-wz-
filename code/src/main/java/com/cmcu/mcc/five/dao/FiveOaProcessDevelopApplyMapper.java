package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaProcessDevelopApply;
import java.util.List;
import java.util.Map;

public interface FiveOaProcessDevelopApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaProcessDevelopApply record);

    FiveOaProcessDevelopApply selectByPrimaryKey(Integer id);

    List<FiveOaProcessDevelopApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaProcessDevelopApply record);
}