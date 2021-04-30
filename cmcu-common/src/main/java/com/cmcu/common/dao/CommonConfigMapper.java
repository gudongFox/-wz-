package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonConfig;
import java.util.List;
import java.util.Map;

public interface CommonConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonConfig record);

    CommonConfig selectByPrimaryKey(Integer id);

    List<CommonConfig> selectAll(Map params);

    int updateByPrimaryKey(CommonConfig record);
}