package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaTrainOut;
import java.util.List;
import java.util.Map;

public interface OaTrainOutMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaTrainOut record);

    OaTrainOut selectByPrimaryKey(Integer id);

    List<OaTrainOut> selectAll(Map params);

    int updateByPrimaryKey(OaTrainOut record);
}