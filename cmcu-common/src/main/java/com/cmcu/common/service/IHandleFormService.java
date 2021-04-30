package com.cmcu.common.service;


import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.dto.TplConfigDto;
import com.cmcu.common.entity.CommonFormData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IHandleFormService {

    void deleteFormData(String businessKey,String enLogin);

    void removeProcessInstance(String businessKey,String enLogin);

    LinkedHashMap getDefaultFormData(String referType, int stepId, String enLogin);

    Map getDefaultProcessVariables(String referType, int referId, String enLogin);

    TplConfigDto getTplConfigDto(String processInstanceId,String businessKey,String enLogin);

    List<CommonCodeDto> listDataSource(CommonFormData commonFormData, String dataSourceKey,String enLogin);

    List<Map> listUserJsTree(Map params);

    void checkFormData(String businessKey,String enLogin);

    String  getProcessInformation(String businessKey);

    Map afterSaveData(CommonFormData commonFormData,Map newData);

    String getNewEdFormData(int nodeId,String enLogin);

    List<Map> listFormChildList(String businessKey);
}
