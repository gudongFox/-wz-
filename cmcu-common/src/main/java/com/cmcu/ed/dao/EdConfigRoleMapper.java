package com.cmcu.ed.dao;

import com.cmcu.ed.entity.EdConfigRole;
import java.util.List;
import java.util.Map;

public interface EdConfigRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EdConfigRole record);

    EdConfigRole selectByPrimaryKey(Integer id);

    List<EdConfigRole> selectAll(Map params);

    int updateByPrimaryKey(EdConfigRole record);
}