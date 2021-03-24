package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.BusinessContractDetail;
import java.util.List;
import java.util.Map;

public interface BusinessContractDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessContractDetail record);

    BusinessContractDetail selectByPrimaryKey(Integer id);

    List<BusinessContractDetail> selectAll(Map params);

    int updateByPrimaryKey(BusinessContractDetail record);

    BusinessContractDetail selectByContractId(int contractId);
}