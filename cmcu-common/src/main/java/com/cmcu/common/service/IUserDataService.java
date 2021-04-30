package com.cmcu.common.service;

import com.cmcu.common.dto.DeptDto;
import com.cmcu.common.dto.PositionDto;
import com.cmcu.common.dto.RoleDto;
import com.cmcu.common.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface IUserDataService {

    List<UserDto> selectAllUser(String tenetId);

    List<DeptDto> selectAllDept(String tenetId);

    List<DeptDto> selectAllDept(String tenetId, Integer parentId);


    List<PositionDto> selectAllPosition(String tenetId);

    List<RoleDto> selectAllRole(String tenetId);

    UserDto selectByEnLogin(String enLogin);

    String getNameByEnLogin(String enLogin);

    void updateProperty(String enLogin, Map property);

    void clearCache(String tenetId);

    List<Integer> getMyDeptList(String enLogin, String uiSref);

    Map getKeyInfoByEnLogin(String enLogin);

    String getTenetId(String enLogin);

}
