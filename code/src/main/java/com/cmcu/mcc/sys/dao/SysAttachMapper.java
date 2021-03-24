package com.cmcu.mcc.sys.dao;

import com.cmcu.mcc.sys.entity.SysAttach;
import java.util.List;
import java.util.Map;

public interface SysAttachMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAttach record);

    SysAttach selectByPrimaryKey(Integer id);

    List<SysAttach> selectAll(Map params);

    int updateByPrimaryKey(SysAttach record);

    SysAttach selectByMd5(String md5);
}