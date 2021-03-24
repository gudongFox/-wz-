package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrCertification;
import java.util.List;
import java.util.Map;

public interface HrCertificationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrCertification record);

    HrCertification selectByPrimaryKey(Integer id);

    List<HrCertification> selectAll(Map params);

    int updateByPrimaryKey(HrCertification record);
}