package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdReturnVisitDetail;
import java.util.List;
import java.util.Map;

public interface FiveEdReturnVisitDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdReturnVisitDetail record);

    FiveEdReturnVisitDetail selectByPrimaryKey(Integer id);

    List<FiveEdReturnVisitDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveEdReturnVisitDetail record);
}