package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysRequest;
import java.util.List;
import java.util.Map;

public interface SysRequestMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRequest record);

    SysRequest selectByPrimaryKey(Long id);

    List<SysRequest> selectAll(Map parameter);

    int updateByPrimaryKey(SysRequest record);
}