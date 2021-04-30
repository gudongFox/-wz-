package com.cmcu.ed.dao;

import com.cmcu.ed.entity.EdConfigPlanDetail;
import java.util.List;
import java.util.Map;

public interface EdConfigPlanDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdConfigPlanDetail record);

    EdConfigPlanDetail selectByPrimaryKey(Integer id);

    List<EdConfigPlanDetail> selectAll(Map params);

    int updateByPrimaryKey(EdConfigPlanDetail record);
}