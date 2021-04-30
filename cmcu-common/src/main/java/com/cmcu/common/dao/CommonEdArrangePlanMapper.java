package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdArrangePlan;
import java.util.List;
import java.util.Map;

public interface CommonEdArrangePlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdArrangePlan record);

    CommonEdArrangePlan selectByPrimaryKey(Integer id);

    List<CommonEdArrangePlan> selectAll(Map params);

    int updateByPrimaryKey(CommonEdArrangePlan record);
}