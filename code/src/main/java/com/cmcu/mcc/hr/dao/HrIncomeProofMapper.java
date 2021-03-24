package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrIncomeProof;
import java.util.List;
import java.util.Map;

public interface HrIncomeProofMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrIncomeProof record);

    HrIncomeProof selectByPrimaryKey(Integer id);

    List<HrIncomeProof> selectAll(Map params);

    int updateByPrimaryKey(HrIncomeProof record);
}