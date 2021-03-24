package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaDepartmentPost;
import java.util.List;
import java.util.Map;

public interface FiveOaDepartmentPostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaDepartmentPost record);

    FiveOaDepartmentPost selectByPrimaryKey(Integer id);

    List<FiveOaDepartmentPost> selectAll(Map params);

    int updateByPrimaryKey(FiveOaDepartmentPost record);
}