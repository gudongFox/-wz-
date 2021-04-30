package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonRequest;

import java.util.List;
import java.util.Map;

public interface CommonRequestMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CommonRequest record);

    CommonRequest selectByPrimaryKey(Long id);

    List<CommonRequest> selectAll(Map params);

    int updateByPrimaryKey(CommonRequest record);

    List<CommonRequest> listIdentifiedRequestName(String tenetId);
}