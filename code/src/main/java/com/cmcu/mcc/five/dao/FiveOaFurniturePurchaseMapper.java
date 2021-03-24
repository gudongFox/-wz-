package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaFurniturePurchase;
import java.util.List;
import java.util.Map;

public interface FiveOaFurniturePurchaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaFurniturePurchase record);

    FiveOaFurniturePurchase selectByPrimaryKey(Integer id);

    List<FiveOaFurniturePurchase> selectAll(Map params);

    int updateByPrimaryKey(FiveOaFurniturePurchase record);
}