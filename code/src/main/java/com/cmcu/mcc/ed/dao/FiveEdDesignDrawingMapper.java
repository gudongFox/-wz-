package com.cmcu.mcc.ed.dao;

import com.cmcu.mcc.ed.entity.FiveEdDesignDrawing;
import java.util.List;
import java.util.Map;

public interface FiveEdDesignDrawingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdDesignDrawing record);

    FiveEdDesignDrawing selectByPrimaryKey(Integer id);

    List<FiveEdDesignDrawing> selectAll(Map params);

    int updateByPrimaryKey(FiveEdDesignDrawing record);
}