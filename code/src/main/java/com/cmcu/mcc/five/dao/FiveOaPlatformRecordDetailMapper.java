package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaPlatformRecordDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaPlatformRecordDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaPlatformRecordDetail record);

    FiveOaPlatformRecordDetail selectByPrimaryKey(Integer id);

    List<FiveOaPlatformRecordDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaPlatformRecordDetail record);
}