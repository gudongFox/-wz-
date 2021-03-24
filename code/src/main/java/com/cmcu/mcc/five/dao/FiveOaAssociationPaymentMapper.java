package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaAssociationPayment;
import java.util.List;
import java.util.Map;

public interface FiveOaAssociationPaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaAssociationPayment record);

    FiveOaAssociationPayment selectByPrimaryKey(Integer id);

    List<FiveOaAssociationPayment> selectAll(Map params);

    int updateByPrimaryKey(FiveOaAssociationPayment record);
}