package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessContractReview;
import java.util.List;
import java.util.Map;

public interface FiveBusinessContractReviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessContractReview record);

    FiveBusinessContractReview selectByPrimaryKey(Integer id);

    List<FiveBusinessContractReview> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessContractReview record);
}