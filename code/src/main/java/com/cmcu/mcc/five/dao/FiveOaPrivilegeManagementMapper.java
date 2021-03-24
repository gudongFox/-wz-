package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaPrivilegeManagement;
import java.util.List;
import java.util.Map;

public interface FiveOaPrivilegeManagementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaPrivilegeManagement record);

    FiveOaPrivilegeManagement selectByPrimaryKey(Integer id);

    List<FiveOaPrivilegeManagement> selectAll(Map params);

    int updateByPrimaryKey(FiveOaPrivilegeManagement record);
}