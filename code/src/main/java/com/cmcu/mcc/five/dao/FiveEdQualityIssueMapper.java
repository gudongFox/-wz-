package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdQualityIssue;
import java.util.List;
import java.util.Map;

public interface FiveEdQualityIssueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdQualityIssue record);

    FiveEdQualityIssue selectByPrimaryKey(Integer id);

    List<FiveEdQualityIssue> selectAll(Map params);

    int updateByPrimaryKey(FiveEdQualityIssue record);
}