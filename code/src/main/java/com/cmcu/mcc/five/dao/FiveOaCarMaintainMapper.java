package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaCarMaintain;
import java.util.List;
import java.util.Map;

public interface FiveOaCarMaintainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaCarMaintain record);

    FiveOaCarMaintain selectByPrimaryKey(Integer id);

    List<FiveOaCarMaintain> selectAll(Map params);

    int updateByPrimaryKey(FiveOaCarMaintain record);
}