package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.FiveEdMajorDrawingCheckDetail;
import java.util.List;
import java.util.Map;

public interface FiveEdMajorDrawingCheckDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdMajorDrawingCheckDetail record);

    FiveEdMajorDrawingCheckDetail selectByPrimaryKey(Integer id);

    List<FiveEdMajorDrawingCheckDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveEdMajorDrawingCheckDetail record);
}