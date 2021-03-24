package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaComputerPurchaseDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerPurchaseDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerPurchaseDetail record);

    FiveOaComputerPurchaseDetail selectByPrimaryKey(Integer id);

    List<FiveOaComputerPurchaseDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerPurchaseDetail record);
}