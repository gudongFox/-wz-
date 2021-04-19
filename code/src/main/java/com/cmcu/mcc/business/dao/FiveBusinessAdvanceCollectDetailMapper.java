package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessAdvanceCollectDetail;
import java.util.List;
import java.util.Map;

public interface FiveBusinessAdvanceCollectDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessAdvanceCollectDetail record);

    FiveBusinessAdvanceCollectDetail selectByPrimaryKey(Integer id);

    List<FiveBusinessAdvanceCollectDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessAdvanceCollectDetail record);
}