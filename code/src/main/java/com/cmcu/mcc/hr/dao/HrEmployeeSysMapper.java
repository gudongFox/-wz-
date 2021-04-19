package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrEmployeeSys;
import java.util.List;
import java.util.Map;

public interface HrEmployeeSysMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrEmployeeSys record);

    HrEmployeeSys selectByPrimaryKey(Integer id);

    List<HrEmployeeSys> selectAll(Map params);

    int updateByPrimaryKey(HrEmployeeSys record);

    HrEmployeeSys selectLastOne();

    HrEmployeeSys selectByUserLogin(String userLogin);

    Map getKeyInfoByEnLogin(String enLogin);
}