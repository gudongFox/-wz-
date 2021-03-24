package com.cmcu.mcc.service;

import com.cmcu.act.service.ProcessQueryService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class H5Service {


    @Resource
    ProcessQueryService processQueryService;



   
    public Map listStampProcessInstance(String initiator, String involvedUser,String enLogin, int firstResult, int maxResults) {
        Map params = Maps.newHashMap();
        params.put("processDefinitionKeys", Arrays.asList("oaStampApplyOffice"));
        
        return processQueryService.listProcessInstanceByH5(initiator, involvedUser,enLogin, params, firstResult, maxResults);
    }






}
