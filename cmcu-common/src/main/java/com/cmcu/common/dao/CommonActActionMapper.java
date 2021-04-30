package com.cmcu.common.dao;

import com.cmcu.common.entity.CommonActAction;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonActActionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommonActAction record);

    CommonActAction selectByPrimaryKey(Integer id);

    List<CommonActAction> selectAll(Map params);

    int updateByPrimaryKey(CommonActAction record);

    CommonActAction getModel(@Param("tenetId")String tenetId,@Param("processInstanceId") String processInstanceId,@Param("enLogin") String enLogin);
}