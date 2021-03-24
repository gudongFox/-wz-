package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaSignQuote;
import java.util.List;
import java.util.Map;

public interface FiveOaSignQuoteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaSignQuote record);

    FiveOaSignQuote selectByPrimaryKey(Integer id);

    List<FiveOaSignQuote> selectAll(Map params);

    int updateByPrimaryKey(FiveOaSignQuote record);
}