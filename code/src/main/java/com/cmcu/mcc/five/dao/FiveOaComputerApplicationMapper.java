package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaComputerApplication;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerApplicationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerApplication record);

    FiveOaComputerApplication selectByPrimaryKey(Integer id);

    List<FiveOaComputerApplication> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerApplication record);
}