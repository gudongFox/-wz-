package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrRegister;
import java.util.List;
import java.util.Map;

public interface HrRegisterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrRegister record);

    HrRegister selectByPrimaryKey(Integer id);

    List<HrRegister> selectAll(Map params);

    int updateByPrimaryKey(HrRegister record);
}