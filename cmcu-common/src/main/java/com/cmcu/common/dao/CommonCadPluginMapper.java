package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonCadPlugin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonCadPluginMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonCadPlugin record);

    CommonCadPlugin selectByPrimaryKey(Integer id);

    List<CommonCadPlugin> selectAll(Map params);

    int updateByPrimaryKey(CommonCadPlugin record);

    CommonCadPlugin getLatest(@Param("tenetId") String tenetId,@Param("versionType") String versionType);
}