package com.cmcu.mcc.supervise.dao;

import com.cmcu.mcc.supervise.entity.FiveOaSuperviseFile;
import java.util.List;
import java.util.Map;

public interface FiveOaSuperviseFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaSuperviseFile record);

    FiveOaSuperviseFile selectByPrimaryKey(Integer id);

    List<FiveOaSuperviseFile> selectAll(Map params);

    int updateByPrimaryKey(FiveOaSuperviseFile record);
}