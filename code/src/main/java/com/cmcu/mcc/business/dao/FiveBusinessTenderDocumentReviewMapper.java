package com.cmcu.mcc.business.dao;

import com.cmcu.mcc.business.entity.FiveBusinessTenderDocumentReview;
import java.util.List;
import java.util.Map;

public interface FiveBusinessTenderDocumentReviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveBusinessTenderDocumentReview record);

    FiveBusinessTenderDocumentReview selectByPrimaryKey(Integer id);

    List<FiveBusinessTenderDocumentReview> selectAll(Map params);

    int updateByPrimaryKey(FiveBusinessTenderDocumentReview record);
}