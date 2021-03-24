package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdPlotApplyDetail;
import java.util.List;
import java.util.Map;

public interface FiveEdPlotApplyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdPlotApplyDetail record);

    FiveEdPlotApplyDetail selectByPrimaryKey(Integer id);

    List<FiveEdPlotApplyDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveEdPlotApplyDetail record);
}