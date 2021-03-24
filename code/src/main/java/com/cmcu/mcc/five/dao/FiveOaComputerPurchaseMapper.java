package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.dto.FiveOaComputerPurchaseDto;
import com.cmcu.mcc.five.entity.FiveOaComputerPurchase;
import java.util.List;
import java.util.Map;

public interface FiveOaComputerPurchaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaComputerPurchase record);

    FiveOaComputerPurchase selectByPrimaryKey(Integer id);

    List<FiveOaComputerPurchase> selectAll(Map params);

    int updateByPrimaryKey(FiveOaComputerPurchase record);
}