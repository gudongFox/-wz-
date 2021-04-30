package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonTreeCategory;
import java.util.List;
import java.util.Map;

public interface CommonTreeCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonTreeCategory record);

    CommonTreeCategory selectByPrimaryKey(Integer id);

    List<CommonTreeCategory> selectAll(Map params);

    int updateByPrimaryKey(CommonTreeCategory record);
}