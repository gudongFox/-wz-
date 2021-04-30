package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonEdMark;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonEdMarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonEdMark record);

    CommonEdMark selectByPrimaryKey(Integer id);

    CommonEdMark selectPre(@Param("businessKey") String businessKey, @Param("creator") String creator);

    List<CommonEdMark> selectAll(Map params);

    int updateByPrimaryKey(CommonEdMark record);

    List<Map> listLevelReport(Map params);
}