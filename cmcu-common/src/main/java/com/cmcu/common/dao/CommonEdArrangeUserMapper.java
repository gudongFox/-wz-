package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdArrangeUser;
import java.util.List;
import java.util.Map;

public interface CommonEdArrangeUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdArrangeUser record);

    CommonEdArrangeUser selectByPrimaryKey(Integer id);

    List<CommonEdArrangeUser> selectAll(Map params);

    int updateByPrimaryKey(CommonEdArrangeUser record);
}