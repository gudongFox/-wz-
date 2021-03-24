package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdPlotApply;
import java.util.List;
import java.util.Map;

public interface FiveEdPlotApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdPlotApply record);

    FiveEdPlotApply selectByPrimaryKey(Integer id);

    List<FiveEdPlotApply> selectAll(Map params);

    int updateByPrimaryKey(FiveEdPlotApply record);
}