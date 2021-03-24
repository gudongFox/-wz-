package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaCarApply;
import java.util.List;
import java.util.Map;

public interface FiveOaCarApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaCarApply record);

    FiveOaCarApply selectByPrimaryKey(Integer id);

    List<FiveOaCarApply> selectAll(Map params);

    int updateByPrimaryKey(FiveOaCarApply record);
}