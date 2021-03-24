package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveAssetComputer;
import java.util.List;
import java.util.Map;

public interface FiveAssetComputerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveAssetComputer record);

    FiveAssetComputer selectByPrimaryKey(Integer id);

    List<FiveAssetComputer> selectAll(Map params);

    int updateByPrimaryKey(FiveAssetComputer record);
}