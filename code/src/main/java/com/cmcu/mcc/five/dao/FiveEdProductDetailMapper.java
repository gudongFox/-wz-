package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdProductDetail;
import java.util.List;
import java.util.Map;

public interface FiveEdProductDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdProductDetail record);

    FiveEdProductDetail selectByPrimaryKey(Integer id);

    List<FiveEdProductDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveEdProductDetail record);
}