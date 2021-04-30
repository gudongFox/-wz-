package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdQuestion;
import java.util.List;
import java.util.Map;

public interface CommonEdQuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdQuestion record);

    CommonEdQuestion selectByPrimaryKey(Integer id);

    List<CommonEdQuestion> selectAll(Map params);

    int updateByPrimaryKey(CommonEdQuestion record);
}