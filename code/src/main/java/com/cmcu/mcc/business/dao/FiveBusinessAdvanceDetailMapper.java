package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessAdvanceDetail;
import java.util.List;
import java.util.Map;

public interface FiveBusinessAdvanceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessAdvanceDetail record);

    FiveBusinessAdvanceDetail selectByPrimaryKey(Integer id);

    List<FiveBusinessAdvanceDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessAdvanceDetail record);
}