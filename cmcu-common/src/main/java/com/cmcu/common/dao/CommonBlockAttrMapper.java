package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonBlockAttr;
import java.util.List;
import java.util.Map;

public interface CommonBlockAttrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonBlockAttr record);

    CommonBlockAttr selectByPrimaryKey(Integer id);

    List<CommonBlockAttr> selectAll(Map params);

    int updateByPrimaryKey(CommonBlockAttr record);
}