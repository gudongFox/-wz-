package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrEmployeeProof;
import java.util.List;
import java.util.Map;

public interface HrEmployeeProofMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrEmployeeProof record);

    HrEmployeeProof selectByPrimaryKey(Integer id);

    List<HrEmployeeProof> selectAll(Map params);

    int updateByPrimaryKey(HrEmployeeProof record);
}