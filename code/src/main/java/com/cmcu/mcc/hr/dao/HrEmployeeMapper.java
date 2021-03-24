package com.cmcu.mcc.hr.dao;

import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSimpleDto;
import com.cmcu.mcc.hr.entity.HrEmployee;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HrEmployeeMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(HrEmployee record);

    HrEmployeeDto selectByPrimaryKey(Integer id);

    HrEmployee selectByName(String cnName);

    List<HrEmployeeDto> selectAll(Map params);

    int updateByPrimaryKey(HrEmployee record);

    HrEmployeeDto selectByUserLoginOrNo(String userLogin);

    String getNameByUserLogin(String userLogin);

    List<Map> listByUserLoginList(@Param("userLoginList") String[] userLoginList);

    List<HrEmployeeSimpleDto> selectSimpleAll(Map params);

    List<HrEmployeeSimpleDto> selectOaSimpleAll(Map map);

    List<HrEmployeeSimpleDto> selectOaRoleSimpleAll(Map map);
}