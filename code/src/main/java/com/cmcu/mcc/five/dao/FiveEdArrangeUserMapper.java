package com.cmcu.mcc.five.dao;

import com.cmcu.mcc.five.entity.FiveEdArrangeUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FiveEdArrangeUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FiveEdArrangeUser record);

    FiveEdArrangeUser selectByPrimaryKey(Integer id);

    List<FiveEdArrangeUser> selectAll(Map params);

    int updateByPrimaryKey(FiveEdArrangeUser record);

    List<FiveEdArrangeUser> listHistoryConfig(@Param("deptId") Integer deptId, @Param("majorName") String majorName);
}