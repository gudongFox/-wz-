package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdArrange;
import java.util.List;
import java.util.Map;

public interface CommonEdArrangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdArrange record);

    CommonEdArrange selectByPrimaryKey(Integer id);

    List<CommonEdArrange> selectAll(Map params);

    int updateByPrimaryKey(CommonEdArrange record);
}