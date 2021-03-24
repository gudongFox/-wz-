package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveOaTechnologyArticleExamine;
import java.util.List;
import java.util.Map;

public interface FiveOaTechnologyArticleExamineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveOaTechnologyArticleExamine record);

    FiveOaTechnologyArticleExamine selectByPrimaryKey(Integer id);

    List<FiveOaTechnologyArticleExamine> selectAll(Map params);

    int updateByPrimaryKey(FiveOaTechnologyArticleExamine record);
}