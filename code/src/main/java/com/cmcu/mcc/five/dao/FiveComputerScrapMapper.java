package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveComputerScrap;
import java.util.List;
import java.util.Map;

public interface FiveComputerScrapMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveComputerScrap record);

    FiveComputerScrap selectByPrimaryKey(Integer id);

    List<FiveComputerScrap> selectAll(Map params);

    int updateByPrimaryKey(FiveComputerScrap record);
}