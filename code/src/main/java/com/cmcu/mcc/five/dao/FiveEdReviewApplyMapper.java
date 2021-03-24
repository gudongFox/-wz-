package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdReviewApply;
import java.util.List;
import java.util.Map;

public interface FiveEdReviewApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdReviewApply record);

    FiveEdReviewApply selectByPrimaryKey(Integer id);

    List<FiveEdReviewApply> selectAll(Map params);

    int updateByPrimaryKey(FiveEdReviewApply record);
}