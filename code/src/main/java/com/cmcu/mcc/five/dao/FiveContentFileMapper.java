package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveContentFile;
import java.util.List;
import java.util.Map;

public interface FiveContentFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveContentFile record);

    FiveContentFile selectByPrimaryKey(Integer id);

    List<FiveContentFile> selectAll(Map params);

    int updateByPrimaryKey(FiveContentFile record);
}