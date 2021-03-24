package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaFileSynergy;
import java.util.List;
import java.util.Map;

public interface FiveOaFileSynergyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaFileSynergy record);

    FiveOaFileSynergy selectByPrimaryKey(Integer id);

    List<FiveOaFileSynergy> selectAll(Map params);

    int updateByPrimaryKey(FiveOaFileSynergy record);
}