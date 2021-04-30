package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonForm;
import java.util.List;
import java.util.Map;

public interface CommonFormMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonForm record);

    CommonForm selectByPrimaryKey(Integer id);

    List<CommonForm> selectAll(Map params);

    int updateByPrimaryKey(CommonForm record);

    CommonForm getModelByFormKey(String formKey);
}