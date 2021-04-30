package com.cmcu.common.service;

import com.cmcu.common.dto.CommonEdArrangeUserDto;
import com.cmcu.common.entity.CommonEdArrangeUser;

import java.util.List;
import java.util.Map;

public interface IEdDataService {

    Map getNewEdData(String referType,int referId,String enLogin);

    List<CommonEdArrangeUserDto> listUser(int referId, String majorName, Integer buildId);

    Map getEdInformation(int formDataId);

    /**
     * 重置参与人员
     * @param businessKey
     */
    void checkAttendUser(String businessKey);

}
