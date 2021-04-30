package com.cmcu.ed.dao;

import com.cmcu.ed.entity.EdConfigPlanTemplate;
import java.util.List;
import java.util.Map;

public interface EdConfigPlanTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdConfigPlanTemplate record);

    EdConfigPlanTemplate selectByPrimaryKey(Integer id);

    List<EdConfigPlanTemplate> selectAll(Map params);

    int updateByPrimaryKey(EdConfigPlanTemplate record);
}