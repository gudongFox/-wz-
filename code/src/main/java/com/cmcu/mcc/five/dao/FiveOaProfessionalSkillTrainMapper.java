package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaProfessionalSkillTrain;
import java.util.List;
import java.util.Map;

public interface FiveOaProfessionalSkillTrainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaProfessionalSkillTrain record);

    FiveOaProfessionalSkillTrain selectByPrimaryKey(Integer id);

    List<FiveOaProfessionalSkillTrain> selectAll(Map params);

    int updateByPrimaryKey(FiveOaProfessionalSkillTrain record);
}