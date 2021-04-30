package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdDwgStd;
import java.util.List;
import java.util.Map;

public interface CommonEdDwgStdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdDwgStd record);

    CommonEdDwgStd selectByPrimaryKey(Integer id);

    List<CommonEdDwgStd> selectAll(Map params);

    int updateByPrimaryKey(CommonEdDwgStd record);
}