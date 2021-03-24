package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdDesignDrawingDetail;
import java.util.List;
import java.util.Map;

public interface FiveEdDesignDrawingDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdDesignDrawingDetail record);

    FiveEdDesignDrawingDetail selectByPrimaryKey(Integer id);

    List<FiveEdDesignDrawingDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveEdDesignDrawingDetail record);
}