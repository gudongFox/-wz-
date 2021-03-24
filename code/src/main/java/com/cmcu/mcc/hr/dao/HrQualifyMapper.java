package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrQualify;
import java.util.List;
import java.util.Map;

public interface HrQualifyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrQualify record);

    HrQualify selectByPrimaryKey(Integer id);

    List<HrQualify> selectAll(Map params);

    int updateByPrimaryKey(HrQualify record);

    HrQualify selectLatestByUserLogin(String userLogin);

    HrQualify selectLatest();
}