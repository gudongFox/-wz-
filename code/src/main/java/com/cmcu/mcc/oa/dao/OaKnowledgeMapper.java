package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaKnowledge;
import java.util.List;
import java.util.Map;

public interface OaKnowledgeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaKnowledge record);

    OaKnowledge selectByPrimaryKey(Integer id);

    List<OaKnowledge> selectAll(Map params);

    int updateByPrimaryKey(OaKnowledge record);
}