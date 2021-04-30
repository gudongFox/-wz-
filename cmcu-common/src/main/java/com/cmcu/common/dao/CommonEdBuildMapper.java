package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdBuild;
import java.util.List;
import java.util.Map;

public interface CommonEdBuildMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdBuild record);

    CommonEdBuild selectByPrimaryKey(Integer id);

    List<CommonEdBuild> selectAll(Map params);

    int updateByPrimaryKey(CommonEdBuild record);
}