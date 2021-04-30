package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonCode;
import java.util.List;
import java.util.Map;

public interface CommonCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonCode record);

    CommonCode selectByPrimaryKey(Integer id);

    List<CommonCode> selectAll(Map params);

    int updateByPrimaryKey(CommonCode record);

    CommonCode getLatest(String tenetId);

    List<String> listCodeCategory(String tenetId);
}