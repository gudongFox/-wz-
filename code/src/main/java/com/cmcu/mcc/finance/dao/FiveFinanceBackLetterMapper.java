package com.cmcu.mcc.finance.dao;

import com.cmcu.mcc.finance.entity.FiveFinanceBackLetter;
import java.util.List;
import java.util.Map;

public interface FiveFinanceBackLetterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveFinanceBackLetter record);

    FiveFinanceBackLetter selectByPrimaryKey(Integer id);

    List<FiveFinanceBackLetter> selectAll(Map params);

    int updateByPrimaryKey(FiveFinanceBackLetter record);
}