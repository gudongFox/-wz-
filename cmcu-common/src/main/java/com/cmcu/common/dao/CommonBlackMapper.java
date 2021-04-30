package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonBlack;
import java.util.List;
import java.util.Map;

public interface CommonBlackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonBlack record);

    CommonBlack selectByPrimaryKey(Integer id);

    List<CommonBlack> selectAll(Map params);

    int updateByPrimaryKey(CommonBlack record);
}