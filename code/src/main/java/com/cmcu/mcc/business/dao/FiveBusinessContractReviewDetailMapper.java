package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessContractReviewDetail;
import java.util.List;
import java.util.Map;

public interface FiveBusinessContractReviewDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessContractReviewDetail record);

    FiveBusinessContractReviewDetail selectByPrimaryKey(Integer id);

    List<FiveBusinessContractReviewDetail> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessContractReviewDetail record);
}