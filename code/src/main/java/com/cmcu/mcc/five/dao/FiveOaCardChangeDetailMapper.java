package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaCardChangeDetail;
import java.util.List;
import java.util.Map;

public interface FiveOaCardChangeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaCardChangeDetail record);

    FiveOaCardChangeDetail selectByPrimaryKey(Integer id);

    List<FiveOaCardChangeDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveOaCardChangeDetail record);
}