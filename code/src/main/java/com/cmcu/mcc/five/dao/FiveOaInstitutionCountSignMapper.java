package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaInstitutionCountSign;
import java.util.List;
import java.util.Map;

public interface FiveOaInstitutionCountSignMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaInstitutionCountSign record);

    FiveOaInstitutionCountSign selectByPrimaryKey(Integer id);

    List<FiveOaInstitutionCountSign> selectAll(Map params);

    int updateByPrimaryKey(FiveOaInstitutionCountSign record);
}