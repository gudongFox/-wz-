package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonCategory;
import java.util.List;
import java.util.Map;

public interface CommonCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonCategory record);

    CommonCategory selectByPrimaryKey(Integer id);

    List<CommonCategory> selectAll(Map params);

    int updateByPrimaryKey(CommonCategory record);
}