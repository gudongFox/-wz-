package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessContractLibrarySubpackage;
import java.util.List;
import java.util.Map;

public interface FiveBusinessContractLibrarySubpackageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessContractLibrarySubpackage record);

    FiveBusinessContractLibrarySubpackage selectByPrimaryKey(Integer id);

    List<FiveBusinessContractLibrarySubpackage> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessContractLibrarySubpackage record);
}