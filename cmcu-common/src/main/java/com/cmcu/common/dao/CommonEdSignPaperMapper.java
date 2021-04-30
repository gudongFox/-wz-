package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdSignPaper;
import java.util.List;
import java.util.Map;

public interface CommonEdSignPaperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdSignPaper record);

    CommonEdSignPaper selectByPrimaryKey(Integer id);

    List<CommonEdSignPaper> selectAll(Map params);

    int updateByPrimaryKey(CommonEdSignPaper record);
}