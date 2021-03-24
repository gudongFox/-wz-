package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaDeptJournal;
import java.util.List;
import java.util.Map;

public interface FiveOaDeptJournalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaDeptJournal record);

    FiveOaDeptJournal selectByPrimaryKey(Integer id);

    List<FiveOaDeptJournal> selectAll(Map params);

    int updateByPrimaryKey(FiveOaDeptJournal record);
}