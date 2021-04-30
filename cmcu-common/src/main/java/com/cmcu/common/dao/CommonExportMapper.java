package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonExport;
import java.util.List;
import java.util.Map;

public interface CommonExportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonExport record);

    CommonExport selectByPrimaryKey(Integer id);

    List<CommonExport> selectAll(Map params);

    int updateByPrimaryKey(CommonExport record);
}