package com.cmcu.common.dao;


import com.cmcu.common.entity.CommonEdMarkMore;

import java.util.List;
import java.util.Map;

public interface CommonEdMarkMoreMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdMarkMore record);

    CommonEdMarkMore selectByPrimaryKey(Integer id);

    List<CommonEdMarkMore> selectAll(Map params);

    int updateByPrimaryKey(CommonEdMarkMore record);

    CommonEdMarkMore getLatest(int markId);
}