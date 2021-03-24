package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdServiceHandle;
import java.util.List;
import java.util.Map;

public interface FiveEdServiceHandleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdServiceHandle record);

    FiveEdServiceHandle selectByPrimaryKey(Integer id);

    List<FiveEdServiceHandle> selectAll(Map params);

    int updateByPrimaryKey(FiveEdServiceHandle record);
}