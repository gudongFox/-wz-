package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.FiveEdDesignDrawingCheck;
import java.util.List;
import java.util.Map;

public interface FiveEdDesignDrawingCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdDesignDrawingCheck record);

    FiveEdDesignDrawingCheck selectByPrimaryKey(Integer id);

    List<FiveEdDesignDrawingCheck> selectAll(Map params);

    int updateByPrimaryKey(FiveEdDesignDrawingCheck record);
}