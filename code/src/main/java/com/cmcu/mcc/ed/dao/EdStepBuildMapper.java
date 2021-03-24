package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.EdStepBuild;
import java.util.List;
import java.util.Map;

public interface EdStepBuildMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdStepBuild record);

    EdStepBuild selectByPrimaryKey(Integer id);

    List<EdStepBuild> selectAll(Map params);

    int updateByPrimaryKey(EdStepBuild record);
}