package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveComputerScrapDetail;
import java.util.List;
import java.util.Map;

public interface FiveComputerScrapDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveComputerScrapDetail record);

    FiveComputerScrapDetail selectByPrimaryKey(Integer id);

    List<FiveComputerScrapDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveComputerScrapDetail record);
}