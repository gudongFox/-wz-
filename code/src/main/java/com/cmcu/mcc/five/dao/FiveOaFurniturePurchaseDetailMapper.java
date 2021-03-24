package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaFurniturePurchaseDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaFurniturePurchaseDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaFurniturePurchaseDetail record);

    FiveOaFurniturePurchaseDetail selectByPrimaryKey(Integer id);

    List<FiveOaFurniturePurchaseDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaFurniturePurchaseDetail record);
}