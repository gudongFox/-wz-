package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaPressurePipSealExamine;
import java.util.List;
import java.util.Map;

public interface FiveOaPressurePipSealExamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaPressurePipSealExamine record);

    FiveOaPressurePipSealExamine selectByPrimaryKey(Integer id);

    List<FiveOaPressurePipSealExamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaPressurePipSealExamine record);
}