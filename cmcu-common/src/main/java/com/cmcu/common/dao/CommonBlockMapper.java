package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonBlock;
import java.util.List;
import java.util.Map;

public interface CommonBlockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonBlock record);

    CommonBlock selectByPrimaryKey(Integer id);

    List<CommonBlock> selectAll(Map params);

    int updateByPrimaryKey(CommonBlock record);
}