package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaMaterialPurchaseDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaMaterialPurchaseDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaMaterialPurchaseDetail record);

    FiveOaMaterialPurchaseDetail selectByPrimaryKey(Integer id);

    List<FiveOaMaterialPurchaseDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaMaterialPurchaseDetail record);
}