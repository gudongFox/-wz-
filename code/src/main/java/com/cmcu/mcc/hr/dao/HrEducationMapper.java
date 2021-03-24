package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrEducation;
import java.util.List;
import java.util.Map;

public interface HrEducationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrEducation record);

    HrEducation selectByPrimaryKey(Integer id);

    List<HrEducation> selectAll(Map params);

    int updateByPrimaryKey(HrEducation record);

    List<HrEducation> getGraduateSchool(Map map);
}