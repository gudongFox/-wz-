package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMaterialPurchase;
import java.util.List;
import java.util.Map;

public interface FiveOaMaterialPurchaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMaterialPurchase record);

    FiveOaMaterialPurchase selectByPrimaryKey(Integer id);

    List<FiveOaMaterialPurchase> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMaterialPurchase record);
}