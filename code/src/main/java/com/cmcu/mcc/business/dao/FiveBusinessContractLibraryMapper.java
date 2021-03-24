package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import java.util.List;
import java.util.Map;

public interface FiveBusinessContractLibraryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessContractLibrary record);

    FiveBusinessContractLibrary selectByPrimaryKey(Integer id);

    List<FiveBusinessContractLibrary> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessContractLibrary record);
}