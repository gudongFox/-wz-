package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaPlatformRecord;
import java.util.List;
import java.util.Map;

public interface FiveOaPlatformRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaPlatformRecord record);

    FiveOaPlatformRecord selectByPrimaryKey(Integer id);

    List<FiveOaPlatformRecord> selectAll(Map params);

    int updateByPrimaryKey(FiveOaPlatformRecord record);
}