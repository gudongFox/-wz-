package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonFormData;
import java.util.List;
import java.util.Map;

public interface CommonFormDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonFormData record);

    CommonFormData selectByPrimaryKey(Integer id);

    List<CommonFormData> selectAll(Map params);

    List<String> selectBusinessKey(Map params);

    int updateByPrimaryKey(CommonFormData record);

    CommonFormData selectByBusinessKey(String businessKey);
}