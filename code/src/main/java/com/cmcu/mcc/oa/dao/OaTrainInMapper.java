package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaTrainIn;
import java.util.List;
import java.util.Map;

public interface OaTrainInMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaTrainIn record);

    OaTrainIn selectByPrimaryKey(Integer id);

    List<OaTrainIn> selectAll(Map params);

    int updateByPrimaryKey(OaTrainIn record);
}