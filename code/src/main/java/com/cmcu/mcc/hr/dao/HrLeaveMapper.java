package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.entity.HrLeave;
import java.util.List;
import java.util.Map;

public interface HrLeaveMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrLeave record);

    HrLeave selectByPrimaryKey(Integer id);

    List<HrLeave> selectAll(Map params);

    int updateByPrimaryKey(HrLeave record);
}