package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdReviewSpecial;
import java.util.List;
import java.util.Map;

public interface FiveEdReviewSpecialMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdReviewSpecial record);

    FiveEdReviewSpecial selectByPrimaryKey(Integer id);

    List<FiveEdReviewSpecial> selectAll(Map params);

    int updateByPrimaryKey(FiveEdReviewSpecial record);
}