package com.cmcu.ed.dao;

import com.cmcu.ed.entity.EdConfigTreeTemplate;
import java.util.List;
import java.util.Map;

public interface EdConfigTreeTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdConfigTreeTemplate record);

    EdConfigTreeTemplate selectByPrimaryKey(Integer id);

    List<EdConfigTreeTemplate> selectAll(Map params);

    int updateByPrimaryKey(EdConfigTreeTemplate record);
}