package com.cmcu.common.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessHandleService;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.act.service.TaskQueryService;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonConfig;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BaseService {

    @Resource
    public TaskHandleService taskHandleService;

    @Resource
    public CommonCodeService commonCodeService;

    @Resource
    public ProcessQueryService processQueryService;

    @Resource
    public TaskQueryService taskQueryService;

    @Resource
    public ProcessHandleService processHandleService;

    @Resource
    public CommonUserService commonUserService;

    @Resource
    public CommonConfigService commonConfigService;

    @Resource
    IUserDataService userDataService;



    /**
     * 得到权限 myDeptData uiSref  enLogin
     * @param webParams
     * @return
     */
    public Map getDefaultRightParams(Map webParams) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        if (webParams.containsKey("uiSref") && webParams.containsKey("enLogin")) {
            String enLogin = webParams.get("enLogin").toString();
            String uiSref = webParams.get("uiSref").toString();
            boolean myDeptData = Boolean.parseBoolean(webParams.getOrDefault("myDeptData", "false").toString());
            List<Integer> deptIdList = userDataService.getMyDeptList(enLogin, uiSref);
            if (deptIdList.size() > 0) {
                params.put("deptIdList", deptIdList);
                params.put("initiator", enLogin);
            } else if (myDeptData) {
                UserDto userDto = userDataService.selectByEnLogin(enLogin);
                params.put("deptId", userDto.getDeptId());
            } else {
                params.put("creator", enLogin);
            }
        }
        return params;
    }



    /**
     * 得到流程详情
     * @param processInstanceId
     * @param businessKey
     * @param enLogin
     * @return
     */
    public CustomProcessInstance getCustomProcessInstance(String processInstanceId ,String businessKey,String enLogin){
       return  processQueryService.getCustomProcessInstance(processInstanceId,businessKey,enLogin);
    }



}
