package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaComputerMaintain;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerMaintainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerMaintain record);

    FiveOaComputerMaintain selectByPrimaryKey(Integer id);

    List<FiveOaComputerMaintain> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerMaintain record);
}