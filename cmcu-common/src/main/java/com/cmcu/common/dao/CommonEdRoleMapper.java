package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdRole;
import java.util.List;
import java.util.Map;

public interface CommonEdRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdRole record);

    CommonEdRole selectByPrimaryKey(Integer id);

    List<CommonEdRole> selectAll(Map params);

    int updateByPrimaryKey(CommonEdRole record);
}