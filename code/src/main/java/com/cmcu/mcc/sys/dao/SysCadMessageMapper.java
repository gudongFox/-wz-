package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysCadMessage;
import java.util.List;
import java.util.Map;

public interface SysCadMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysCadMessage record);

    SysCadMessage selectByPrimaryKey(Integer id);

    List<SysCadMessage> selectAll(Map params);

    int updateByPrimaryKey(SysCadMessage record);
}