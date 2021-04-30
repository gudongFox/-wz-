package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdStamp;

import java.util.List;
import java.util.Map;

public interface CommonEdStampMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdStamp record);

    CommonEdStamp selectByPrimaryKey(Integer id);

    List<CommonEdStamp> selectAll(Map params);

    int updateByPrimaryKey(CommonEdStamp record);
}