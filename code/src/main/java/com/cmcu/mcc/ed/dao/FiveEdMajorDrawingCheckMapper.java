package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.FiveEdMajorDrawingCheck;
import java.util.List;
import java.util.Map;

public interface FiveEdMajorDrawingCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdMajorDrawingCheck record);

    FiveEdMajorDrawingCheck selectByPrimaryKey(Integer id);

    List<FiveEdMajorDrawingCheck> selectAll(Map params);

    int updateByPrimaryKey(FiveEdMajorDrawingCheck record);
}