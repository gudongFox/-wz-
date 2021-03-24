package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdTaskUser;
import java.util.List;
import java.util.Map;

public interface FiveEdTaskUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdTaskUser record);

    FiveEdTaskUser selectByPrimaryKey(Integer id);

    List<FiveEdTaskUser> selectAll(Map params);

    int updateByPrimaryKey(FiveEdTaskUser record);
}