package com.cmcu.mcc.oa.dao;

import com.cmcu.mcc.oa.entity.OaMeetingRoomApply;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OaMeetingRoomApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OaMeetingRoomApply record);

    OaMeetingRoomApply selectByPrimaryKey(Integer id);

    List<OaMeetingRoomApply> selectAll(Map params);

    int updateByPrimaryKey(OaMeetingRoomApply record);

}